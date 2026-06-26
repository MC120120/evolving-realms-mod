package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.Optional;

/**
 * Sapling generator for crystal trees — grows into a spruce-variant until
 * a custom configured feature is registered via data pack.
 */
public class CrystalSaplingGenerator extends SaplingGenerator {

    @Override
    protected Optional<RegistryKey<ConfiguredFeature<?, ?>>> getTreeFeature(
            net.minecraft.util.math.random.Random random, boolean bees) {
        return Optional.of(net.minecraft.world.gen.feature.TreeConfiguredFeatures.SPRUCE);
    }
}
