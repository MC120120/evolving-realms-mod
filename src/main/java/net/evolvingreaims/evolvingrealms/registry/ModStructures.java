package net.evolvingreaims.evolvingrealms.registry;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;

/**
 * Structure registry keys for Evolving Realms.
 * Structure data lives in data/evolving_realms/worldgen/structure/.
 */
public final class ModStructures {

    public static final RegistryKey<Structure> SULFUR_DUNGEON =
            RegistryKey.of(RegistryKeys.STRUCTURE, Identifier.of(EvolvingRealms.MOD_ID, "sulfur_dungeon"));

    public static final RegistryKey<Structure> CRYSTAL_TOWER =
            RegistryKey.of(RegistryKeys.STRUCTURE, Identifier.of(EvolvingRealms.MOD_ID, "crystal_tower"));

    public static final RegistryKey<Structure> TITAN_ARENA =
            RegistryKey.of(RegistryKeys.STRUCTURE, Identifier.of(EvolvingRealms.MOD_ID, "titan_arena"));

    public static final RegistryKey<Structure> CIVILIZATION_RUINS =
            RegistryKey.of(RegistryKeys.STRUCTURE, Identifier.of(EvolvingRealms.MOD_ID, "civilization_ruins"));

    public static void register() {
        EvolvingRealms.LOGGER.debug("Structure registry keys set for Evolving Realms");
    }
}
