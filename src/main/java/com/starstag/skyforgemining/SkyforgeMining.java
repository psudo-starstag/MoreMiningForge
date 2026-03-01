package com.starstag.skyforgemining;

import com.mojang.logging.LogUtils;
import com.starstag.skyforgemining.registry.ModItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(SkyforgeMining.MODID)
public class SkyforgeMining {

    public static final String MODID = "skyforgemining";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SkyforgeMining() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);

        LOGGER.info("Skyforge Mining Loaded!");
    }
}