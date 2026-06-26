package net.evolvingreaims.evolvingrealms.worldgen.structure;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.StructureType;

public final class EvolvingRealmsStructureTypes {

    public static final StructureType<DungeonStructure> DUNGEON =
            Registry.register(
                    Registries.STRUCTURE_TYPE,
                    Identifier.of(EvolvingRealms.MOD_ID, "dungeon"),
                    () -> DungeonStructure.CODEC);

    public static void register() {
        EvolvingRealms.LOGGER.debug("Registering structure types for Evolving Realms");
    }
}
