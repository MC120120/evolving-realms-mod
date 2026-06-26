package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.Optional;

/**
 * Sapling generator for burnt trees — grows into a small dead oak variant.
 * Uses vanilla oak tree as fallback; custom configured feature can be
 * supplied via data pack by overriding the placed feature.
 */
public class BurntSaplingGenerator extends SaplingGenerator {

    @Override
    protected Optional<RegistryKey<ConfiguredFeature<?, ?>>> getTreeFeature(
            net.minecraft.util.math.random.Random random, boolean bees) {
        return Optional.of(net.minecraft.world.gen.feature.TreeConfiguredFeatures.OAK);
    }
}
