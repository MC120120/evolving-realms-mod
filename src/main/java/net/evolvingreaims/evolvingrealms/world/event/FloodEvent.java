package net.evolvingreaims.evolvingrealms.world.event;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

/**
 * FloodEvent — simulates a flash flood:
 * <ol>
 *   <li>Rapidly fills low-lying terrain with water.</li>
 *   <li>Washes away crops and loose blocks.</li>
 *   <li>Recedes after a duration.</li>
 * </ol>
 */
public class FloodEvent {

    public static final int PEAK_TICKS = 300;
    public static final int TOTAL_DURATION = 1200; // 60 seconds

    private final ServerWorld world;
    private final BlockPos source;
    private final int radius;
    private final List<BlockPos> floodedBlocks = new ArrayList<>();
    private int ticksElapsed = 0;
    private boolean receding = false;

    public FloodEvent(ServerWorld world, BlockPos source, int radius) {
        this.world = world;
        this.source = source;
        this.radius = radius;
    }

    public boolean isFinished() {
        return ticksElapsed >= TOTAL_DURATION;
    }

    public void tick(Random random) {
        ticksElapsed++;

        if (ticksElapsed == 1) {
            for (ServerPlayerEntity player : world.getPlayers()) {
                if (player.getBlockPos().isWithinDistance(source, radius * 3)) {
                    player.sendMessage(
                        Text.literal("§9§lFlood waters are rising!"), true);
                }
            }
        }

        if (ticksElapsed >= PEAK_TICKS && !receding) {
            receding = true;
        }

        if (!receding) {
            spreadWater(random);
        } else {
            recedeWater(random);
        }

        // Sound every 40 ticks
        if (ticksElapsed % 40 == 0) {
            world.playSound(null, source,
                SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, 1.5f, 0.8f);
        }
    }

    private void spreadWater(Random random) {
        int attempts = 8;
        for (int i = 0; i < attempts; i++) {
            int x = source.getX() + random.nextBetween(-radius, radius);
            int z = source.getZ() + random.nextBetween(-radius, radius);
            int y = world.getTopY(net.minecraft.world.Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);

            BlockPos surface = new BlockPos(x, y, z);

            // Only flood below the source elevation
            if (surface.getY() > source.getY() + 3) continue;

            BlockState state = world.getBlockState(surface);

            // Wash away crops and loose blocks
            if (state.isOf(Blocks.WHEAT) || state.isOf(Blocks.CARROTS)
                    || state.isOf(Blocks.POTATOES) || state.isOf(Blocks.BEETROOTS)) {
                world.setBlockState(surface, Blocks.WATER.getDefaultState());
                floodedBlocks.add(surface);
                continue;
            }

            if (state.isAir()) {
                BlockPos below = surface.down();
                if (!world.getBlockState(below).isAir()) {
                    world.setBlockState(surface, Blocks.WATER.getDefaultState());
                    floodedBlocks.add(surface);
                }
            }
        }
    }

    private void recedeWater(Random random) {
        if (floodedBlocks.isEmpty()) return;

        int toRecede = Math.min(4, floodedBlocks.size());
        for (int i = 0; i < toRecede; i++) {
            int idx = random.nextBetween(0, floodedBlocks.size() - 1);
            BlockPos pos = floodedBlocks.remove(idx);
            if (world.getBlockState(pos).isOf(Blocks.WATER)) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                // Leave mud behind
                if (random.nextFloat() < 0.4f) {
                    BlockPos below = pos.down();
                    if (world.getBlockState(below).isOf(Blocks.GRASS_BLOCK)
                            || world.getBlockState(below).isOf(Blocks.DIRT)) {
                        world.setBlockState(below, Blocks.MUD.getDefaultState());
                    }
                }
            }
        }
    }
}
