package net.evolvingreaims.evolvingrealms.registry;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.*;

/**
 * Biome registry keys for Evolving Realms.
 * Biome JSON data lives in data/evolving_realms/worldgen/biome/.
 * This class only defines the RegistryKey references used in code.
 */
public final class ModBiomes {

    public static final RegistryKey<Biome> SULFUR_FLATS = key("sulfur_flats");
    public static final RegistryKey<Biome> CRYSTAL_CAVES = key("crystal_caves");
    public static final RegistryKey<Biome> ACID_SWAMP = key("acid_swamp");
    public static final RegistryKey<Biome> VOLCANIC_PEAKS = key("volcanic_peaks");
    public static final RegistryKey<Biome> ASH_WASTES = key("ash_wastes");
    public static final RegistryKey<Biome> TOXIC_JUNGLE = key("toxic_jungle");

    private static RegistryKey<Biome> key(String name) {
        return RegistryKey.of(RegistryKeys.BIOME, Identifier.of(EvolvingRealms.MOD_ID, name));
    }

    public static void register() {
        EvolvingRealms.LOGGER.debug("Biome registry keys set for Evolving Realms");
    }
}
