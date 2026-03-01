package com.starstag.skyforgemining.registry;

import com.starstag.skyforgemining.SkyforgeMining;
import com.starstag.skyforgemining.blockentity.MasteryInfuserBlockEntity;
import com.starstag.skyforgemining.menu.MasteryInfuserMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, SkyforgeMining.MODID);

    public static final RegistryObject<MenuType<MasteryInfuserMenu>> MASTERY_INFUSER_MENU =
            MENUS.register("mastery_infuser_menu",
                    () -> IForgeMenuType.create((id, inv, data) -> {

                        // Read block position sent from server
                        var pos = data.readBlockPos();

                        BlockEntity be = inv.player.level().getBlockEntity(pos);

                        if (be instanceof MasteryInfuserBlockEntity infuser) {
                            return new MasteryInfuserMenu(id, inv, infuser);
                        }

                        return null;
                    }));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}