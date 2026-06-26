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
import net.minecraft.particle.ParticleTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * EarthquakeEvent — a seismic event that:
 * <ol>
 *   <li>Shakes nearby players (adds velocity jitter).</li>
 *   <li>Cracks stone/deepslate blocks into cobblestone variants.</li>
 *   <li>Causes cave-ins (drops gravel/sand above underground voids).</li>
 *   <li>Opens fissures (2-3 block wide cracks in the terrain).</li>
 * </ol>
 * Triggered by {@code LivingEarthManager} based on regional tectonic stress.
 */
public class EarthquakeEvent {

    public static final int DURATION_TICKS = 200; // 10 seconds

    private final ServerWorld world;
    private final BlockPos epicentre;
    private final int magnitude; // 1-5
    private int ticksRemaining;

    public EarthquakeEvent(ServerWorld world, BlockPos epicentre, int magnitude) {
        this.world = world;
        this.epicentre = epicentre;
        this.magnitude = Math.max(1, Math.min(5, magnitude));
        this.ticksRemaining = DURATION_TICKS;
    }

    public boolean isFinished() {
        return ticksRemaining <= 0;
    }

    public void tick(Random random) {
        ticksRemaining--;

        int radius = magnitude * 12;

        // Shake players
        for (ServerPlayerEntity player : world.getPlayers()) {
            if (player.getBlockPos().isWithinDistance(epicentre, radius * 2)) {
                float intensity = 1.0f - (float) player.getBlockPos().getManhattanDistance(epicentre) / (radius * 2);
                player.addVelocity(
                    (random.nextFloat() - 0.5f) * 0.3f * intensity,
                    random.nextFloat() * 0.1f * intensity,
                    (random.nextFloat() - 0.5f) * 0.3f * intensity);
                player.velocityModified = true;

                if (ticksRemaining == DURATION_TICKS - 1) {
                    player.sendMessage(Text.literal("§6§lThe earth trembles! (Magnitude " + magnitude + ")"), true);
                }
            }
        }

        // Crack terrain every 10 ticks
        if (ticksRemaining % 10 == 0) {
            crackTerrain(random, radius);
        }

        // Sound
        if (ticksRemaining % 20 == 0) {
            world.playSound(null, epicentre,
                SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.BLOCKS,
                1.5f + magnitude * 0.2f, 0.4f + random.nextFloat() * 0.2f);
        }

        // Dust particles
        if (random.nextFloat() < 0.4f) {
            BlockPos dustPos = epicentre.add(
                random.nextBetween(-radius, radius),
                random.nextBetween(0, 20),
                random.nextBetween(-radius, radius));
            world.spawnParticles(
                new net.minecraft.particle.BlockStateParticleEffect(ParticleTypes.FALLING_DUST, Blocks.GRAVEL.getDefaultState()),
                dustPos.getX(), dustPos.getY(), dustPos.getZ(),
                5, 0.5, 0.2, 0.5, 0.01);
        }
    }

    private void crackTerrain(Random random, int radius) {
        int attempts = magnitude * 3;
        for (int i = 0; i < attempts; i++) {
            int x = epicentre.getX() + random.nextBetween(-radius, radius);
            int z = epicentre.getZ() + random.nextBetween(-radius, radius);

            // Find surface
            int y = world.getTopY(net.minecraft.world.Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    x, z);

            BlockPos surface = new BlockPos(x, y - 1, z);
            BlockState state = world.getBlockState(surface);

            if (state.isOf(Blocks.STONE)) {
                world.setBlockState(surface, Blocks.COBBLESTONE.getDefaultState());
            } else if (state.isOf(Blocks.DEEPSLATE)) {
                world.setBlockState(surface, Blocks.COBBLED_DEEPSLATE.getDefaultState());
            } else if (state.isOf(Blocks.GRASS_BLOCK)) {
                // Cave-in: if there's air below, drop dirt
                BlockPos below = surface.down(2);
                if (world.getBlockState(below).isAir()) {
                    world.setBlockState(surface, Blocks.AIR.getDefaultState());
                    world.setBlockState(below, Blocks.DIRT.getDefaultState());
                }
            }

            // Fissures on high magnitude earthquakes
            if (magnitude >= 4 && random.nextFloat() < 0.1f) {
                openFissure(x, y, z);
            }
        }
    }

    private void openFissure(int x, int surfaceY, int z) {
        int depth = magnitude * 4 + world.getRandom().nextBetween(2, 8);
        for (int dy = 0; dy < depth; dy++) {
            world.setBlockState(new BlockPos(x, surfaceY - dy - 1, z), Blocks.AIR.getDefaultState());
            // Width 1-2
            if (magnitude >= 5) {
                world.setBlockState(new BlockPos(x + 1, surfaceY - dy - 1, z), Blocks.AIR.getDefaultState());
            }
        }
    }
}
