package net.evolvingreaims.evolvingrealms.village;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.village.data.VillageData;
import net.evolvingreaims.evolvingrealms.village.event.VillageEventManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.poi.PointOfInterestStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Village Manager — tracks all living civilizations in the world.
 *
 * Responsibilities:
 *  - Discovers villages from PoI data (beds, job sites)
 *  - Maintains VillageData for each settlement
 *  - Ticks VillageData to evolve population, economy, buildings
 *  - Fires VillageEvents (weddings, raids, festivals, …)
 *  - Persists all data to NBT between sessions
 */
public class VillageManager {

    public static final VillageManager INSTANCE = new VillageManager();

    /** Ticks between village discovery sweeps (5 minutes). */
    private static final int DISCOVERY_INTERVAL = 6000;
    /** Ticks between full village ticks (10 seconds). */
    private static final int VILLAGE_TICK_INTERVAL = 200;

    private MinecraftServer server;
    private final List<VillageData> villages = new ArrayList<>();
    private final VillageEventManager eventManager = new VillageEventManager();

    private int discoveryTimer   = 0;
    private int villageTickTimer = 0;

    private VillageManager() {}

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------
    public void init(MinecraftServer server) {
        this.server = server;
        load(server);
        EvolvingRealms.LOGGER.info("VillageManager initialised with {} villages.", villages.size());
    }

    public void tick(MinecraftServer server) {
        discoveryTimer++;
        villageTickTimer++;

        if (discoveryTimer >= DISCOVERY_INTERVAL) {
            discoveryTimer = 0;
            discoverNewVillages(server);
        }

        if (villageTickTimer >= VILLAGE_TICK_INTERVAL) {
            villageTickTimer = 0;
            for (VillageData village : villages) {
                village.tick(server);
                eventManager.checkAndFireEvents(server, village);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Discovery
    // -------------------------------------------------------------------------
    private void discoverNewVillages(MinecraftServer server) {
        for (ServerWorld world : server.getWorlds()) {
            world.getPointOfInterestStorage()
                 .getPositions(type -> type.isIn(net.minecraft.registry.tag.PointOfInterestTypeTags.VILLAGE),
                               pos -> !isNearKnownVillage(pos),
                               new BlockPos(0, 64, 0),
                               5000,
                               PointOfInterestStorage.OccupationStatus.ANY)
                 .findFirst()
                 .ifPresent(seedPos -> {
                     VillageData village = VillageData.discover(world, seedPos);
                     if (village != null) {
                         villages.add(village);
                         EvolvingRealms.LOGGER.debug("Discovered new village at {}", seedPos);
                     }
                 });
        }
    }

    private boolean isNearKnownVillage(BlockPos pos) {
        for (VillageData v : villages) {
            if (v.getCenter().isWithinDistance(pos, 80)) return true;
        }
        return false;
    }

    // -------------------------------------------------------------------------
    // Persistence
    // -------------------------------------------------------------------------
    public void save(MinecraftServer server) {
        Path path = getSavePath(server);
        NbtCompound root = new NbtCompound();
        NbtList list = new NbtList();
        for (VillageData v : villages) {
            list.add(v.toNbt());
        }
        root.put("Villages", list);
        try {
            Files.createDirectories(path.getParent());
            NbtIo.write(root, path);
        } catch (IOException e) {
            EvolvingRealms.LOGGER.error("Failed to save VillageManager data", e);
        }
    }

    private void load(MinecraftServer server) {
        Path path = getSavePath(server);
        if (!Files.exists(path)) return;
        try {
            NbtCompound root = NbtIo.read(path);
            if (root == null) return;
            NbtList list = root.getList("Villages", 10);
            for (int i = 0; i < list.size(); i++) {
                VillageData v = VillageData.fromNbt(list.getCompound(i));
                villages.add(v);
            }
        } catch (IOException e) {
            EvolvingRealms.LOGGER.error("Failed to load VillageManager data", e);
        }
    }

    private Path getSavePath(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT)
                     .resolve("evolving_realms")
                     .resolve("villages.nbt");
    }

    public List<VillageData> getVillages() { return villages; }
}
