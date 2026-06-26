package net.evolvingreaims.evolvingrealms.worldgen.biome;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.evolvingreaims.evolvingrealms.registry.ModBiomes;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

import java.util.List;
import java.util.stream.Stream;

/**
 * Biome source for the Sulfur Dimension.
 * Uses a simple grid pattern based on the block position to assign
 * one of six sulfur biomes. No noise sampling needed — the biomes are
 * large distinct zones delineated by coordinate bands.
 */
public class SulfurDimensionBiomeSource extends BiomeSource {

    public static final MapCodec<SulfurDimensionBiomeSource> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.stable(new SulfurDimensionBiomeSource()));

    // Zone thresholds (x + z hash bands in 2048-block zones)
    private static final int ZONE_SIZE = 2048;

    private final List<RegistryKey<Biome>> biomeKeys = List.of(
            ModBiomes.SULFUR_FLATS,
            ModBiomes.VOLCANIC_PEAKS,
            ModBiomes.ASH_WASTES,
            ModBiomes.ACID_SWAMP,
            ModBiomes.CRYSTAL_CAVES,
            ModBiomes.TOXIC_JUNGLE
    );

    public SulfurDimensionBiomeSource() {}

    @Override
    protected MapCodec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

    @Override
    protected Stream<RegistryEntry<Biome>> biomeStream() {
        return Stream.empty(); // entries resolved at runtime via registry
    }

    @Override
    public RegistryEntry<Biome> getBiome(int x, int y, int z, MultiNoiseUtil.MultiNoiseSampler noise) {
        // Simple deterministic zone assignment
        int zoneX = Math.floorDiv(x, ZONE_SIZE >> 2);
        int zoneZ = Math.floorDiv(z, ZONE_SIZE >> 2);
        int hash = Math.abs((zoneX * 31 + zoneZ) % biomeKeys.size());
        return noise.getBiome(biomeKeys.get(hash));
    }
}
