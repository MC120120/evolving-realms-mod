package net.evolvingreaims.evolvingrealms.world.event;

import net.evolvingreaims.evolvingrealms.registry.ModSounds;
import net.evolvingreaims.evolvingrealms.world.season.SeasonManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

/**
 * Wildfire Event — spreads fire through flammable biomes during Summer.
 *
 * The fire starts at a random surface block with adjacent flammable wood/leaves
 * and uses a BFS capped at MAX_BLOCKS to spread realistically, skipping water
 * and stone. It respects MOB_GRIEFING.
 */
public class WildfireEvent {

    private static final int TRIGGER_CHANCE = 1500;
    private static final int MAX_BLOCKS     = 80;

    private final Random rng = new Random();

    public void tryTrigger(ServerWorld world, SeasonManager.Season season) {
        if (season != SeasonManager.Season.SUMMER) return;
        if (!world.getGameRules().getBoolean(net.minecraft.world.GameRules.DO_FIRE_TICK)) return;
        if (!world.getGameRules().getBoolean(net.minecraft.world.GameRules.DO_MOB_GRIEFING)) return;
        if (rng.nextInt(TRIGGER_CHANCE) != 0) return;

        world.getPlayers().stream()
             .filter(p -> !p.isCreative() && !p.isSpectator())
             .findAny()
             .ifPresent(player -> {
                 BlockPos ignition = findIgnitionPoint(world, player.getBlockPos());
                 if (ignition != null) startWildfire(world, ignition);
             });
    }

    private BlockPos findIgnitionPoint(ServerWorld world, BlockPos near) {
        for (int attempt = 0; attempt < 20; attempt++) {
            int x = near.getX() + rng.nextInt(200) - 100;
            int z = near.getZ() + rng.nextInt(200) - 100;
            int y = world.getTopY(net.minecraft.world.Heightmap.Type.WORLD_SURFACE, x, z);
            BlockPos candidate = new BlockPos(x, y, z);
            BlockState below   = world.getBlockState(candidate.down());
            if (below.isOf(Blocks.GRASS_BLOCK) || below.isOf(Blocks.DIRT)) {
                // Check for flammable neighbours
                for (BlockPos adj : BlockPos.iterateOutwards(candidate, 2, 1, 2)) {
                    BlockState s = world.getBlockState(adj);
                    if (s.isOf(Blocks.OAK_LEAVES) || s.isOf(Blocks.OAK_LOG) ||
                        s.isOf(Blocks.BIRCH_LEAVES) || s.isOf(Blocks.ACACIA_LOG)) {
                        return candidate;
                    }
                }
            }
        }
        return null;
    }

    private void startWildfire(ServerWorld world, BlockPos origin) {
        world.getPlayers().stream()
             .filter(p -> p.getBlockPos().isWithinDistance(origin, 200))
             .forEach(p -> p.sendMessage(
                     Text.translatable("event.evolving_realms.wildfire"), false));
        world.playSound(null, origin, ModSounds.WILDFIRE_SPREAD, SoundCategory.AMBIENT, 3.0f, 1.0f);

        // BFS fire spread
        Deque<BlockPos> queue = new ArrayDeque<>();
        queue.add(origin);
        int count = 0;

        while (!queue.isEmpty() && count < MAX_BLOCKS) {
            BlockPos pos = queue.poll();
            for (BlockPos adj : new BlockPos[]{pos.north(), pos.south(), pos.east(), pos.west(), pos.up()}) {
                BlockState state = world.getBlockState(adj);
                if (state.isOf(Blocks.OAK_LEAVES) || state.isOf(Blocks.OAK_LOG) ||
                    state.isOf(Blocks.BIRCH_LEAVES) || state.isOf(Blocks.SPRUCE_LOG) ||
                    state.isOf(Blocks.GRASS_BLOCK)  || state.isOf(Blocks.SHORT_GRASS)) {
                    if (rng.nextInt(3) != 0) {
                        world.setBlockState(adj, Blocks.FIRE.getDefaultState());
                        queue.add(adj);
                        count++;
                    }
                }
            }
        }
    }
}
