package com.starstag.skyforgemining.registry;

import com.starstag.skyforgemining.SkyforgeMining;
import com.starstag.skyforgemining.item.DrillItem;
import com.starstag.skyforgemining.item.MasteryCapsuleItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SkyforgeMining.MODID);

    /*
     * =========================
     * DRILLS
     * =========================
     */
    public static final RegistryObject<Item> BASIC_DRILL =
            ITEMS.register("basic_drill",
                    () -> new DrillItem(
                            Tiers.IRON,        // Tool tier
                            1,                 // Attack damage
                            -2.8f,             // Attack speed
                            new Item.Properties()
                                    .stacksTo(1)
                                    .rarity(net.minecraft.world.item.Rarity.RARE),
                            10,                // Base Mining Speed
                            20                 // Base Mining Fortune
                    ));
    public static final RegistryObject<Item> DIVANS_DRILL =
            ITEMS.register("divans_drill",
                    () -> new DrillItem(
                            Tiers.NETHERITE,        // Tool tier
                            1,                 // Attack damage
                            -2.8f,             // Attack speed
                            new Item.Properties()
                                    .stacksTo(1)
                                    .rarity(net.minecraft.world.item.Rarity.EPIC),
                            150,                // Base Mining Speed
                            200                 // Base Mining Fortune
                    ));
    /*
     * =========================
     * ITEMS/CONSUMABLES
     * =========================
     */
    public static final RegistryObject<Item> MASTERY_CAPSULE =
            ITEMS.register("mastery_capsule",
                    () -> new MasteryCapsuleItem());

    // In ModItems.java — alternative approach
    public static final RegistryObject<Item> MASTERY_INFUSER_ITEM =
            ITEMS.register("mastery_infuser",
                    () -> new BlockItem(
                            ModBlocks.MASTERY_INFUSER_BLOCK.get(),
                            new Item.Properties()
                    ));
    /*
     * =========================
     * MATERIALS
     * =========================
     */
    public static final RegistryObject<Item> ROUGH_RUBY =
            ITEMS.register("rough_ruby",
                    () -> new Item(new Item.Properties()));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}