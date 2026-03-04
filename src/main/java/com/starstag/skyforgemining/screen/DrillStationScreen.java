package com.starstag.skyforgemining.screen;

import com.starstag.skyforgemining.SkyforgeMining;
import com.starstag.skyforgemining.item.DrillItem;
import com.starstag.skyforgemining.item.drillpart.DrillPartItem;
import com.starstag.skyforgemining.item.drillpart.DrillPartType;
import com.starstag.skyforgemining.item.gemstone.GemstoneItem;
import com.starstag.skyforgemining.menu.DrillStationLogic;
import com.starstag.skyforgemining.menu.DrillStationMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DrillStationScreen extends AbstractContainerScreen<DrillStationMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(SkyforgeMining.MODID, "textures/gui/drill_station.png");

    private static final int MASTERY_Y  = 20;
    private static final int GEMS_Y     = 74;
    private static final int PARTS_Y    = 128;
    private static final int ITEMS_X   = 8;
    private static final int SLOT_SIZE = 18;

    public DrillStationScreen(DrillStationMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth  = 176;
        this.imageHeight = 196; // reduced from 240
    }

    // =========================
    // RENDER
    // =========================

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);

        net.minecraft.client.player.LocalPlayer player = this.minecraft.player;
        if (player == null) return;

        ItemStack drill = player.getMainHandItem();
        boolean hasDrill = drill.getItem() instanceof DrillItem;

        // Drill status header
        if (hasDrill) {
            graphics.drawString(font, "§a" + drill.getHoverName().getString(),
                    leftPos + 8, topPos + 6, 0xFFFFFF, false);
        } else {
            graphics.drawString(font, "§cHold a drill to upgrade",
                    leftPos + 8, topPos + 6, 0xFFFFFF, false);
        }

        // Section headers
        graphics.drawString(font, "§6§lMastery",   leftPos + 8, topPos + MASTERY_Y - 10, 0xFFFFAA00, false);
        graphics.drawString(font, "§b§lGemstones", leftPos + 8, topPos + GEMS_Y - 10,    0xFF55FFFF, false);
        graphics.drawString(font, "§7§lParts",     leftPos + 8, topPos + PARTS_Y - 10,   0xFFAAAAAA, false);

        // Render sections
        renderSection(graphics, player, drill, hasDrill,
                DrillStationLogic.getMasteryOptions(player), MASTERY_Y, mouseX, mouseY, 0);
        renderSection(graphics, player, drill, hasDrill,
                DrillStationLogic.getGemOptions(), GEMS_Y, mouseX, mouseY, 1);
        renderSection(graphics, player, drill, hasDrill,
                DrillStationLogic.getPartOptions(), PARTS_Y, mouseX, mouseY, 2);

        renderTooltip(graphics, mouseX, mouseY);
    }

    private void renderSection(GuiGraphics graphics, net.minecraft.client.player.LocalPlayer player,
                               ItemStack drill, boolean hasDrill,
                               List<ItemStack> options, int sectionY,
                               int mouseX, int mouseY, int section) {

        for (int i = 0; i < options.size(); i++) {
            ItemStack option = options.get(i);
            int col = i % 9;
            int row = i / 9;
            int x = leftPos + ITEMS_X + col * SLOT_SIZE;
            int y = topPos + sectionY + row * SLOT_SIZE;

            boolean canAfford = hasDrill && DrillStationLogic.hasItem(player, option);

            // Slot background
            graphics.fill(x, y, x + 16, y + 16, canAfford ? 0x44FFFFFF : 0x44333333);

            // Render item
            graphics.renderItem(option, x, y);
            graphics.renderItemDecorations(font, option, x, y);

            // Grey overlay if can't afford
            if (!canAfford) {
                graphics.fill(x, y, x + 16, y + 16, 0x88000000);
            }

            // Highlight + tooltip on hover
            if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
                graphics.fill(x, y, x + 16, y + 16, 0x44FFFFFF);
                List<Component> tooltip = buildTooltip(player, drill, option, section, canAfford);
                graphics.renderComponentTooltip(font, tooltip, mouseX, mouseY);
            }
        }
    }

    // =========================
    // TOOLTIP
    // =========================

    private List<Component> buildTooltip(net.minecraft.client.player.LocalPlayer player,
                                         ItemStack drill, ItemStack option,
                                         int section, boolean canAfford) {
        List<Component> lines = new ArrayList<>();
        lines.add(option.getHoverName().copy());

        switch (section) {
            case 0 -> {
                if (option.getItem() == com.starstag.skyforgemining.registry.ModItems.MASTERY_CAPSULE.get()) {
                    lines.add(Component.literal("§7Cost: §f1x Mastery Capsule"));
                    lines.add(Component.literal("§7Effect: §a+1000 Drill XP"));
                } else if (option.getItem() instanceof DrillItem sourceDrill) {
                    int xp = sourceDrill.getTotalXp(option);
                    lines.add(Component.literal("§7Transfers: §a" + xp + " XP"));
                    lines.add(Component.literal("§7Source drill will be reset"));
                }
            }

            case 1 -> {
                if (option.getItem() instanceof GemstoneItem gem) {
                    lines.add(Component.literal("§7Cost: §f1x " + option.getHoverName().getString()));
                    lines.add(Component.literal("§7Effect: §a+" + gem.getBoost()
                            + " " + gem.getGemstoneType().getStatKey().replace("_", " ")));

                    if (drill.getItem() instanceof DrillItem drillItem) {
                        int used = drillItem.getUsedSockets(drill);
                        int max = drillItem.getMaxSockets(drill);
                        lines.add(Component.literal("§7Sockets: §f" + used + "/" + max));

                        if (used > 0) {
                            StringBuilder gemLine = new StringBuilder("§7Socketed: ");
                            CompoundTag tag = drill.getOrCreateTag();
                            for (int i = 0; i < used; i++) {
                                String gemStr = tag.getString("Gem_" + i);
                                if (!gemStr.isEmpty()) {
                                    String typeName = gemStr.split("_")[0];
                                    gemLine.append(getGemEmoji(typeName)).append(" ");
                                }
                            }
                            lines.add(Component.literal(gemLine.toString().trim()));
                        }

                        if (used >= max) {
                            lines.add(Component.literal("§cNo sockets remaining!"));
                        }
                    }
                }
            }

            case 2 -> {
                if (option.getItem() instanceof DrillPartItem part) {
                    lines.add(Component.literal("§7Cost: §f1x " + option.getHoverName().getString()));
                    lines.add(Component.literal("§7Effect: §a+"
                            + part.getPartType().getStat1Value() + " "
                            + part.getPartType().getStat1Key()));
                    if (part.getPartType().hasStat2()) {
                        lines.add(Component.literal("§a+"
                                + part.getPartType().getStat2Value() + " "
                                + part.getPartType().getStat2Key()));
                    }

                    if (drill.getItem() instanceof DrillItem) {
                        String key = "Part_" + part.getPartType().name();
                        if (drill.getOrCreateTag().contains(key)) {
                            lines.add(Component.literal("§eReplaces existing part"));
                            lines.add(Component.literal("§7Previous part will be returned"));
                        } else {
                            lines.add(Component.literal("§7Slot is §aempty"));
                        }
                    }

                    lines.add(Component.literal(" "));
                    lines.add(Component.literal("§7Installed parts:"));
                    boolean anyParts = false;
                    for (DrillPartType partType : DrillPartType.values()) {
                        if (drill.getOrCreateTag().contains("Part_" + partType.name())) {
                            anyParts = true;
                            lines.add(Component.literal("§a✔ §f" + partType.getDisplayName()));
                        }
                    }
                    if (!anyParts) {
                        lines.add(Component.literal("§8None"));
                    }
                }
            }
        }

        if (!canAfford) {
            lines.add(Component.literal("§cYou don't have this item"));
        }

        return lines;
    }

    // =========================
    // HELPERS
    // =========================

    private String getGemEmoji(String typeName) {
        return switch (typeName.toUpperCase()) {
            case "RUBY"     -> "§c◆";
            case "SAPPHIRE" -> "§9◆";
            case "EMERALD"  -> "§a◆";
            case "AMETHYST" -> "§5◆";
            default         -> "§7◆";
        };
    }

    // =========================
    // CLICK HANDLING
    // =========================

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);

        net.minecraft.client.player.LocalPlayer player = this.minecraft.player;
        if (player == null) return super.mouseClicked(mouseX, mouseY, button);

        ItemStack drill = player.getMainHandItem();
        if (!(drill.getItem() instanceof DrillItem)) return super.mouseClicked(mouseX, mouseY, button);

        if (checkSectionClick(player, (int) mouseX, (int) mouseY,
                DrillStationLogic.getMasteryOptions(player), MASTERY_Y, 0)) return true;
        if (checkSectionClick(player, (int) mouseX, (int) mouseY,
                DrillStationLogic.getGemOptions(), GEMS_Y, 1)) return true;
        if (checkSectionClick(player, (int) mouseX, (int) mouseY,
                DrillStationLogic.getPartOptions(), PARTS_Y, 2)) return true;

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean checkSectionClick(net.minecraft.client.player.LocalPlayer player,
                                      int mouseX, int mouseY,
                                      List<ItemStack> options, int sectionY, int section) {
        for (int i = 0; i < options.size(); i++) {
            int col = i % 9;
            int row = i / 9;
            int x = leftPos + ITEMS_X + col * SLOT_SIZE;
            int y = topPos + sectionY + row * SLOT_SIZE;

            if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
                ItemStack option = options.get(i);
                if (DrillStationLogic.hasItem(player, option)) {
                    net.minecraft.client.Minecraft.getInstance()
                            .gameMode.handleInventoryButtonClick(
                                    menu.containerId, section * 100 + i);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        // Suppress default title
    }
}