package net.evolvingreaims.evolvingrealms.village.data;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.village.profession.BuilderVillagerTask;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

/**
 * Mutable state for one living civilization.
 *
 * Stats are on a 0–100 scale unless stated otherwise.
 *
 * Population  — number of villager entities (tracked, not a score)
 * Food        — determines population growth / famine risk
 * Economy     — drives trade, expansion budgets
 * Military    — determines raid resistance
 * Technology  — unlocks new building types and profession upgrades
 * Security    — reduced by raids, recovered by guards
 * Happiness   — affects birth rate and migration
 * Resources   — raw materials (wood, stone, iron) for construction
 */
public class VillageData {

    private final UUID   id;
    private       String name;
    private       BlockPos center;
    private       String dimensionId;

    // Core stats (0–100)
    private int food        = 50;
    private int economy     = 30;
    private int military    = 20;
    private int technology  = 10;
    private int security    = 60;
    private int happiness   = 70;
    private int resources   = 40;

    // Tracking
    private int populationCount   = 0;
    private int dayAge            = 0; // how many Minecraft days this village has existed
    private int expansionBudget   = 0;
    private int buildQueueSize    = 0;
    private boolean underRaid     = false;
    private boolean hasFamine     = false;

    // -------------------------------------------------------------------------
    // Construction / Discovery
    // -------------------------------------------------------------------------
    private VillageData(UUID id, String name, BlockPos center, String dimensionId) {
        this.id          = id;
        this.name        = name;
        this.center      = center;
        this.dimensionId = dimensionId;
    }

    /**
     * Creates a VillageData by examining the POI cluster around {@code seedPos}.
     * Returns null if no valid village can be formed.
     */
    public static VillageData discover(ServerWorld world, BlockPos seedPos) {
        // Count nearby beds to estimate population
        long beds = world.getPointOfInterestStorage()
                .getPositions(type -> type.isIn(net.minecraft.registry.tag.PointOfInterestTypeTags.VILLAGE),
                              pos -> pos.isWithinDistance(seedPos, 80),
                              seedPos, 80,
                              net.minecraft.world.poi.PointOfInterestStorage.OccupationStatus.ANY)
                .count();
        if (beds < 3) return null; // Too small — not a real village

        String name     = generateVillageName();
        String dimId    = world.getRegistryKey().getValue().toString();
        VillageData data = new VillageData(UUID.randomUUID(), name, seedPos, dimId);
        data.populationCount = (int) Math.min(beds, 30);
        return data;
    }

    // -------------------------------------------------------------------------
    // Tick — called every 10 server seconds
    // -------------------------------------------------------------------------
    public void tick(MinecraftServer server) {
        dayAge++;

        // Food cycle: happy & well-fed villages grow
        if (food > 70 && happiness > 60 && populationCount < 50) {
            if (dayAge % 20 == 0) populationCount++;
        } else if (food < 20) {
            hasFamine = true;
            happiness = Math.max(0, happiness - 2);
            if (dayAge % 30 == 0 && populationCount > 1) populationCount--;
        } else {
            hasFamine = false;
        }

        // Economy grows from trade and resources
        economy = clamp(economy + (resources > 50 ? 1 : -1));

        // Technology slowly advances with high economy
        if (economy > 60 && dayAge % 100 == 0) technology = clamp(technology + 1);

        // Security decays slightly; guards restore it
        if (!underRaid) security = clamp(security + 1);

        // Happiness depends on food, security, economy
        int happinessTarget = (food + security + economy) / 3;
        if (happiness < happinessTarget) happiness = clamp(happiness + 1);
        else if (happiness > happinessTarget) happiness = clamp(happiness - 1);

        // Resource consumption by building projects
        if (buildQueueSize > 0 && resources > 10) {
            resources -= 2;
            buildQueueSize--;
        }

        // Queue expansion projects when resources are abundant
        if (resources > 70 && economy > 50 && buildQueueSize == 0) {
            expansionBudget++;
            if (expansionBudget >= 5) {
                expansionBudget = 0;
                queueBuildingProject(server);
            }
        }

        // Harvest food from farms (simulated)
        if (dayAge % 8 == 0) food = clamp(food + 3);
        food = clamp(food - 1); // consumption
    }

    private void queueBuildingProject(MinecraftServer server) {
        buildQueueSize += 3;
        BuilderVillagerTask.scheduleBuild(server, this);
        EvolvingRealms.LOGGER.debug("Village '{}' queued a new building project.", name);
    }

    // -------------------------------------------------------------------------
    // Serialisation
    // -------------------------------------------------------------------------
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putUuid("Id", id);
        nbt.putString("Name", name);
        nbt.putInt("CenterX", center.getX());
        nbt.putInt("CenterY", center.getY());
        nbt.putInt("CenterZ", center.getZ());
        nbt.putString("Dimension", dimensionId);
        nbt.putInt("Food",        food);
        nbt.putInt("Economy",     economy);
        nbt.putInt("Military",    military);
        nbt.putInt("Technology",  technology);
        nbt.putInt("Security",    security);
        nbt.putInt("Happiness",   happiness);
        nbt.putInt("Resources",   resources);
        nbt.putInt("Population",  populationCount);
        nbt.putInt("DayAge",      dayAge);
        nbt.putBoolean("Famine",  hasFamine);
        nbt.putBoolean("UnderRaid", underRaid);
        return nbt;
    }

    public static VillageData fromNbt(NbtCompound nbt) {
        UUID     id     = nbt.getUuid("Id");
        String   name   = nbt.getString("Name");
        BlockPos center = new BlockPos(nbt.getInt("CenterX"), nbt.getInt("CenterY"), nbt.getInt("CenterZ"));
        String   dim    = nbt.getString("Dimension");
        VillageData data = new VillageData(id, name, center, dim);
        data.food           = nbt.getInt("Food");
        data.economy        = nbt.getInt("Economy");
        data.military       = nbt.getInt("Military");
        data.technology     = nbt.getInt("Technology");
        data.security       = nbt.getInt("Security");
        data.happiness      = nbt.getInt("Happiness");
        data.resources      = nbt.getInt("Resources");
        data.populationCount = nbt.getInt("Population");
        data.dayAge         = nbt.getInt("DayAge");
        data.hasFamine      = nbt.getBoolean("Famine");
        data.underRaid      = nbt.getBoolean("UnderRaid");
        return data;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private static int clamp(int v) { return Math.max(0, Math.min(100, v)); }

    private static String generateVillageName() {
        String[] prefixes = {"Kor","Vel","Arun","Mor","Sylv","Thar","Brin","Eld","Ash","Iron","Storm"};
        String[] suffixes = {"haven","hold","dale","wick","ford","burg","vale","keep","hollow","reach"};
        java.util.Random rng = new java.util.Random();
        return prefixes[rng.nextInt(prefixes.length)] + suffixes[rng.nextInt(suffixes.length)];
    }

    // -------------------------------------------------------------------------
    // Getters / Setters
    // -------------------------------------------------------------------------
    public UUID     getId()              { return id; }
    public String   getName()            { return name; }
    public BlockPos getCenter()          { return center; }
    public String   getDimensionId()     { return dimensionId; }
    public int      getFood()            { return food; }
    public int      getEconomy()         { return economy; }
    public int      getMilitary()        { return military; }
    public int      getTechnology()      { return technology; }
    public int      getSecurity()        { return security; }
    public int      getHappiness()       { return happiness; }
    public int      getResources()       { return resources; }
    public int      getPopulation()      { return populationCount; }
    public boolean  isUnderRaid()        { return underRaid; }
    public boolean  hasFamine()          { return hasFamine; }
    public void     setUnderRaid(boolean v)  { underRaid  = v; }
    public void     addResources(int amt)    { resources  = clamp(resources + amt); }
    public void     addFood(int amt)         { food       = clamp(food + amt); }
    public void     addMilitary(int amt)     { military   = clamp(military + amt); }
    public void     addSecurity(int amt)     { security   = clamp(security + amt); }
}
