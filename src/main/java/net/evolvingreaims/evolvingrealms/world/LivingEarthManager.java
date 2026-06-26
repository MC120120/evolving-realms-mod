package net.evolvingreaims.evolvingrealms.world;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.world.event.*;
import net.evolvingreaims.evolvingrealms.world.nature.*;
import net.evolvingreaims.evolvingrealms.world.season.SeasonManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class LivingEarthManager {

    public static final LivingEarthManager INSTANCE = new LivingEarthManager();

    private static final int NATURE_TICK_INTERVAL = 400;
    private static final int EVENT_CHECK_INTERVAL = 1200;

    private MinecraftServer server;
    private final SeasonManager        seasonManager     = new SeasonManager();
    private final TreeGrowthSystem     treeGrowthSystem  = new TreeGrowthSystem();
    private final VolcanoEruptionEvent volcanoEruption   = new VolcanoEruptionEvent();
    private final WildfireEvent        wildfireEvent     = new WildfireEvent();

    private int natureTick = 0;
    private int eventTick  = 0;

    private LivingEarthManager() {}

    public void init(MinecraftServer server) {
        this.server = server;
        seasonManager.init(server);
        EvolvingRealms.LOGGER.info("LivingEarthManager initialised. Current season: {}", seasonManager.getCurrentSeason());
    }

    public void save(MinecraftServer server) {
        seasonManager.save(server);
    }

    public void tick(MinecraftServer server) {
        seasonManager.tick(server);

        natureTick++;
        if (natureTick >= NATURE_TICK_INTERVAL) {
            natureTick = 0;
            SeasonManager.Season season = seasonManager.getCurrentSeason();
            for (ServerWorld world : server.getWorlds()) {
                treeGrowthSystem.tick(world, season);
                GrassSpreadSystem.getInstance().tick(world, season);
                AnimalMigrationSystem.getInstance().tick(world, world.getRandom());
            }
        }

        eventTick++;
        if (eventTick >= EVENT_CHECK_INTERVAL) {
            eventTick = 0;
            SeasonManager.Season season = seasonManager.getCurrentSeason();
            for (ServerWorld world : server.getWorlds()) {
                volcanoEruption.tryTrigger(world);
                tryTriggerEarthquake(world);
                tryTriggerFlood(world, season);
                wildfireEvent.tryTrigger(world, season);
            }
        }
    }

    private void tryTriggerEarthquake(ServerWorld world) {
        Random random = world.getRandom();
        if (random.nextInt(2000) != 0) return;
        for (net.minecraft.server.network.ServerPlayerEntity player : world.getPlayers()) {
            BlockPos epicentre = player.getBlockPos();
            int magnitude = 1 + random.nextInt(5);
            EarthquakeEvent event = new EarthquakeEvent(world, epicentre, magnitude);
            // Run 20 ticks immediately as a quick burst (or schedule via mixin tick)
            for (int i = 0; i < EarthquakeEvent.DURATION_TICKS && !event.isFinished(); i++) {
                event.tick(random);
            }
            break; // only one earthquake per check
        }
    }

    private void tryTriggerFlood(ServerWorld world, SeasonManager.Season season) {
        if (season != SeasonManager.Season.SPRING) return;
        Random random = world.getRandom();
        if (random.nextInt(3000) != 0) return;
        for (net.minecraft.server.network.ServerPlayerEntity player : world.getPlayers()) {
            BlockPos source = player.getBlockPos().add(
                random.nextBetween(-50, 50), 0, random.nextBetween(-50, 50));
            FloodEvent event = new FloodEvent(world, source, 20);
            for (int i = 0; i < FloodEvent.TOTAL_DURATION && !event.isFinished(); i++) {
                event.tick(random);
            }
            break;
        }
    }

    public SeasonManager getSeasonManager() { return seasonManager; }
}
