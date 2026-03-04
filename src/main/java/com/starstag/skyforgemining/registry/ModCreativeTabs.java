package com.starstag.skyforgemining.registry;

import com.starstag.skyforgemining.SkyforgeMining;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SkyforgeMining.MODID);

    public static final RegistryObject<CreativeModeTab> SKYFORGE_TAB =
            CREATIVE_TABS.register("skyforge_tab", () ->
                    CreativeModeTab.builder()
                            .icon(() -> new ItemStack(ModItems.BASIC_DRILL.get()))
                            .title(Component.translatable("creativetab.skyforgemining.skyforge_tab"))
                            .displayItems((parameters, output) -> {

                                // Drills
                                output.accept(ModItems.BASIC_DRILL.get());
                                output.accept(ModItems.DIVANS_DRILL.get());

                                // Consumables
                                output.accept(ModItems.MASTERY_CAPSULE.get());

                                // Sacks
                                output.accept(ModItems.BASIC_SACK.get());
                                output.accept(ModItems.REINFORCED_SACK.get());
                                output.accept(ModItems.ENCHANTED_SACK.get());
                                output.accept(ModItems.VOID_SACK.get());

                                // Gemstones — Ruby
                                output.accept(ModItems.ROUGH_RUBY.get());
                                output.accept(ModItems.FLAWED_RUBY.get());
                                output.accept(ModItems.FINE_RUBY.get());
                                output.accept(ModItems.FLAWLESS_RUBY.get());
                                output.accept(ModItems.PERFECT_RUBY.get());

                                // Gemstones — Sapphire
                                output.accept(ModItems.ROUGH_SAPPHIRE.get());
                                output.accept(ModItems.FLAWED_SAPPHIRE.get());
                                output.accept(ModItems.FINE_SAPPHIRE.get());
                                output.accept(ModItems.FLAWLESS_SAPPHIRE.get());
                                output.accept(ModItems.PERFECT_SAPPHIRE.get());

                                // Gemstones — Emerald
                                output.accept(ModItems.ROUGH_EMERALD.get());
                                output.accept(ModItems.FLAWED_EMERALD.get());
                                output.accept(ModItems.FINE_EMERALD.get());
                                output.accept(ModItems.FLAWLESS_EMERALD.get());
                                output.accept(ModItems.PERFECT_EMERALD.get());

                                // Gemstones — Amethyst
                                output.accept(ModItems.ROUGH_AMETHYST.get());
                                output.accept(ModItems.FLAWED_AMETHYST.get());
                                output.accept(ModItems.FINE_AMETHYST.get());
                                output.accept(ModItems.FLAWLESS_AMETHYST.get());
                                output.accept(ModItems.PERFECT_AMETHYST.get());

                                // Drill Parts
                                output.accept(ModItems.DRILL_ENGINE.get());
                                output.accept(ModItems.DRILL_PLATE.get());
                                output.accept(ModItems.DRILL_MOTOR.get());
                                output.accept(ModItems.FUEL_TANK.get());

                                // Blocks
                                output.accept(ModBlocks.DRILL_STATION_ITEM.get());
                            })
                            .build()
            );

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}