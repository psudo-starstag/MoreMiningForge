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

public class CollectionDetailMenu implements MenuProvider {

    private final CollectionType type;
    private static final int[] TIERS = {100, 250, 500, 1000, 2500, 5000};

    public CollectionDetailMenu(CollectionType type) {
        this.type = type;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("§6" + formatName(type) + " Collection");
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

                if (slotId == 22) {
                    player.openMenu(new CollectionMenu());
                }
            }
        };

        fillBackground(menu);

        if (player instanceof ServerPlayer serverPlayer) {

            serverPlayer.getCapability(CollectionProvider.COLLECTION_CAP).ifPresent(data -> {

                int amount = data.get(type);

                for (int i = 0; i < TIERS.length; i++) {

                    int required = TIERS[i];
                    boolean unlocked = amount >= required;

                    ItemStack reward = new ItemStack(unlocked ? Items.EMERALD : Items.BARRIER);

                    reward.setHoverName(Component.literal(
                            (unlocked ? "§aTier " : "§cTier ") + (i + 1)
                    ));

                    CompoundTag tag = reward.getOrCreateTagElement("display");
                    ListTag lore = new ListTag();

                    lore.add(line("§7Required: §e" + required));
                    lore.add(line(" "));
                    lore.add(line(unlocked ? "§aUnlocked" : "§cLocked"));

                    tag.put("Lore", lore);

                    menu.setItem(10 + i, 0, reward);
                }
            });
        }

        // Back button
        ItemStack back = new ItemStack(Items.ARROW);
        back.setHoverName(Component.literal("§eGo Back"));
        menu.setItem(22, 0, back);

        return menu;
    }

    private void fillBackground(ChestMenu menu) {
        ItemStack filler = new ItemStack(Items.GRAY_STAINED_GLASS_PANE);
        filler.setHoverName(Component.literal(" "));
        for (int i = 0; i < 27; i++) {
            menu.setItem(i, 0, filler.copy());
        }
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

    private StringTag line(String text) {
        return StringTag.valueOf(Component.Serializer.toJson(
                Component.literal(text)
        ));
    }
}