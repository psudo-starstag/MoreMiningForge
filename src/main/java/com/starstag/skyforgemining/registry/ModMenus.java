package com.starstag.skyforgemining.registry;

import com.starstag.skyforgemining.SkyforgeMining;
import com.starstag.skyforgemining.menu.DrillStationMenu;
import com.starstag.skyforgemining.menu.MiningSackMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, SkyforgeMining.MODID);

    public static final RegistryObject<MenuType<DrillStationMenu>> DRILL_STATION_MENU =
            MENUS.register("drill_station_menu",
                    () -> IForgeMenuType.create((id, inv, data) ->
                            new DrillStationMenu(id, inv)));

    public static final RegistryObject<MenuType<MiningSackMenu>> MINING_SACK_MENU =
            MENUS.register("mining_sack",
                    () -> IForgeMenuType.create(MiningSackMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}