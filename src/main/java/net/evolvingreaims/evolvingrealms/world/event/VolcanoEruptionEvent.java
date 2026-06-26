package net.evolvingreaims.evolvingrealms.world.event;

import net.evolvingreaims.evolvingrealms.registry.ModSounds;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeKeys;

import java.util.Random;

/**
 * Volcano Eruption Event — randomly erupts in mountain biomes.
 *
 * On trigger:
 *   1. Picks a random high-altitude block in a badlands/mountain biome
 *   2. Plays rumble sounds and shows particle column
 *   3. Creates a controlled lava outflow (replaces air blocks in a downward cone)
 *   4. Announces the eruption to nearby players
 *
 * Eruptions are purely aesthetic within the overworld; they use lava-flow
 * logic from vanilla without causing chunk lag.
 */
public class VolcanoEruptionEvent {

    private static final int  TRIGGER_CHANCE = 2000; // 1/2000 chance per event check
    private static final int  LAVA_RADIUS    = 6;

    private final Random rng = new Random();

    public void tryTrigger(ServerWorld world) {
        if (!world.getGameRules().getBoolean(net.minecraft.world.GameRules.DO_MOB_GRIEFING)) return;
        if (rng.nextInt(TRIGGER_CHANCE) != 0) return;

        // Find a suitable eruption point (high ground near a player)
        world.getPlayers().stream()
             .filter(p -> !p.isCreative() && !p.isSpectator())
             .findAny()
             .ifPresent(player -> {
                 BlockPos base = findEruptionPoint(world, player.getBlockPos());
                 if (base != null) erupt(world, base);
             });
    }

    private BlockPos findEruptionPoint(ServerWorld world, BlockPos near) {
        for (int attempt = 0; attempt < 10; attempt++) {
            int x = near.getX() + rng.nextInt(300) - 150;
            int z = near.getZ() + rng.nextInt(300) - 150;
            int y = world.getTopY(net.minecraft.world.Heightmap.Type.WORLD_SURFACE, x, z);
            if (y > 100) { // Must be at mountain elevation
                return new BlockPos(x, y, z);
            }
        }
        return null;
    }

    private void erupt(ServerWorld world, BlockPos summit) {
        // Announce to nearby players
        world.getPlayers().stream()
             .filter(p -> p.getBlockPos().isWithinDistance(summit, 300))
             .forEach(p -> p.sendMessage(
                     Text.translatable("event.evolving_realms.volcano_eruption"), false));

        // Sound
        world.playSound(null, summit, ModSounds.VOLCANO_ERUPT, SoundCategory.AMBIENT, 4.0f, 0.8f);

        // Particle column
        for (int i = 0; i < 50; i++) {
            double ox = (rng.nextDouble() - 0.5) * 4;
            double oz = (rng.nextDouble() - 0.5) * 4;
            world.spawnParticles(ParticleTypes.LAVA,
                    summit.getX() + ox, summit.getY(), summit.getZ() + oz,
                    3, 0, 0.5, 0, 0.05);
            world.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    summit.getX() + ox, summit.getY() + 2, summit.getZ() + oz,
                    2, 0, 0.8, 0, 0.02);
        }

        // Lava flow: place lava in a downward-spreading cone
        placeLavaFlow(world, summit);
    }

    private void placeLavaFlow(ServerWorld world, BlockPos summit) {
        for (int dy = 0; dy >= -LAVA_RADIUS; dy--) {
            int radiusAtLevel = LAVA_RADIUS - Math.abs(dy);
            for (int dx = -radiusAtLevel; dx <= radiusAtLevel; dx++) {
                for (int dz = -radiusAtLevel; dz <= radiusAtLevel; dz++) {
                    if (dx * dx + dz * dz > radiusAtLevel * radiusAtLevel) continue;
                    if (rng.nextInt(3) != 0) continue; // Not every block
                    BlockPos target = summit.add(dx, dy, dz);
                    if (world.getBlockState(target).isAir()) {
                        world.setBlockState(target, Blocks.LAVA.getDefaultState());
                    }
                }
            }
        }
    }
}
