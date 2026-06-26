package net.evolvingreaims.evolvingrealms.util;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

/**
 * All custom data-pack tags used by Evolving Realms.
 */
public final class ModTags {

    // -------------------------------------------------------------------------
    // Block tags
    // -------------------------------------------------------------------------
    public static final class Blocks {
        /** Blocks that generate in the Sulfur Dimension. */
        public static final TagKey<Block> SULFUR_DIMENSION_BLOCKS =
                TagKey.of(RegistryKeys.BLOCK, id("sulfur_dimension_blocks"));

        /** Blocks that are immune to Wildfire spread. */
        public static final TagKey<Block> WILDFIRE_IMMUNE =
                TagKey.of(RegistryKeys.BLOCK, id("wildfire_immune"));

        /** Blocks that can be melted during a volcano eruption. */
        public static final TagKey<Block> VOLCANO_MELTABLE =
                TagKey.of(RegistryKeys.BLOCK, id("volcano_meltable"));

        /** Sulfur-family ores for pickaxe harvesting. */
        public static final TagKey<Block> SULFUR_ORES =
                TagKey.of(RegistryKeys.BLOCK, id("sulfur_ores"));

        private Blocks() {}
    }

    // -------------------------------------------------------------------------
    // Item tags
    // -------------------------------------------------------------------------
    public static final class Items {
        /** All Volcanium-tier tools and armour. */
        public static final TagKey<Item> VOLCANIUM_EQUIPMENT =
                TagKey.of(RegistryKeys.ITEM, id("volcanium_equipment"));

        /** Raw materials that can be smelted in a Pyrite Forge. */
        public static final TagKey<Item> PYRITE_FORGE_INPUTS =
                TagKey.of(RegistryKeys.ITEM, id("pyrite_forge_inputs"));

        /** Items dropped by Sulfur Dimension mobs. */
        public static final TagKey<Item> SULFUR_MOB_DROPS =
                TagKey.of(RegistryKeys.ITEM, id("sulfur_mob_drops"));

        private Items() {}
    }

    private static Identifier id(String path) {
        return Identifier.of(EvolvingRealms.MOD_ID, path);
    }

    private ModTags() {}
}
