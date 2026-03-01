package com.starstag.skyforgemining.registry;

import com.starstag.skyforgemining.SkyforgeMining;
import com.starstag.skyforgemining.block.MasteryInfuserBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, SkyforgeMining.MODID);

    public static final DeferredRegister<Item> BLOCK_ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SkyforgeMining.MODID);

    /*
     * =========================
     * BLOCKS
     * =========================
     */
    public static final RegistryObject<Block> MASTERY_INFUSER_BLOCK =
            BLOCKS.register("mastery_infuser",
                    () -> new MasteryInfuserBlock(
                            BlockBehaviour.Properties.of()
                                    .strength(3.5f)
                                    .requiresCorrectToolForDrops()
                    ));


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ITEMS.register(eventBus);
    }
}