package net.evolvingreaims.evolvingrealms.registry;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.item.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

/**
 * Central item registry for Evolving Realms.
 */
public final class ModItems {

    // -------------------------------------------------------------------------
    // Raw Materials
    // -------------------------------------------------------------------------
    public static final Item RAW_SULFUR     = register("raw_sulfur",     new Item(new FabricItemSettings()));
    public static final Item SULFUR_CRYSTAL = register("sulfur_crystal_item", new Item(new FabricItemSettings()));
    public static final Item RAW_PYRITE     = register("raw_pyrite",     new Item(new FabricItemSettings()));
    public static final Item PYRITE_INGOT   = register("pyrite_ingot",   new Item(new FabricItemSettings()));
    public static final Item RAW_VOLCANIUM  = register("raw_volcanium",  new Item(new FabricItemSettings()));
    public static final Item VOLCANIUM_INGOT = register("volcanium_ingot", new Item(new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item ACID_FLASK     = register("acid_flask",     new AcidFlaskItem(new FabricItemSettings().maxCount(16)));
    public static final Item LIQUID_SULFUR_BUCKET = register("liquid_sulfur_bucket", new LiquidSulfurBucketItem(new FabricItemSettings().maxCount(1)));
    public static final Item ACID_BUCKET    = register("acid_bucket",    new AcidBucketItem(new FabricItemSettings().maxCount(1)));
    public static final Item SULFUR_DUST    = register("sulfur_dust",    new Item(new FabricItemSettings()));
    public static final Item PYRITE_DUST    = register("pyrite_dust",    new Item(new FabricItemSettings()));
    public static final Item CRYSTAL_SHARD  = register("crystal_shard",  new Item(new FabricItemSettings()));
    public static final Item ASH            = register("ash",            new Item(new FabricItemSettings()));
    public static final Item TITAN_SCALE    = register("titan_scale",    new Item(new FabricItemSettings().rarity(Rarity.EPIC)));
    public static final Item TITAN_CORE     = register("titan_core",     new TitanCoreItem(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item SULFUR_PHANTOM_WING = register("sulfur_phantom_wing", new Item(new FabricItemSettings()));
    public static final Item CRYSTAL_GOLEM_HEART = register("crystal_golem_heart", new Item(new FabricItemSettings().rarity(Rarity.RARE)));

    // -------------------------------------------------------------------------
    // Pyrite Tools
    // -------------------------------------------------------------------------
    public static final Item PYRITE_SWORD   = register("pyrite_sword",   new SwordItem(ModToolMaterials.PYRITE,   new FabricItemSettings().attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.PYRITE, 3, -2.4f))));
    public static final Item PYRITE_PICKAXE = register("pyrite_pickaxe", new PickaxeItem(ModToolMaterials.PYRITE, new FabricItemSettings().attributeModifiers(PickaxeItem.createAttributeModifiers(ModToolMaterials.PYRITE, 1, -2.8f))));
    public static final Item PYRITE_AXE     = register("pyrite_axe",     new AxeItem(ModToolMaterials.PYRITE,     new FabricItemSettings().attributeModifiers(AxeItem.createAttributeModifiers(ModToolMaterials.PYRITE, 6, -3.1f))));
    public static final Item PYRITE_SHOVEL  = register("pyrite_shovel",  new ShovelItem(ModToolMaterials.PYRITE,  new FabricItemSettings().attributeModifiers(ShovelItem.createAttributeModifiers(ModToolMaterials.PYRITE, 1.5f, -3.0f))));
    public static final Item PYRITE_HOE     = register("pyrite_hoe",     new HoeItem(ModToolMaterials.PYRITE,     new FabricItemSettings().attributeModifiers(HoeItem.createAttributeModifiers(ModToolMaterials.PYRITE, -3, 0))));

    // -------------------------------------------------------------------------
    // Pyrite Armor
    // -------------------------------------------------------------------------
    public static final Item PYRITE_HELMET     = register("pyrite_helmet",     new ArmorItem(ModArmorMaterials.PYRITE, ArmorItem.Type.HELMET,     new FabricItemSettings()));
    public static final Item PYRITE_CHESTPLATE = register("pyrite_chestplate", new ArmorItem(ModArmorMaterials.PYRITE, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item PYRITE_LEGGINGS   = register("pyrite_leggings",   new ArmorItem(ModArmorMaterials.PYRITE, ArmorItem.Type.LEGGINGS,   new FabricItemSettings()));
    public static final Item PYRITE_BOOTS      = register("pyrite_boots",      new ArmorItem(ModArmorMaterials.PYRITE, ArmorItem.Type.BOOTS,      new FabricItemSettings()));

    // -------------------------------------------------------------------------
    // Volcanium Tools
    // -------------------------------------------------------------------------
    public static final Item VOLCANIUM_SWORD   = register("volcanium_sword",   new VolcaniumSwordItem(ModToolMaterials.VOLCANIUM, new FabricItemSettings().rarity(Rarity.RARE).attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.VOLCANIUM, 5, -2.2f))));
    public static final Item VOLCANIUM_PICKAXE = register("volcanium_pickaxe", new VolcaniumPickaxeItem(ModToolMaterials.VOLCANIUM, new FabricItemSettings().rarity(Rarity.RARE).attributeModifiers(PickaxeItem.createAttributeModifiers(ModToolMaterials.VOLCANIUM, 3, -2.6f))));
    public static final Item VOLCANIUM_AXE     = register("volcanium_axe",     new VolcaniumAxeItem(ModToolMaterials.VOLCANIUM,    new FabricItemSettings().rarity(Rarity.RARE).attributeModifiers(AxeItem.createAttributeModifiers(ModToolMaterials.VOLCANIUM, 8, -2.9f))));
    public static final Item VOLCANIUM_SHOVEL  = register("volcanium_shovel",  new VolcaniumShovelItem(ModToolMaterials.VOLCANIUM,  new FabricItemSettings().rarity(Rarity.RARE).attributeModifiers(ShovelItem.createAttributeModifiers(ModToolMaterials.VOLCANIUM, 2, -2.8f))));
    public static final Item VOLCANIUM_HOE     = register("volcanium_hoe",     new HoeItem(ModToolMaterials.VOLCANIUM,              new FabricItemSettings().rarity(Rarity.RARE).attributeModifiers(HoeItem.createAttributeModifiers(ModToolMaterials.VOLCANIUM, -5, 0))));

    // -------------------------------------------------------------------------
    // Volcanium Armor
    // -------------------------------------------------------------------------
    public static final Item VOLCANIUM_HELMET     = register("volcanium_helmet",     new VolcaniumArmorItem(ModArmorMaterials.VOLCANIUM, ArmorItem.Type.HELMET,     new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item VOLCANIUM_CHESTPLATE = register("volcanium_chestplate", new VolcaniumArmorItem(ModArmorMaterials.VOLCANIUM, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item VOLCANIUM_LEGGINGS   = register("volcanium_leggings",   new VolcaniumArmorItem(ModArmorMaterials.VOLCANIUM, ArmorItem.Type.LEGGINGS,   new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item VOLCANIUM_BOOTS      = register("volcanium_boots",      new VolcaniumArmorItem(ModArmorMaterials.VOLCANIUM, ArmorItem.Type.BOOTS,      new FabricItemSettings().rarity(Rarity.RARE)));

    // -------------------------------------------------------------------------
    // Sulfur Dimension Spawn Item
    // -------------------------------------------------------------------------
    public static final Item SULFUR_COMPASS = register("sulfur_compass",
            new SulfurCompassItem(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item DIMENSION_KEY  = register("dimension_key",
            new DimensionKeyItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE)));

    // -------------------------------------------------------------------------
    // Boss Summoning Items
    // -------------------------------------------------------------------------
    public static final Item TITAN_SUMMONING_IDOL = register("titan_summoning_idol",
            new TitanSummoningIdolItem(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC)));

    // -------------------------------------------------------------------------
    // Foods
    // -------------------------------------------------------------------------
    public static final Item SULFUR_MUSHROOM_STEW = register("sulfur_mushroom_stew",
            new Item(new FabricItemSettings().maxCount(1).food(ModFoods.SULFUR_MUSHROOM_STEW)));
    public static final Item CRYSTAL_CANDY = register("crystal_candy",
            new Item(new FabricItemSettings().food(ModFoods.CRYSTAL_CANDY)));

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------
    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(EvolvingRealms.MOD_ID, name), item);
    }

    public static void register() {
        EvolvingRealms.LOGGER.debug("Registering items for Evolving Realms");
    }
}
