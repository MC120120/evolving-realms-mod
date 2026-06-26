package net.evolvingreaims.evolvingrealms.registry;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

/**
 * World generation additions for Evolving Realms.
 *
 * Placed feature JSON lives in data/evolving_realms/worldgen/placed_feature/.
 * This class adds those features to existing vanilla biomes at init time.
 */
public final class ModWorldGen {

    // Keys for placed features added to overworld biomes
    private static final RegistryKey<PlacedFeature> SULFUR_ORE_OVERWORLD =
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(EvolvingRealms.MOD_ID, "sulfur_ore_overworld"));
    private static final RegistryKey<PlacedFeature> PYRITE_ORE_OVERWORLD =
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(EvolvingRealms.MOD_ID, "pyrite_ore_overworld"));
    private static final RegistryKey<PlacedFeature> VOLCANIUM_ORE_OVERWORLD =
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(EvolvingRealms.MOD_ID, "volcanium_ore_overworld"));
    private static final RegistryKey<PlacedFeature> CRYSTAL_GEODE =
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(EvolvingRealms.MOD_ID, "crystal_geode"));

    public static void register() {
        // Add sulfur ore to all overworld biomes in the lower layers
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                SULFUR_ORE_OVERWORLD);

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                PYRITE_ORE_OVERWORLD);

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_DECORATION,
                VOLCANIUM_ORE_OVERWORLD);

        // Crystal geodes in underground caves
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.LOCAL_MODIFICATIONS,
                CRYSTAL_GEODE);

        EvolvingRealms.LOGGER.debug("ModWorldGen registered features for overworld biomes.");
    }
}
