package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;

import java.util.Optional;

/**
 * Sapling generator for sulfur trees — grows into a birch-variant until
 * a custom configured feature is registered via data pack.
 */
public class SulfurSaplingGenerator extends SaplingGenerator {

    @Override
    protected Optional<RegistryKey<ConfiguredFeature<?, ?>>> getTreeFeature(
            net.minecraft.util.math.random.Random random, boolean bees) {
        return Optional.of(TreeConfiguredFeatures.BIRCH);
    }
}
