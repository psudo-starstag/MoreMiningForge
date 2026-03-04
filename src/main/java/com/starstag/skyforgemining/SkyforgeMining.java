package com.starstag.skyforgemining;

import com.mojang.logging.LogUtils;
import com.starstag.skyforgemining.registry.*;
import com.starstag.skyforgemining.screen.DrillStationScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(SkyforgeMining.MODID)
public class SkyforgeMining {

    public static final String MODID = "skyforgemining";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SkyforgeMining() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenus.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        modEventBus.addListener(SkyforgeMining::onClientSetup);

        LOGGER.info("Skyforge Mining Loaded!");
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {

            MenuScreens.register(
                    ModMenus.DRILL_STATION_MENU.get(),
                    DrillStationScreen::new
            );

            MenuScreens.register(
                    ModMenus.MINING_SACK_MENU.get(),
                    MiningSackScreen::new
            );

        });
    }
}