package com.starstag.skyforgemining.registry;

import com.starstag.skyforgemining.SkyforgeMining;
import com.starstag.skyforgemining.item.DrillItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SkyforgeMining.MODID);

    public static final RegistryObject<Item> BASIC_DRILL =
            ITEMS.register("basic_drill",
                    () -> new DrillItem(
                            Tiers.IRON,
                            10,   // base mining speed
                            20,   // base mining fortune
                            new Item.Properties().stacksTo(1)
                    ));
    public static final RegistryObject<Item> ROUGH_RUBY =
            ITEMS.register("rough_ruby",
                    () -> new Item(new Item.Properties()));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}