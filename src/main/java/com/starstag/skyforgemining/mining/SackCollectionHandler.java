package com.starstag.skyforgemining.mining;

import com.starstag.skyforgemining.item.sack.SackItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SackCollectionHandler {

    // Try to put an item into any sack in the player's inventory
    // Returns true if fully absorbed
    public static boolean tryCollect(Player player, ItemStack stack) {
        if (stack.isEmpty()) return false;

        for (ItemStack invStack : player.getInventory().items) {
            if (!(invStack.getItem() instanceof SackItem sack)) continue;

            int remaining = sack.addItem(invStack, stack);
            stack.setCount(remaining);

            if (remaining <= 0) return true;
        }

        return false;
    }
}