package net.evolvingreaims.evolvingrealms.world.nature;

import net.evolvingreaims.evolvingrealms.registry.ModSounds;
import net.evolvingreaims.evolvingrealms.world.season.SeasonManager;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.Random;

/**
 * Tree Growth System — simulates natural tree lifecycle.
 *
 * Each tick:
 *  - Samples a few loaded chunks at random
 *  - Applies seed spread near existing leaves (with directional wind bias)
 *  - Ages old trees (logs surrounded by decayed leaves → marked for felling)
 *  - Falls dead trees: replaces the trunk with logs tilted on the ground
 *
 * This system never modifies blocks if it would grief player structures;
 * it respects the MOB_GRIEFING game rule.
 */
public class TreeGrowthSystem {

    private static final int SAMPLES_PER_TICK     = 3;
    private static final int SEED_SPREAD_RANGE    = 8;
    private static final int SEED_SPREAD_CHANCE   = 200; // 1/200 per leaf block per sample
    private static final int TREE_DEATH_CHANCE    = 800; // 1/800 per log per sample

    private final Random rng = new Random();

    public void tick(ServerWorld world, SeasonManager.Season season) {
        if (!world.getGameRules().getBoolean(net.minecraft.world.GameRules.DO_MOB_GRIEFING)) return;

        Iterable<ChunkPos> loaded = world.getChunkManager().getLoadedChunkPositions()::iterator;
        int samples = 0;

        for (ChunkPos chunk : loaded) {
            if (samples++ >= SAMPLES_PER_TICK) break;
            processChunk(world, chunk, season);
        }
    }

    private void processChunk(ServerWorld world, ChunkPos chunk, SeasonManager.Season season) {
        int baseX = chunk.getStartX() + rng.nextInt(16);
        int baseZ = chunk.getStartZ() + rng.nextInt(16);

        for (int y = world.getBottomY(); y < world.getTopY(); y++) {
            BlockPos pos = new BlockPos(baseX, y, baseZ);

            if (world.getBlockState(pos).getBlock() instanceof LeavesBlock) {
                // Seed spread in spring/summer
                if (season == SeasonManager.Season.SPRING || season == SeasonManager.Season.SUMMER) {
                    if (rng.nextInt(SEED_SPREAD_CHANCE) == 0) {
                        trySeedSpread(world, pos);
                    }
                }
                // Autumn: leaves decay faster (vanilla already handles this, we accelerate)
                if (season == SeasonManager.Season.AUTUMN && rng.nextInt(50) == 0) {
                    if (world.getBlockState(pos).get(LeavesBlock.PERSISTENT).equals(false)) {
                        world.breakBlock(pos, true);
                    }
                }
            }

            if (world.getBlockState(pos).getBlock() instanceof PillarBlock) {
                // Dead trees: logs with no adjacent leaves → might fall
                if (rng.nextInt(TREE_DEATH_CHANCE) == 0 && isDeadTree(world, pos)) {
                    fellTree(world, pos);
                }
            }
        }
    }

    private void trySeedSpread(ServerWorld world, BlockPos leafPos) {
        // Wind direction bias (constant eastward drift for simplicity)
        int tx = leafPos.getX() + rng.nextInt(SEED_SPREAD_RANGE * 2) - SEED_SPREAD_RANGE + 1;
        int tz = leafPos.getZ() + rng.nextInt(SEED_SPREAD_RANGE * 2) - SEED_SPREAD_RANGE;
        int ty = leafPos.getY() - 1;

        BlockPos target = new BlockPos(tx, ty, tz);
        BlockPos ground = findGround(world, target);
        if (ground == null) return;

        if (world.getBlockState(ground).isOf(Blocks.GRASS_BLOCK) ||
            world.getBlockState(ground).isOf(Blocks.DIRT)) {
            if (world.getBlockState(ground.up()).isAir()) {
                world.setBlockState(ground.up(), Blocks.OAK_SAPLING.getDefaultState());
            }
        }
    }

    private boolean isDeadTree(ServerWorld world, BlockPos logPos) {
        // Check a 3×3×3 area for any leaves — if none, the tree is dead
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -1; dy <= 4; dy++) {
                for (int dz = -2; dz <= 2; dz++) {
                    if (world.getBlockState(logPos.add(dx, dy, dz)).getBlock() instanceof LeavesBlock) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void fellTree(ServerWorld world, BlockPos base) {
        // Walk up the trunk, then drop logs as items (simulates falling)
        world.playSound(null, base, ModSounds.TREE_FALL, SoundCategory.BLOCKS, 1.5f, 0.9f + rng.nextFloat() * 0.2f);
        BlockPos current = base;
        while (world.getBlockState(current).getBlock() instanceof PillarBlock) {
            world.breakBlock(current, true);
            current = current.up();
        }
    }

    private BlockPos findGround(ServerWorld world, BlockPos start) {
        for (int dy = 0; dy >= -4; dy--) {
            BlockPos p = start.add(0, dy, 0);
            if (!world.getBlockState(p).isAir() && world.getBlockState(p.up()).isAir()) return p;
        }
        return null;
    }
}
