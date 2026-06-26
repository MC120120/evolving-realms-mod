package net.evolvingreaims.evolvingrealms.registry;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.item.*;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public final class ModItems {

    public static final Item RAW_SULFUR     = register("raw_sulfur",     new Item(new Item.Settings()));
    public static final Item SULFUR_CRYSTAL = register("sulfur_crystal_item", new Item(new Item.Settings()));
    public static final Item RAW_PYRITE     = register("raw_pyrite",     new Item(new Item.Settings()));
    public static final Item PYRITE_INGOT   = register("pyrite_ingot",   new Item(new Item.Settings()));
    public static final Item RAW_VOLCANIUM  = register("raw_volcanium",  new Item(new Item.Settings()));
    public static final Item VOLCANIUM_INGOT = register("volcanium_ingot", new Item(new Item.Settings().rarity(Rarity.RARE)));
    public static final Item ACID_FLASK     = register("acid_flask",     new AcidFlaskItem(new Item.Settings().maxCount(16)));
    public static final Item LIQUID_SULFUR_BUCKET = register("liquid_sulfur_bucket", new LiquidSulfurBucketItem(new Item.Settings().maxCount(1)));
    public static final Item ACID_BUCKET    = register("acid_bucket",    new AcidBucketItem(new Item.Settings().maxCount(1)));
    public static final Item SULFUR_DUST    = register("sulfur_dust",    new Item(new Item.Settings()));
    public static final Item PYRITE_DUST    = register("pyrite_dust",    new Item(new Item.Settings()));
    public static final Item CRYSTAL_SHARD  = register("crystal_shard",  new Item(new Item.Settings()));
    public static final Item ASH            = register("ash",            new Item(new Item.Settings()));
    public static final Item TITAN_SCALE    = register("titan_scale",    new Item(new Item.Settings().rarity(Rarity.EPIC)));
    public static final Item TITAN_CORE     = register("titan_core",     new TitanCoreItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item SULFUR_PHANTOM_WING = register("sulfur_phantom_wing", new Item(new Item.Settings()));
    public static final Item CRYSTAL_GOLEM_HEART = register("crystal_golem_heart", new Item(new Item.Settings().rarity(Rarity.RARE)));

    public static final Item PYRITE_SWORD   = register("pyrite_sword",   new SwordItem(ModToolMaterials.PYRITE,   new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.PYRITE, 3, -2.4f))));
    public static final Item PYRITE_PICKAXE = register("pyrite_pickaxe", new PickaxeItem(ModToolMaterials.PYRITE, new Item.Settings().attributeModifiers(PickaxeItem.createAttributeModifiers(ModToolMaterials.PYRITE, 1, -2.8f))));
    public static final Item PYRITE_AXE     = register("pyrite_axe",     new AxeItem(ModToolMaterials.PYRITE,     new Item.Settings().attributeModifiers(AxeItem.createAttributeModifiers(ModToolMaterials.PYRITE, 6, -3.1f))));
    public static final Item PYRITE_SHOVEL  = register("pyrite_shovel",  new ShovelItem(ModToolMaterials.PYRITE,  new Item.Settings().attributeModifiers(ShovelItem.createAttributeModifiers(ModToolMaterials.PYRITE, 1.5f, -3.0f))));
    public static final Item PYRITE_HOE     = register("pyrite_hoe",     new HoeItem(ModToolMaterials.PYRITE,     new Item.Settings().attributeModifiers(HoeItem.createAttributeModifiers(ModToolMaterials.PYRITE, -3, 0))));

    public static final Item PYRITE_HELMET     = register("pyrite_helmet",     new ArmorItem(ModArmorMaterials.PYRITE, ArmorItem.Type.HELMET,     new Item.Settings()));
    public static final Item PYRITE_CHESTPLATE = register("pyrite_chestplate", new ArmorItem(ModArmorMaterials.PYRITE, ArmorItem.Type.CHESTPLATE, new Item.Settings()));
    public static final Item PYRITE_LEGGINGS   = register("pyrite_leggings",   new ArmorItem(ModArmorMaterials.PYRITE, ArmorItem.Type.LEGGINGS,   new Item.Settings()));
    public static final Item PYRITE_BOOTS      = register("pyrite_boots",      new ArmorItem(ModArmorMaterials.PYRITE, ArmorItem.Type.BOOTS,      new Item.Settings()));

    public static final Item VOLCANIUM_SWORD   = register("volcanium_sword",   new VolcaniumSwordItem(ModToolMaterials.VOLCANIUM, new Item.Settings().rarity(Rarity.RARE).attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.VOLCANIUM, 5, -2.2f))));
    public static final Item VOLCANIUM_PICKAXE = register("volcanium_pickaxe", new VolcaniumPickaxeItem(ModToolMaterials.VOLCANIUM, new Item.Settings().rarity(Rarity.RARE).attributeModifiers(PickaxeItem.createAttributeModifiers(ModToolMaterials.VOLCANIUM, 3, -2.6f))));
    public static final Item VOLCANIUM_AXE     = register("volcanium_axe",     new VolcaniumAxeItem(ModToolMaterials.VOLCANIUM,    new Item.Settings().rarity(Rarity.RARE).attributeModifiers(AxeItem.createAttributeModifiers(ModToolMaterials.VOLCANIUM, 8, -2.9f))));
    public static final Item VOLCANIUM_SHOVEL  = register("volcanium_shovel",  new VolcaniumShovelItem(ModToolMaterials.VOLCANIUM,  new Item.Settings().rarity(Rarity.RARE).attributeModifiers(ShovelItem.createAttributeModifiers(ModToolMaterials.VOLCANIUM, 2, -2.8f))));
    public static final Item VOLCANIUM_HOE     = register("volcanium_hoe",     new HoeItem(ModToolMaterials.VOLCANIUM,              new Item.Settings().rarity(Rarity.RARE).attributeModifiers(HoeItem.createAttributeModifiers(ModToolMaterials.VOLCANIUM, -5, 0))));

    public static final Item VOLCANIUM_HELMET     = register("volcanium_helmet",     new VolcaniumArmorItem(ModArmorMaterials.VOLCANIUM, ArmorItem.Type.HELMET,     new Item.Settings().rarity(Rarity.RARE)));
    public static final Item VOLCANIUM_CHESTPLATE = register("volcanium_chestplate", new VolcaniumArmorItem(ModArmorMaterials.VOLCANIUM, ArmorItem.Type.CHESTPLATE, new Item.Settings().rarity(Rarity.RARE)));
    public static final Item VOLCANIUM_LEGGINGS   = register("volcanium_leggings",   new VolcaniumArmorItem(ModArmorMaterials.VOLCANIUM, ArmorItem.Type.LEGGINGS,   new Item.Settings().rarity(Rarity.RARE)));
    public static final Item VOLCANIUM_BOOTS      = register("volcanium_boots",      new VolcaniumArmorItem(ModArmorMaterials.VOLCANIUM, ArmorItem.Type.BOOTS,      new Item.Settings().rarity(Rarity.RARE)));

    public static final Item SULFUR_COMPASS = register("sulfur_compass",
            new SulfurCompassItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item DIMENSION_KEY  = register("dimension_key",
            new DimensionKeyItem(new Item.Settings().maxCount(1).rarity(Rarity.RARE)));

    public static final Item TITAN_SUMMONING_IDOL = register("titan_summoning_idol",
            new TitanSummoningIdolItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)));

    public static final Item SULFUR_MUSHROOM_STEW = register("sulfur_mushroom_stew",
            new Item(new Item.Settings().maxCount(1).food(ModFoods.SULFUR_MUSHROOM_STEW)));
    public static final Item CRYSTAL_CANDY = register("crystal_candy",
            new Item(new Item.Settings().food(ModFoods.CRYSTAL_CANDY)));

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(EvolvingRealms.MOD_ID, name), item);
    }

    public static void register() {
        EvolvingRealms.LOGGER.debug("Registering items for Evolving Realms");
    }
}
