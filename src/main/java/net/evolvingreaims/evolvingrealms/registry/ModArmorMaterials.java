package net.evolvingreaims.evolvingrealms.registry;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.Map;

public final class ModArmorMaterials {

    public static final ArmorMaterial PYRITE = new ArmorMaterial(
            16,
            Map.of(
                EquipmentType.HELMET,     3,
                EquipmentType.CHESTPLATE, 8,
                EquipmentType.LEGGINGS,   6,
                EquipmentType.BOOTS,      3
            ),
            16,
            Registries.SOUND_EVENT.getEntry(SoundEvents.ITEM_ARMOR_EQUIP_IRON),
            0.5f,
            0.1f,
            TagKey.of(RegistryKeys.ITEM, Identifier.of(EvolvingRealms.MOD_ID, "pyrite_ingots")),
            RegistryKey.of(EquipmentAsset.REGISTRY_KEY, Identifier.of(EvolvingRealms.MOD_ID, "pyrite"))
    );

    public static final ArmorMaterial VOLCANIUM = new ArmorMaterial(
            37,
            Map.of(
                EquipmentType.HELMET,     4,
                EquipmentType.CHESTPLATE, 11,
                EquipmentType.LEGGINGS,   8,
                EquipmentType.BOOTS,      4
            ),
            6,
            Registries.SOUND_EVENT.getEntry(SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE),
            4.0f,
            0.3f,
            TagKey.of(RegistryKeys.ITEM, Identifier.of(EvolvingRealms.MOD_ID, "volcanium_ingots")),
            RegistryKey.of(EquipmentAsset.REGISTRY_KEY, Identifier.of(EvolvingRealms.MOD_ID, "volcanium"))
    );

    private ModArmorMaterials() {}
}
