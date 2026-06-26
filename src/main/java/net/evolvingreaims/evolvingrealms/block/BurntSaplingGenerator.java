package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.SaplingGenerator;
import net.minecraft.registry.RegistryKey;

/**
 * Sapling generator for burnt trees — uses oak as placeholder.
 */
public final class BurntSaplingGenerator {
    public static final SaplingGenerator INSTANCE =
            new SaplingGenerator(RegistryKey.of(
                    net.minecraft.world.gen.feature.ConfiguredFeatures.REGISTRY_KEY,
                    net.minecraft.util.Identifier.of("minecraft", "oak")));

    private BurntSaplingGenerator() {}
}
