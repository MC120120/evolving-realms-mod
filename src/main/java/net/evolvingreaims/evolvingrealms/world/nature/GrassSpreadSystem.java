package net.evolvingreaims.evolvingrealms.world.nature;

import net.evolvingreaims.evolvingrealms.registry.ModBlocks;
import net.evolvingreaims.evolvingrealms.world.season.SeasonManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

/**
 * GrassSpreadSystem — extends vanilla grass spreading to include season-aware
 * Toxic Grass spread within the Sulfur Dimension and season-affected spread
 * rates in the Overworld.
 *
 * Called from {@code ServerWorldMixin} on the random block tick.
 */
public class GrassSpreadSystem {

    private static final GrassSpreadSystem INSTANCE = new GrassSpreadSystem();

    public static GrassSpreadSystem getInstance() { return INSTANCE; }

    private GrassSpreadSystem() {}

    /**
     * Attempt to spread Toxic Grass or seasonal Overworld grass from {@code pos}.
     * Returns {@code true} if a spread event occurred.
     */
    public boolean trySpread(ServerWorld world, BlockPos pos, BlockState state, Random random) {
        Block block = state.getBlock();

        if (block == ModBlocks.TOXIC_GRASS) {
            return spreadToxicGrass(world, pos, random);
        }

        if (block == Blocks.GRASS_BLOCK) {
            return seasonalGrassSpread(world, pos, random);
        }

        return false;
    }

    // -------------------------------------------------------------------------
    // Toxic Grass spreading
    // -------------------------------------------------------------------------

    private boolean spreadToxicGrass(ServerWorld world, BlockPos pos, Random random) {
        // Toxic grass spreads slower than normal grass, but converts adjacent soil
        for (int i = 0; i < 4; i++) {
            BlockPos target = pos.add(
                    random.nextBetween(-3, 3),
                    random.nextBetween(-1, 1),
                    random.nextBetween(-3, 3));

            BlockState targetState = world.getBlockState(target);
            if (targetState.isOf(Blocks.DIRT) || targetState.isOf(Blocks.GRASS_BLOCK)) {
                BlockState above = world.getBlockState(target.up());
                if (above.isAir() || above.isTranslucent(world, target.up())) {
                    if (random.nextFloat() < 0.07f) {
                        world.setBlockState(target, ModBlocks.TOXIC_GRASS.getDefaultState());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // -------------------------------------------------------------------------
    // Season-modified grass spread
    // -------------------------------------------------------------------------

    private boolean seasonalGrassSpread(ServerWorld world, BlockPos pos, Random random) {
        SeasonManager.Season season = SeasonManager.getInstance().getCurrentSeason(world);
        float spreadChance = switch (season) {
            case SPRING -> 0.30f;  // Lush growth
            case SUMMER -> 0.20f;  // Normal
            case AUTUMN -> 0.10f;  // Slowing down
            case WINTER -> 0.02f;  // Almost no spread
        };

        if (random.nextFloat() > spreadChance) return false;

        BlockPos target = pos.add(
                random.nextBetween(-3, 3),
                random.nextBetween(-1, 1),
                random.nextBetween(-3, 3));

        BlockState targetState = world.getBlockState(target);
        if (targetState.isOf(Blocks.DIRT)) {
            BlockState above = world.getBlockState(target.up());
            if (above.isAir()) {
                world.setBlockState(target, Blocks.GRASS_BLOCK.getDefaultState());
                return true;
            }
        }
        return false;
    }
}
