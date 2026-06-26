package net.evolvingreaims.evolvingrealms.world;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.world.event.*;
import net.evolvingreaims.evolvingrealms.world.nature.*;
import net.evolvingreaims.evolvingrealms.world.season.SeasonManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

/**
 * Living Earth Manager — orchestrates all dynamic nature systems.
 *
 * Systems:
 *   SeasonManager        — tracks season (Spring/Summer/Autumn/Winter) and applies weather/light changes
 *   TreeGrowthSystem     — natural tree ageing, death, seed spread, and trunk falling
 *   GrassSpreadSystem    — grass, flowers, moss, and fern spreading and die-off
 *   AnimalMigrationSystem — moves animal herds toward biome-appropriate locations each season
 *   VolcanoEruptionEvent — randomly erupts volcanoes in volcanic biomes
 *   EarthquakeEvent      — rare cave collapses and structural shifts
 *   FloodEvent           — water level rises near rivers during Spring
 *   WildfireEvent        — fire spreads realistically through dry biomes in Summer
 */
public class LivingEarthManager {

    public static final LivingEarthManager INSTANCE = new LivingEarthManager();

    private static final int NATURE_TICK_INTERVAL  = 400; // 20 s
    private static final int EVENT_CHECK_INTERVAL  = 1200; // 60 s

    private MinecraftServer server;
    private final SeasonManager        seasonManager        = new SeasonManager();
    private final TreeGrowthSystem     treeGrowthSystem     = new TreeGrowthSystem();
    private final GrassSpreadSystem    grassSpreadSystem    = new GrassSpreadSystem();
    private final AnimalMigrationSystem animalMigration     = new AnimalMigrationSystem();
    private final VolcanoEruptionEvent volcanoEruption      = new VolcanoEruptionEvent();
    private final EarthquakeEvent      earthquakeEvent      = new EarthquakeEvent();
    private final FloodEvent           floodEvent           = new FloodEvent();
    private final WildfireEvent        wildfireEvent        = new WildfireEvent();

    private int natureTick = 0;
    private int eventTick  = 0;

    private LivingEarthManager() {}

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------
    public void init(MinecraftServer server) {
        this.server = server;
        seasonManager.init(server);
        EvolvingRealms.LOGGER.info("LivingEarthManager initialised. Current season: {}", seasonManager.getCurrentSeason());
    }

    public void tick(MinecraftServer server) {
        seasonManager.tick(server);

        natureTick++;
        if (natureTick >= NATURE_TICK_INTERVAL) {
            natureTick = 0;
            for (ServerWorld world : server.getWorlds()) {
                treeGrowthSystem.tick(world, seasonManager.getCurrentSeason());
                grassSpreadSystem.tick(world, seasonManager.getCurrentSeason());
                animalMigration.tick(world, seasonManager.getCurrentSeason());
            }
        }

        eventTick++;
        if (eventTick >= EVENT_CHECK_INTERVAL) {
            eventTick = 0;
            for (ServerWorld world : server.getWorlds()) {
                volcanoEruption.tryTrigger(world);
                earthquakeEvent.tryTrigger(world);
                floodEvent.tryTrigger(world, seasonManager.getCurrentSeason());
                wildfireEvent.tryTrigger(world, seasonManager.getCurrentSeason());
            }
        }
    }

    public SeasonManager getSeasonManager() { return seasonManager; }
}
