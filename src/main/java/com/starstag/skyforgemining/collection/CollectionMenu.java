package com.starstag.skyforgemining.collection;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

public class CollectionMenu implements MenuProvider {

    @Override
    public Component getDisplayName() {
        return Component.literal("§6Collections");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, net.minecraft.world.entity.player.Player player) {

        ChestMenu menu = new ChestMenu(
                net.minecraft.world.inventory.MenuType.GENERIC_9x3,
                id,
                inv,
                new SimpleContainer(27),
                3
        ) {

            @Override
            public boolean stillValid(net.minecraft.world.entity.player.Player player) {
                return true;
            }

            @Override
            public void clicked(int slotId, int button,
                                net.minecraft.world.inventory.ClickType typeClick,
                                net.minecraft.world.entity.player.Player player) {

                if (slotId >= 10 && slotId < 10 + CollectionType.values().length) {

                    int index = slotId - 10;
                    CollectionType selected = CollectionType.values()[index];

                    player.openMenu(new CollectionDetailMenu(selected));
                }
            }
        };
        fillBackground(menu);

        if (player instanceof ServerPlayer serverPlayer) {

            serverPlayer.getCapability(CollectionProvider.COLLECTION_CAP).ifPresent(data -> {

                int slot = 10;

                for (CollectionType type : CollectionType.values()) {

                    int amount = data.get(type);
                    int level = getCollectionLevel(amount);
                    int currentReq = getRequiredForLevel(level);
                    int nextReq = getRequiredForLevel(level + 1);

                    double progress = nextReq == 0 ? 1.0 :
                            (double)(amount - currentReq) / (nextReq - currentReq);

                    if (progress < 0) progress = 0;
                    if (progress > 1) progress = 1;

                    ItemStack display = new ItemStack(type.getItem());
                    display.setHoverName(Component.literal("§e" + formatName(type)));

                    CompoundTag tag = display.getOrCreateTagElement("display");
                    ListTag lore = new ListTag();

                    lore.add(line("§7Collected: §a" + format(amount)));
                    lore.add(line("§7Level: §6" + level));
                    lore.add(line(" "));

                    lore.add(line("§7Progress to Level " + (level + 1) + ":"));
                    lore.add(line(getProgressBar(progress)));
                    lore.add(line("§e" + format(amount - currentReq)
                            + " §7/ §e"
                            + format(nextReq - currentReq)));

                    tag.put("Lore", lore);

                    menu.setItem(slot, 0, display);
                    slot++;
                }
            });
        }

        return menu;
    }

    private void fillBackground(ChestMenu menu) {
        ItemStack filler = new ItemStack(Items.GRAY_STAINED_GLASS_PANE);
        filler.setHoverName(Component.literal(" "));

        for (int i = 0; i < 27; i++) {
            menu.setItem(i, 0, filler.copy());
        }
    }

    private String getProgressBar(double progress) {

        int totalBars = 20;
        int filled = (int)(progress * totalBars);

        StringBuilder bar = new StringBuilder("§a");

        for (int i = 0; i < filled; i++) bar.append("|");

        bar.append("§7");

        for (int i = filled; i < totalBars; i++) bar.append("|");

        int percent = (int)(progress * 100);

        bar.append(" §e").append(percent).append("%");

        return bar.toString();
    }

    private int getCollectionLevel(int amount) {
        int level = 0;
        while (amount >= getRequiredForLevel(level + 1)) {
            level++;
        }
        return level;
    }

    private int getRequiredForLevel(int level) {
        return 100 * level * level;
    }

    private String formatName(CollectionType type) {

        String raw = type.name().toLowerCase().replace("_", " ");
        String[] parts = raw.split(" ");

        StringBuilder builder = new StringBuilder();

        for (String part : parts) {
            builder.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1))
                    .append(" ");
        }

        return builder.toString().trim();
    }

    private String format(int number) {
        return String.format("%,d", number);
    }

    private StringTag line(String text) {
        return StringTag.valueOf(Component.Serializer.toJson(
                Component.literal(text)
        ));
    }

    private static int[] TIERS = {100, 250, 500, 1000, 2500, 5000};
}