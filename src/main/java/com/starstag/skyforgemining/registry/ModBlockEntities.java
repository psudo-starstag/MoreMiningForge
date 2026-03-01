package com.starstag.skyforgemining.registry;

import com.starstag.skyforgemining.SkyforgeMining;
import com.starstag.skyforgemining.blockentity.MasteryInfuserBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SkyforgeMining.MODID);

    public static final RegistryObject<BlockEntityType<MasteryInfuserBlockEntity>> MASTERY_INFUSER =
            BLOCK_ENTITIES.register("mastery_infuser",
                    () -> BlockEntityType.Builder
                            .of(MasteryInfuserBlockEntity::new,
                                    ModBlocks.MASTERY_INFUSER_BLOCK.get())
                            .build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}