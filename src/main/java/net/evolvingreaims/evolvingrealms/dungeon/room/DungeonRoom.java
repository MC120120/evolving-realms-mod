package net.evolvingreaims.evolvingrealms.dungeon.room;

import net.evolvingreaims.evolvingrealms.dungeon.DungeonTheme;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

/**
 * Base class for all dungeon room types.
 *
 * Subclasses override {@link #placeInterior(ServerWorld, DungeonTheme, Random)}
 * to add furniture, mobs, loot chests, traps, or puzzle mechanisms.
 */
public abstract class DungeonRoom {

    protected final BlockPos origin; // bottom-north-west corner
    protected final int width;
    protected final int height;
    protected final int depth;

    protected DungeonRoom(BlockPos origin, int width, int height, int depth) {
        this.origin = origin;
        this.width  = width;
        this.height = height;
        this.depth  = depth;
    }

    // -------------------------------------------------------------------------
    // Placement
    // -------------------------------------------------------------------------
    /**
     * Carves and decorates this room into the world.
     * Called once during dungeon generation.
     */
    public final void place(ServerWorld world, DungeonTheme theme, Random rng) {
        carveShell(world, theme);
        placeInterior(world, theme, rng);
    }

    /** Carves the room shell (walls, floor, ceiling, corners). */
    protected void carveShell(ServerWorld world, DungeonTheme theme) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    BlockPos p = origin.add(x, y, z);
                    boolean isWall   = x == 0 || x == width - 1 || z == 0 || z == depth - 1;
                    boolean isFloor  = y == 0;
                    boolean isCeiling = y == height - 1;

                    if (isFloor) {
                        world.setBlockState(p, theme.floorBlock());
                    } else if (isCeiling) {
                        world.setBlockState(p, theme.ceilingBlock());
                    } else if (isWall) {
                        world.setBlockState(p, theme.wallBlock());
                    } else {
                        world.setBlockState(p, Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
    }

    /** Override to place room-specific content. */
    protected abstract void placeInterior(ServerWorld world, DungeonTheme theme, Random rng);

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    protected BlockPos getCenter() {
        return origin.add(width / 2, 1, depth / 2);
    }

    public BlockPos getEntrance() {
        return origin.add(width / 2, 1, 0); // south-facing door
    }

    /** Checks whether a proposed room would overlap this one. */
    public boolean overlaps(BlockPos otherOrigin, int otherW, int otherH, int otherD) {
        return otherOrigin.getX() < origin.getX() + width  + 2 &&
               otherOrigin.getX() + otherW + 2 > origin.getX() &&
               otherOrigin.getY() < origin.getY() + height + 2 &&
               otherOrigin.getY() + otherH + 2 > origin.getY() &&
               otherOrigin.getZ() < origin.getZ() + depth  + 2 &&
               otherOrigin.getZ() + otherD + 2 > origin.getZ();
    }

    protected void placeTorch(ServerWorld world, BlockPos pos) {
        if (world.getBlockState(pos).isAir()) {
            world.setBlockState(pos, Blocks.TORCH.getDefaultState());
        }
    }

    protected void placeChest(ServerWorld world, BlockPos pos, net.minecraft.util.Identifier lootTable) {
        world.setBlockState(pos, Blocks.CHEST.getDefaultState());
        if (world.getBlockEntity(pos) instanceof net.minecraft.block.entity.ChestBlockEntity chest) {
            chest.setLootTable(lootTable, world.getRandom().nextLong());
        }
    }

    protected void spawnMob(ServerWorld world, BlockPos pos, net.minecraft.entity.EntityType<?> type) {
        net.minecraft.entity.Entity entity = type.create(world);
        if (entity != null) {
            entity.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
            world.spawnEntity(entity);
        }
    }
}
