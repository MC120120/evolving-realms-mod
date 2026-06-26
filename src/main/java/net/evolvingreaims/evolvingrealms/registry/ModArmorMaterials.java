package net.evolvingreaims.evolvingrealms.registry;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.EnumMap;
import java.util.List;

public final class ModArmorMaterials {

    public static final RegistryEntry<ArmorMaterial> PYRITE = register("pyrite",
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS,      3);
                map.put(ArmorItem.Type.LEGGINGS,   6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET,     3);
                map.put(ArmorItem.Type.BODY,       11);
            }),
            16,
            SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            0.5f, 0.1f,
            () -> Ingredient.ofItems(ModItems.PYRITE_INGOT));

    public static final RegistryEntry<ArmorMaterial> VOLCANIUM = register("volcanium",
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS,      4);
                map.put(ArmorItem.Type.LEGGINGS,   8);
                map.put(ArmorItem.Type.CHESTPLATE, 11);
                map.put(ArmorItem.Type.HELMET,     4);
                map.put(ArmorItem.Type.BODY,       15);
            }),
            6,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            4.0f, 0.3f,
            () -> Ingredient.ofItems(ModItems.VOLCANIUM_INGOT));

    private static RegistryEntry<ArmorMaterial> register(
            String name,
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantability,
            net.minecraft.sound.SoundEvent equipSound,
            float toughness,
            float knockbackResistance,
            java.util.function.Supplier<Ingredient> repairIngredient) {

        Identifier id = Identifier.of(EvolvingRealms.MOD_ID, name);
        ArmorMaterial material = new ArmorMaterial(
                defense, enchantability,
                Registries.SOUND_EVENT.getEntry(equipSound).orElseThrow(),
                repairIngredient,
                List.of(new ArmorMaterial.Layer(id)),
                toughness, knockbackResistance);
        return Registry.registerReference(Registries.ARMOR_MATERIAL, id, material);
    }

    private ModArmorMaterials() {}
}
