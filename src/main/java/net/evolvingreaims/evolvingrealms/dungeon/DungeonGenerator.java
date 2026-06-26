package net.evolvingreaims.evolvingrealms.dungeon;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.dungeon.room.*;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

/**
 * Procedural Dungeon Generator.
 *
 * Generates unique, themed dungeon layouts underground.
 * No two dungeons are identical — room type, layout, loot, and
 * mini-boss are all randomised per generation.
 *
 * Architecture:
 *   DungeonGenerator picks a theme and depth, then uses DungeonLayout
 *   to BSP-partition the available space into rooms and corridors.
 *   Each room receives a DungeonRoom sub-type (Combat, Puzzle, Treasure …)
 *   that places its own blocks, entities, and loot.
 *
 * Dungeons are generated when a player enters a qualifying cave biome
 * or when explicitly triggered by a Dungeon Compass item.
 */
public class DungeonGenerator {

    public static final DungeonGenerator INSTANCE = new DungeonGenerator();

    /** Maximum rooms per dungeon (including corridors). */
    private static final int MAX_ROOMS = 18;
    /** Minimum depth below surface for generation. */
    private static final int MIN_DEPTH = 20;

    private MinecraftServer server;
    private final Random rng = new Random();

    private DungeonGenerator() {}

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------
    public void init(MinecraftServer server) {
        this.server = server;
        EvolvingRealms.LOGGER.info("DungeonGenerator ready.");
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------
    /**
     * Attempts to generate a dungeon centered on {@code origin}.
     * Returns the entry BlockPos if successful, or empty.
     */
    public Optional<BlockPos> generateDungeon(ServerWorld world, BlockPos origin) {
        // Choose theme based on biome / dimension
        DungeonTheme theme = chooseTheme(world, origin);

        // Find a valid underground cavity to build in
        BlockPos start = findUndergroundStart(world, origin);
        if (start == null) {
            EvolvingRealms.LOGGER.debug("DungeonGenerator: no valid start point near {}", origin);
            return Optional.empty();
        }

        // Build layout
        List<DungeonRoom> rooms = buildLayout(theme, start);
        if (rooms.isEmpty()) return Optional.empty();

        // Place all rooms
        for (DungeonRoom room : rooms) {
            room.place(world, theme, rng);
        }

        // Connect rooms with corridors
        connectRooms(world, rooms, theme);

        // Seal the dungeon (place walls around the perimeter)
        sealPerimeter(world, rooms, theme);

        EvolvingRealms.LOGGER.info("Generated {} dungeon at {} with {} rooms.",
                theme.name(), start, rooms.size());

        return Optional.of(rooms.get(0).getEntrance());
    }

    // -------------------------------------------------------------------------
    // Layout
    // -------------------------------------------------------------------------
    private List<DungeonRoom> buildLayout(DungeonTheme theme, BlockPos start) {
        List<DungeonRoom> rooms = new ArrayList<>();
        Deque<BlockPos> frontier = new ArrayDeque<>();
        frontier.add(start);

        // Always start with an entrance corridor
        rooms.add(new CombatRoom(start, 9, 5, 9));

        int attempts = 0;
        while (rooms.size() < MAX_ROOMS && !frontier.isEmpty() && attempts++ < 60) {
            BlockPos parent = frontier.poll();

            // Random direction for next room
            int[] dir = randomDirection();
            int roomW  = 7 + rng.nextInt(7);
            int roomH  = 4 + rng.nextInt(3);
            int roomD  = 7 + rng.nextInt(7);
            int offset = 8 + rng.nextInt(6);

            BlockPos next = parent.add(dir[0] * offset, 0, dir[1] * offset);

            // Avoid overlaps
            boolean overlaps = rooms.stream().anyMatch(r -> r.overlaps(next, roomW, roomH, roomD));
            if (overlaps) continue;

            DungeonRoom room = createRoom(next, roomW, roomH, roomD, rooms.size(), theme);
            rooms.add(room);
            frontier.add(next);

            // Occasionally branch
            if (rng.nextInt(3) == 0) frontier.add(parent);
        }

        return rooms;
    }

    private DungeonRoom createRoom(BlockPos pos, int w, int h, int d, int index, DungeonTheme theme) {
        // Last room is always a boss arena or treasure vault
        if (index == MAX_ROOMS - 1) {
            return rng.nextBoolean()
                    ? new BossArenaRoom(pos, Math.max(w, 15), h + 2, Math.max(d, 15))
                    : new TreasureRoom(pos, w, h, d);
        }

        // Weighted random room selection
        int roll = rng.nextInt(100);
        if (roll < 30) return new CombatRoom(pos, w, h, d);
        if (roll < 50) return new TreasureRoom(pos, w, h, d);
        if (roll < 65) return new PuzzleRoom(pos, w, h, d);
        if (roll < 75) return new LibraryRoom(pos, w, h, d);
        if (roll < 85) return new TrapHallwayRoom(pos, w, h, d);
        if (roll < 92) return new CryptRoom(pos, w, h, d);
        return new SecretVaultRoom(pos, w, h, d);
    }

    // -------------------------------------------------------------------------
    // Corridors
    // -------------------------------------------------------------------------
    private void connectRooms(ServerWorld world, List<DungeonRoom> rooms, DungeonTheme theme) {
        for (int i = 1; i < rooms.size(); i++) {
            BlockPos a = rooms.get(i - 1).getCenter();
            BlockPos b = rooms.get(i).getCenter();
            carveCorridor(world, a, b, theme);
        }
    }

    private void carveCorridor(ServerWorld world, BlockPos from, BlockPos to, DungeonTheme theme) {
        // L-shaped corridor: horizontal then vertical
        BlockPos mid = new BlockPos(to.getX(), from.getY(), from.getZ());
        carveSegment(world, from, mid, theme);
        carveSegment(world, mid, to, theme);
    }

    private void carveSegment(ServerWorld world, BlockPos from, BlockPos to, DungeonTheme theme) {
        int x0 = Math.min(from.getX(), to.getX());
        int x1 = Math.max(from.getX(), to.getX());
        int z0 = Math.min(from.getZ(), to.getZ());
        int z1 = Math.max(from.getZ(), to.getZ());
        int y  = from.getY();

        for (int x = x0; x <= x1; x++) {
            for (int z = z0; z <= z1; z++) {
                for (int dy = 0; dy < 3; dy++) {
                    BlockPos p = new BlockPos(x, y + dy, z);
                    world.setBlockState(p, Blocks.AIR.getDefaultState());
                }
                // Floor
                world.setBlockState(new BlockPos(x, y - 1, z), theme.floorBlock());
                // Ceiling
                world.setBlockState(new BlockPos(x, y + 3, z), theme.ceilingBlock());
            }
        }
    }

    // -------------------------------------------------------------------------
    // Sealing
    // -------------------------------------------------------------------------
    private void sealPerimeter(ServerWorld world, List<DungeonRoom> rooms, DungeonTheme theme) {
        // Already handled by each room's place() method — rooms place their own walls
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private DungeonTheme chooseTheme(ServerWorld world, BlockPos pos) {
        DungeonTheme[] themes = DungeonTheme.values();
        return themes[rng.nextInt(themes.length)];
    }

    private BlockPos findUndergroundStart(ServerWorld world, BlockPos surface) {
        int targetY = surface.getY() - MIN_DEPTH - rng.nextInt(20);
        if (targetY < world.getBottomY() + 10) return null;
        BlockPos candidate = new BlockPos(surface.getX(), targetY, surface.getZ());
        // Must be in solid ground
        if (world.getBlockState(candidate).isAir()) return null;
        return candidate;
    }

    private int[] randomDirection() {
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        return dirs[rng.nextInt(dirs.length)];
    }
}
