package net.evolvingreaims.evolvingrealms.registry;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public final class ModToolMaterials {

    public static final ToolMaterial PYRITE = new ToolMaterial(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            800,
            7.5f,
            2.5f,
            16,
            TagKey.of(RegistryKeys.ITEM, Identifier.of(EvolvingRealms.MOD_ID, "pyrite_ingots"))
    );

    public static final ToolMaterial VOLCANIUM = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            2800,
            11.0f,
            5.0f,
            8,
            TagKey.of(RegistryKeys.ITEM, Identifier.of(EvolvingRealms.MOD_ID, "volcanium_ingots"))
    );

    private ModToolMaterials() {}
}
