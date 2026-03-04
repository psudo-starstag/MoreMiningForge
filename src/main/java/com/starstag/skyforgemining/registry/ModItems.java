package com.starstag.skyforgemining.registry;

import com.starstag.skyforgemining.SkyforgeMining;
import com.starstag.skyforgemining.item.DrillItem;
import com.starstag.skyforgemining.item.MasteryCapsuleItem;
import com.starstag.skyforgemining.item.drillpart.DrillPartItem;
import com.starstag.skyforgemining.item.drillpart.DrillPartType;
import com.starstag.skyforgemining.item.gemstone.GemstoneItem;
import com.starstag.skyforgemining.item.gemstone.GemstoneTier;
import com.starstag.skyforgemining.item.gemstone.GemstoneType;
import com.starstag.skyforgemining.item.sack.SackItem;
import com.starstag.skyforgemining.item.sack.SackTier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
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
                            Tiers.IRON, 1, -2.8f,
                            new Item.Properties().stacksTo(1).rarity(Rarity.RARE),
                            10, 20
                    ));

    public static final RegistryObject<Item> DIVANS_DRILL =
            ITEMS.register("divans_drill",
                    () -> new DrillItem(
                            Tiers.NETHERITE, 1, -2.8f,
                            new Item.Properties().stacksTo(1).rarity(Rarity.EPIC),
                            150, 200
                    ) {
                        @Override
                        public int getMaxSockets(ItemStack stack) { return 5; }
                    });

    /*
     * =========================
     * CONSUMABLES
     * =========================
     */
    public static final RegistryObject<Item> MASTERY_CAPSULE =
            ITEMS.register("mastery_capsule",
                    () -> new MasteryCapsuleItem());

    /*
     * =========================
     * SACKS
     * =========================
     */
    public static final RegistryObject<Item> BASIC_SACK =
            ITEMS.register("basic_sack",
                    () -> new SackItem(SackTier.BASIC, new Item.Properties()));

    public static final RegistryObject<Item> REINFORCED_SACK =
            ITEMS.register("reinforced_sack",
                    () -> new SackItem(SackTier.REINFORCED, new Item.Properties()));

    public static final RegistryObject<Item> ENCHANTED_SACK =
            ITEMS.register("enchanted_sack",
                    () -> new SackItem(SackTier.ENCHANTED,
                            new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> VOID_SACK =
            ITEMS.register("void_sack",
                    () -> new SackItem(SackTier.VOID,
                            new Item.Properties().rarity(Rarity.RARE)));

    /*
     * =========================
     * GEMSTONES — RUBY
     * =========================
     */
    public static final RegistryObject<Item> ROUGH_RUBY =
            ITEMS.register("rough_ruby",
                    () -> new GemstoneItem(GemstoneType.RUBY, GemstoneTier.ROUGH, new Item.Properties()));
    public static final RegistryObject<Item> FLAWED_RUBY =
            ITEMS.register("flawed_ruby",
                    () -> new GemstoneItem(GemstoneType.RUBY, GemstoneTier.FLAWED, new Item.Properties()));
    public static final RegistryObject<Item> FINE_RUBY =
            ITEMS.register("fine_ruby",
                    () -> new GemstoneItem(GemstoneType.RUBY, GemstoneTier.FINE,
                            new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> FLAWLESS_RUBY =
            ITEMS.register("flawless_ruby",
                    () -> new GemstoneItem(GemstoneType.RUBY, GemstoneTier.FLAWLESS,
                            new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> PERFECT_RUBY =
            ITEMS.register("perfect_ruby",
                    () -> new GemstoneItem(GemstoneType.RUBY, GemstoneTier.PERFECT,
                            new Item.Properties().rarity(Rarity.EPIC)));

    /*
     * =========================
     * GEMSTONES — SAPPHIRE
     * =========================
     */
    public static final RegistryObject<Item> ROUGH_SAPPHIRE =
            ITEMS.register("rough_sapphire",
                    () -> new GemstoneItem(GemstoneType.SAPPHIRE, GemstoneTier.ROUGH, new Item.Properties()));
    public static final RegistryObject<Item> FLAWED_SAPPHIRE =
            ITEMS.register("flawed_sapphire",
                    () -> new GemstoneItem(GemstoneType.SAPPHIRE, GemstoneTier.FLAWED, new Item.Properties()));
    public static final RegistryObject<Item> FINE_SAPPHIRE =
            ITEMS.register("fine_sapphire",
                    () -> new GemstoneItem(GemstoneType.SAPPHIRE, GemstoneTier.FINE,
                            new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> FLAWLESS_SAPPHIRE =
            ITEMS.register("flawless_sapphire",
                    () -> new GemstoneItem(GemstoneType.SAPPHIRE, GemstoneTier.FLAWLESS,
                            new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> PERFECT_SAPPHIRE =
            ITEMS.register("perfect_sapphire",
                    () -> new GemstoneItem(GemstoneType.SAPPHIRE, GemstoneTier.PERFECT,
                            new Item.Properties().rarity(Rarity.EPIC)));

    /*
     * =========================
     * GEMSTONES — EMERALD
     * =========================
     */
    public static final RegistryObject<Item> ROUGH_EMERALD =
            ITEMS.register("rough_emerald",
                    () -> new GemstoneItem(GemstoneType.EMERALD, GemstoneTier.ROUGH, new Item.Properties()));
    public static final RegistryObject<Item> FLAWED_EMERALD =
            ITEMS.register("flawed_emerald",
                    () -> new GemstoneItem(GemstoneType.EMERALD, GemstoneTier.FLAWED, new Item.Properties()));
    public static final RegistryObject<Item> FINE_EMERALD =
            ITEMS.register("fine_emerald",
                    () -> new GemstoneItem(GemstoneType.EMERALD, GemstoneTier.FINE,
                            new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> FLAWLESS_EMERALD =
            ITEMS.register("flawless_emerald",
                    () -> new GemstoneItem(GemstoneType.EMERALD, GemstoneTier.FLAWLESS,
                            new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> PERFECT_EMERALD =
            ITEMS.register("perfect_emerald",
                    () -> new GemstoneItem(GemstoneType.EMERALD, GemstoneTier.PERFECT,
                            new Item.Properties().rarity(Rarity.EPIC)));

    /*
     * =========================
     * GEMSTONES — AMETHYST
     * =========================
     */
    public static final RegistryObject<Item> ROUGH_AMETHYST =
            ITEMS.register("rough_amethyst",
                    () -> new GemstoneItem(GemstoneType.AMETHYST, GemstoneTier.ROUGH, new Item.Properties()));
    public static final RegistryObject<Item> FLAWED_AMETHYST =
            ITEMS.register("flawed_amethyst",
                    () -> new GemstoneItem(GemstoneType.AMETHYST, GemstoneTier.FLAWED, new Item.Properties()));
    public static final RegistryObject<Item> FINE_AMETHYST =
            ITEMS.register("fine_amethyst",
                    () -> new GemstoneItem(GemstoneType.AMETHYST, GemstoneTier.FINE,
                            new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> FLAWLESS_AMETHYST =
            ITEMS.register("flawless_amethyst",
                    () -> new GemstoneItem(GemstoneType.AMETHYST, GemstoneTier.FLAWLESS,
                            new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> PERFECT_AMETHYST =
            ITEMS.register("perfect_amethyst",
                    () -> new GemstoneItem(GemstoneType.AMETHYST, GemstoneTier.PERFECT,
                            new Item.Properties().rarity(Rarity.EPIC)));

    /*
     * =========================
     * DRILL PARTS
     * =========================
     */
    public static final RegistryObject<Item> DRILL_ENGINE =
            ITEMS.register("drill_engine",
                    () -> new DrillPartItem(DrillPartType.ENGINE, new Item.Properties()));

    public static final RegistryObject<Item> DRILL_PLATE =
            ITEMS.register("drill_plate",
                    () -> new DrillPartItem(DrillPartType.PLATE, new Item.Properties()));

    public static final RegistryObject<Item> DRILL_MOTOR =
            ITEMS.register("drill_motor",
                    () -> new DrillPartItem(DrillPartType.MOTOR, new Item.Properties()));

    public static final RegistryObject<Item> FUEL_TANK =
            ITEMS.register("fuel_tank",
                    () -> new DrillPartItem(DrillPartType.FUEL_TANK, new Item.Properties()));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}