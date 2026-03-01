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
                                output.accept(ModItems.BASIC_DRILL.get());
                                output.accept(ModItems.DIVANS_DRILL.get());
                                output.accept(ModItems.ROUGH_RUBY.get());
                                output.accept(ModItems.MASTERY_CAPSULE.get());
                                output.accept(ModItems.MASTERY_INFUSER_ITEM.get());
                            })
                            .build()
            );

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}