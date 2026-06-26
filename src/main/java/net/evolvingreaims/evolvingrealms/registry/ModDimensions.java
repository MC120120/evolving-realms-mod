package net.evolvingreaims.evolvingrealms.registry;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

/**
 * Dimension registry keys for the Sulfur Dimension.
 * The actual dimension registration is done via JSON data files:
 *   data/evolving_realms/dimension/sulfur_dimension.json
 *   data/evolving_realms/dimension_type/sulfur_dimension.json
 */
public final class ModDimensions {

    public static final RegistryKey<World> SULFUR_DIMENSION = RegistryKey.of(
            RegistryKeys.WORLD,
            Identifier.of(EvolvingRealms.MOD_ID, "sulfur_dimension"));

    public static final RegistryKey<DimensionType> SULFUR_DIMENSION_TYPE = RegistryKey.of(
            RegistryKeys.DIMENSION_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "sulfur_dimension"));

    public static void register() {
        EvolvingRealms.LOGGER.debug("Dimension registry keys set for Evolving Realms");
    }
}
