package net.evolvingreaims.evolvingrealms.config;

import com.google.gson.*;
import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;

/**
 * Simple JSON-based config for Evolving Realms.
 *
 * Located at: config/evolving_realms.json
 *
 * All settings are public final after load(); the config is immutable at runtime.
 * To apply changes, restart the server/game.
 */
public class EvolvingRealmsConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static EvolvingRealmsConfig INSTANCE;

    // -------------------------------------------------------------------------
    // Config Fields (with defaults)
    // -------------------------------------------------------------------------
    /** Disable the adaptive AI system entirely. */
    public boolean disableAdaptiveAi = false;

    /** Disable living civilization village simulation. */
    public boolean disableLivingCivilizations = false;

    /** Disable living earth events (volcanoes, wildfires, seasons, …). */
    public boolean disableLivingEarth = false;

    /** Season length in Minecraft days (default 8 days per season). */
    public int seasonLengthDays = 8;

    /** Volcano eruption chance (1-in-N per event check). */
    public int volcanoEruptionChance = 2000;

    /** Wildfire chance per Summer event check. */
    public int wildfireChance = 1500;

    /** Maximum number of dungeon rooms per dungeon. */
    public int dungeonMaxRooms = 18;

    /** Whether mob griefing is allowed for adaptive block-breaking goal. */
    public boolean adaptiveMobGriefing = true;

    /** Multiplier on boss health (0.5 = half, 2.0 = double). */
    public float bossHealthMultiplier = 1.0f;

    /** How many Sulfur Dimension portals can exist simultaneously per world. */
    public int maxSulfurPortals = 3;

    /** Enable debug logging. */
    public boolean debugLogging = false;

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------
    public static void load() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("evolving_realms.json");
        EvolvingRealmsConfig defaults = new EvolvingRealmsConfig();

        if (!configPath.toFile().exists()) {
            INSTANCE = defaults;
            save(configPath, INSTANCE);
            EvolvingRealms.LOGGER.info("Created default config at {}", configPath);
            return;
        }

        try (Reader reader = new FileReader(configPath.toFile())) {
            INSTANCE = GSON.fromJson(reader, EvolvingRealmsConfig.class);
            if (INSTANCE == null) INSTANCE = defaults;
            EvolvingRealms.LOGGER.info("Loaded config from {}", configPath);
        } catch (IOException | JsonSyntaxException e) {
            EvolvingRealms.LOGGER.error("Failed to load config; using defaults", e);
            INSTANCE = defaults;
        }

        // Re-save to ensure any new fields are written out
        save(configPath, INSTANCE);
    }

    private static void save(Path path, EvolvingRealmsConfig config) {
        try (Writer writer = new FileWriter(path.toFile())) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            EvolvingRealms.LOGGER.error("Failed to save config", e);
        }
    }

    public static EvolvingRealmsConfig get() {
        if (INSTANCE == null) load();
        return INSTANCE;
    }
}
