package net.evolvingreaims.evolvingrealms.client;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.client.render.*;
import net.evolvingreaims.evolvingrealms.registry.ModEntities;
import net.evolvingreaims.evolvingrealms.registry.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client-side entrypoint. Registers renderers, particle factories, and
 * screen overlays that must only exist on the logical client.
 */
public class EvolvingRealmsClient implements ClientModInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EvolvingRealms.MOD_ID + "/client");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Evolving Realms client initialising...");

        registerEntityRenderers();
        registerParticleFactories();

        LOGGER.info("Evolving Realms client initialised.");
    }

    private void registerEntityRenderers() {
        EntityRendererRegistry.register(ModEntities.SULFUR_SLIME,    SulfurSlimeRenderer::new);
        EntityRendererRegistry.register(ModEntities.ACID_SPIDER,     AcidSpiderRenderer::new);
        EntityRendererRegistry.register(ModEntities.CRYSTAL_GOLEM,   CrystalGolemRenderer::new);
        EntityRendererRegistry.register(ModEntities.TOXIC_SKELETON,  ToxicSkeletonRenderer::new);
        EntityRendererRegistry.register(ModEntities.ASH_ZOMBIE,      AshZombieRenderer::new);
        EntityRendererRegistry.register(ModEntities.LAVA_WORM,       LavaWormRenderer::new);
        EntityRendererRegistry.register(ModEntities.CRYSTAL_BEETLE,  CrystalBeetleRenderer::new);
        EntityRendererRegistry.register(ModEntities.SULFUR_PHANTOM,  SulfurPhantomRenderer::new);
        EntityRendererRegistry.register(ModEntities.ACID_FISH,       AcidFishRenderer::new);
        EntityRendererRegistry.register(ModEntities.BURNING_WOLF,    BurningWolfRenderer::new);
        EntityRendererRegistry.register(ModEntities.ANCIENT_SULFUR_TITAN, AncientSulfurTitanRenderer::new);
    }

    private void registerParticleFactories() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.SULFUR_SMOKE,    SulfurSmokeParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.ACID_DRIP,       AcidDripParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.CRYSTAL_SPARKLE, CrystalSparkleParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.ASH_FLAKE,       AshFlakeParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.LAVA_EMBER,      LavaEmberParticle.Factory::new);
    }
}
