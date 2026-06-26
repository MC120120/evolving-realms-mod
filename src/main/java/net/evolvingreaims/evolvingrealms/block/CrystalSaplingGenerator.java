package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;

import java.util.Optional;

public class CrystalSaplingGenerator extends SaplingGenerator {

    @Override
    protected Optional<RegistryKey<ConfiguredFeature<?, ?>>> getTreeFeature(
            net.minecraft.util.math.random.Random random, boolean bees) {
        return Optional.of(TreeConfiguredFeatures.SPRUCE);
    }
}
