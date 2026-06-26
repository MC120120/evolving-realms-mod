package net.evolvingreaims.evolvingrealms;

import net.evolvingreaims.evolvingrealms.ai.adaptive.AdaptiveDifficultyManager;
import net.evolvingreaims.evolvingrealms.config.EvolvingRealmsConfig;
import net.evolvingreaims.evolvingrealms.dungeon.DungeonGenerator;
import net.evolvingreaims.evolvingrealms.registry.*;
import net.evolvingreaims.evolvingrealms.village.VillageManager;
import net.evolvingreaims.evolvingrealms.world.LivingEarthManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entrypoint for Evolving Realms.
 * Registers all content and initialises server-side systems.
 */
public class EvolvingRealms implements ModInitializer {

    public static final String MOD_ID = "evolving_realms";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Evolving Realms is initialising...");

        // Load config first so every system can read it
        EvolvingRealmsConfig.load();

        // Registries (order matters – items depend on blocks, entities depend on sounds)
        ModSounds.register();
        ModParticles.register();
        ModBlocks.register();
        ModItems.register();
        ModFluids.register();
        ModEntities.register();
        ModBlockEntities.register();
        ModBiomes.register();
        ModDimensions.register();
        ModStructures.register();
        ModWorldGen.register();
        ModAdvancements.register();

        // Server lifecycle hooks
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            AdaptiveDifficultyManager.INSTANCE.init(server);
            VillageManager.INSTANCE.init(server);
            LivingEarthManager.INSTANCE.init(server);
            DungeonGenerator.INSTANCE.init(server);
            LOGGER.info("Evolving Realms server systems started.");
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            AdaptiveDifficultyManager.INSTANCE.save(server);
            VillageManager.INSTANCE.save(server);
            LivingEarthManager.INSTANCE.save(server);
        });

        // Tick events for living systems
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (!EvolvingRealmsConfig.get().disableAdaptiveAi) {
                AdaptiveDifficultyManager.INSTANCE.tick(server);
            }
            if (!EvolvingRealmsConfig.get().disableLivingCivilizations) {
                VillageManager.INSTANCE.tick(server);
            }
            if (!EvolvingRealmsConfig.get().disableLivingEarth) {
                LivingEarthManager.INSTANCE.tick(server);
            }
        });

        LOGGER.info("Evolving Realms initialised successfully.");
    }
}
