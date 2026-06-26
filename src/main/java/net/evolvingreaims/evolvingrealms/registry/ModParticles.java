package net.evolvingreaims.evolvingrealms.registry;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModParticles {

    public static final SimpleParticleType SULFUR_SMOKE    = register("sulfur_smoke",    false);
    public static final SimpleParticleType ACID_DRIP       = register("acid_drip",       false);
    public static final SimpleParticleType CRYSTAL_SPARKLE = register("crystal_sparkle", true);
    public static final SimpleParticleType ASH_FLAKE       = register("ash_flake",       false);
    public static final SimpleParticleType LAVA_EMBER      = register("lava_ember",      true);

    private static SimpleParticleType register(String name, boolean alwaysSpawn) {
        return Registry.register(
                Registries.PARTICLE_TYPE,
                Identifier.of(EvolvingRealms.MOD_ID, name),
                new SimpleParticleType(alwaysSpawn));
    }

    public static void register() {
        EvolvingRealms.LOGGER.debug("Registering particles for Evolving Realms");
    }
}
