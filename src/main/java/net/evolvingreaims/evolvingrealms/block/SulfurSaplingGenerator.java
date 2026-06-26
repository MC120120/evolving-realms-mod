package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.SaplingGenerator;
import net.minecraft.registry.RegistryKey;

/**
 * Sapling generator for sulfur trees — uses birch as placeholder.
 */
public final class SulfurSaplingGenerator {
    public static final SaplingGenerator INSTANCE =
            new SaplingGenerator(RegistryKey.of(
                    net.minecraft.world.gen.feature.ConfiguredFeatures.REGISTRY_KEY,
                    net.minecraft.util.Identifier.of("minecraft", "birch")));

    private SulfurSaplingGenerator() {}
}
