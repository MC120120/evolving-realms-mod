package net.evolvingreaims.evolvingrealms.ai.adaptive;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.WorldSavePath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Adaptive Difficulty Manager — tracks per-player combat behaviour and
 * adjusts hostile mob goals at runtime to counter the player's dominant
 * strategy.
 *
 * Behaviour categories tracked (per player, accumulated over time):
 *   BOW_KILLS        — how often the player kills with ranged weapons
 *   MELEE_KILLS      — how often the player kills in melee
 *   TOWER_BUILDS     — block-placement events during combat
 *   SLEEP_EVENTS     — how often the player sleeps
 *   FARM_INTERACTIONS — crop/animal interactions
 *
 * Every 30 seconds of server time the manager re-evaluates each player
 * and sets a CombatProfile that mob goals read to adjust behaviour.
 */
public class AdaptiveDifficultyManager {

    public static final AdaptiveDifficultyManager INSTANCE = new AdaptiveDifficultyManager();

    private static final int EVAL_INTERVAL_TICKS = 600; // 30 s
    private int tickCounter = 0;
    private MinecraftServer server;

    /** Persistent counters per player UUID. */
    private final Map<UUID, PlayerBehaviorData> dataMap = new HashMap<>();

    private AdaptiveDifficultyManager() {}

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------
    public void init(MinecraftServer server) {
        this.server = server;
        load(server);
        EvolvingRealms.LOGGER.info("AdaptiveDifficultyManager initialised.");
    }

    public void tick(MinecraftServer server) {
        tickCounter++;
        if (tickCounter < EVAL_INTERVAL_TICKS) return;
        tickCounter = 0;
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            evaluatePlayer(player);
        }
    }

    // -------------------------------------------------------------------------
    // Recording Events
    // -------------------------------------------------------------------------
    public void recordBowKill(UUID playerId) {
        getData(playerId).bowKills++;
    }

    public void recordMeleeKill(UUID playerId) {
        getData(playerId).meleeKills++;
    }

    public void recordBlockPlacedInCombat(UUID playerId) {
        getData(playerId).towerBuilds++;
    }

    public void recordSleep(UUID playerId) {
        getData(playerId).sleepEvents++;
    }

    public void recordFarmInteraction(UUID playerId) {
        getData(playerId).farmInteractions++;
    }

    // -------------------------------------------------------------------------
    // Profile Evaluation
    // -------------------------------------------------------------------------
    private void evaluatePlayer(ServerPlayerEntity player) {
        PlayerBehaviorData data = getData(player.getUuid());
        CombatProfile newProfile = computeProfile(data);
        if (newProfile != data.profile) {
            data.profile = newProfile;
            EvolvingRealms.LOGGER.debug(
                "AdaptiveAI: player {} profile changed to {}",
                player.getNameForScoreboard(), newProfile);
        }
    }

    private CombatProfile computeProfile(PlayerBehaviorData data) {
        int total = data.bowKills + data.meleeKills + data.towerBuilds;
        if (total == 0) return CombatProfile.DEFAULT;

        float bowRatio    = (float) data.bowKills    / Math.max(1, total);
        float meleeRatio  = (float) data.meleeKills  / Math.max(1, total);
        float towerRatio  = (float) data.towerBuilds / Math.max(1, total);

        if (bowRatio   > 0.55f) return CombatProfile.BOW_DOMINANT;
        if (meleeRatio > 0.55f) return CombatProfile.MELEE_DOMINANT;
        if (towerRatio > 0.40f) return CombatProfile.BUILDER;
        if (data.sleepEvents > 20) return CombatProfile.SLEEPER;
        return CombatProfile.DEFAULT;
    }

    // -------------------------------------------------------------------------
    // Query
    // -------------------------------------------------------------------------
    public CombatProfile getProfile(UUID playerId) {
        return getData(playerId).profile;
    }

    /** Returns the nearest player profile within radius, or DEFAULT. */
    public CombatProfile getNearestProfile(net.minecraft.entity.mob.MobEntity mob, double radius) {
        if (server == null) return CombatProfile.DEFAULT;
        ServerPlayerEntity nearest = mob.getWorld().getClosestPlayer(mob, radius);
        if (nearest == null) return CombatProfile.DEFAULT;
        return getProfile(nearest.getUuid());
    }

    // -------------------------------------------------------------------------
    // Persistence
    // -------------------------------------------------------------------------
    public void save(MinecraftServer server) {
        Path savePath = getSavePath(server);
        NbtCompound root = new NbtCompound();
        for (Map.Entry<UUID, PlayerBehaviorData> entry : dataMap.entrySet()) {
            NbtCompound playerNbt = new NbtCompound();
            entry.getValue().writeToNbt(playerNbt);
            root.put(entry.getKey().toString(), playerNbt);
        }
        try {
            Files.createDirectories(savePath.getParent());
            NbtIo.write(root, savePath);
        } catch (IOException e) {
            EvolvingRealms.LOGGER.error("Failed to save AdaptiveDifficulty data", e);
        }
    }

    private void load(MinecraftServer server) {
        Path savePath = getSavePath(server);
        if (!Files.exists(savePath)) return;
        try {
            NbtCompound root = NbtIo.read(savePath);
            if (root == null) return;
            for (String key : root.getKeys()) {
                try {
                    UUID uuid = UUID.fromString(key);
                    PlayerBehaviorData data = new PlayerBehaviorData();
                    data.readFromNbt(root.getCompound(key));
                    dataMap.put(uuid, data);
                } catch (IllegalArgumentException ignored) {}
            }
            EvolvingRealms.LOGGER.info("Loaded adaptive difficulty data for {} players.", dataMap.size());
        } catch (IOException e) {
            EvolvingRealms.LOGGER.error("Failed to load AdaptiveDifficulty data", e);
        }
    }

    private Path getSavePath(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT)
                .resolve("evolving_realms")
                .resolve("adaptive_difficulty.nbt");
    }

    private PlayerBehaviorData getData(UUID uuid) {
        return dataMap.computeIfAbsent(uuid, k -> new PlayerBehaviorData());
    }

    // -------------------------------------------------------------------------
    // Inner data class
    // -------------------------------------------------------------------------
    public static class PlayerBehaviorData {
        public int bowKills         = 0;
        public int meleeKills       = 0;
        public int towerBuilds      = 0;
        public int sleepEvents      = 0;
        public int farmInteractions = 0;
        public CombatProfile profile = CombatProfile.DEFAULT;

        public void writeToNbt(NbtCompound nbt) {
            nbt.putInt("BowKills",         bowKills);
            nbt.putInt("MeleeKills",       meleeKills);
            nbt.putInt("TowerBuilds",      towerBuilds);
            nbt.putInt("SleepEvents",      sleepEvents);
            nbt.putInt("FarmInteractions", farmInteractions);
            nbt.putString("Profile",       profile.name());
        }

        public void readFromNbt(NbtCompound nbt) {
            bowKills         = nbt.getInt("BowKills");
            meleeKills       = nbt.getInt("MeleeKills");
            towerBuilds      = nbt.getInt("TowerBuilds");
            sleepEvents      = nbt.getInt("SleepEvents");
            farmInteractions = nbt.getInt("FarmInteractions");
            try {
                profile = CombatProfile.valueOf(nbt.getString("Profile"));
            } catch (IllegalArgumentException e) {
                profile = CombatProfile.DEFAULT;
            }
        }
    }
}
