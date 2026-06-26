package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;

/**
 * Sapling generator for crystal trees — uses spruce as placeholder
 * until a custom configured feature is registered via data pack.
 */
public final class CrystalSaplingGenerator {
    public static final SaplingGenerator INSTANCE =
            new SaplingGenerator(RegistryKey.of(
                    net.minecraft.world.gen.feature.ConfiguredFeatures.REGISTRY_KEY,
                    net.minecraft.util.Identifier.of("minecraft", "spruce")));

    private CrystalSaplingGenerator() {}
}
