package net.evolvingreaims.evolvingrealms.world.season;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

/**
 * Tracks the current season and applies appropriate environmental effects.
 *
 * One full year = 8 Minecraft days per season × 4 seasons = 32 days.
 * This keeps the cycle noticeable without being tedious.
 *
 * Effects per season:
 *   Spring  — increased rainfall, flood risk, animals breed freely
 *   Summer  — droughts possible, wildfire risk, crops grow fast
 *   Autumn  — leaves colour and fall, animals begin migrating
 *   Winter  — snow accumulates, water freezes, hostile mobs slightly stronger
 */
public class SeasonManager {

    public static final SeasonManager INSTANCE = new SeasonManager();

    public static SeasonManager getInstance() { return INSTANCE; }

    /** Days per season. Configurable via EvolvingRealmsConfig. */
    public static final int DAYS_PER_SEASON = 8;

    public enum Season { SPRING, SUMMER, AUTUMN, WINTER }

    private Season current   = Season.SPRING;
    private int    dayInSeason = 0;

    private long lastCheckedDay = -1;

    public void init(MinecraftServer server) {
        // Restore from world time if applicable
        ServerWorld overworld = server.getWorld(World.OVERWORLD);
        if (overworld != null) {
            long totalDays = overworld.getTimeOfDay() / 24000L;
            int seasonIndex = (int) ((totalDays / DAYS_PER_SEASON) % 4);
            current      = Season.values()[seasonIndex];
            dayInSeason  = (int) (totalDays % DAYS_PER_SEASON);
            EvolvingRealms.LOGGER.info("SeasonManager restored. Season={} day={}", current, dayInSeason);
        }
    }

    public void tick(MinecraftServer server) {
        ServerWorld overworld = server.getWorld(World.OVERWORLD);
        if (overworld == null) return;

        long currentDay = overworld.getTimeOfDay() / 24000L;
        if (currentDay == lastCheckedDay) return;
        lastCheckedDay = currentDay;

        dayInSeason++;
        if (dayInSeason >= DAYS_PER_SEASON) {
            dayInSeason = 0;
            advanceSeason(server);
        }
    }

    private void advanceSeason(MinecraftServer server) {
        Season previous = current;
        current = Season.values()[(current.ordinal() + 1) % 4];
        EvolvingRealms.LOGGER.info("Season changed: {} → {}", previous, current);

        applySeasonEffects(server, current);
        broadcastSeasonChange(server, current);
    }

    private void applySeasonEffects(MinecraftServer server, Season season) {
        for (ServerWorld world : server.getWorlds()) {
            switch (season) {
                case WINTER -> {
                    // Increase rain to create snow effect at high altitude
                    if (world.getRegistryKey() == World.OVERWORLD) {
                        world.setWeather(0, 6000, true, false); // 5 min rain as snowfall proxy
                    }
                }
                case SPRING -> {
                    // More frequent rain
                    if (world.getRegistryKey() == World.OVERWORLD) {
                        world.setWeather(0, 3000, true, false);
                    }
                }
                case SUMMER -> {
                    // Clear weather
                    if (world.getRegistryKey() == World.OVERWORLD) {
                        world.setWeather(12000, 0, false, false);
                    }
                }
                case AUTUMN -> { /* leaf particles handled by client */ }
            }
        }
    }

    private void broadcastSeasonChange(MinecraftServer server, Season season) {
        String key = "season.evolving_realms." + season.name().toLowerCase();
        server.getPlayerManager().getPlayerList()
              .forEach(p -> p.sendMessage(
                      net.minecraft.text.Text.translatable("message.evolving_realms.season_change",
                              net.minecraft.text.Text.translatable(key)),
                      false));
    }

    public void save(MinecraftServer server) {
        // Season state is derived from world time on load; nothing to persist.
    }

    public Season getCurrentSeason(net.minecraft.world.World world) { return current; }
    public Season getCurrentSeason() { return current; }
    public int    getDayInSeason()   { return dayInSeason; }
}
