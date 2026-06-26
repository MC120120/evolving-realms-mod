package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/**
 * Acid Stone — stone corroded by acid. Has a small chance to crumble into gravel each random tick.
 */
public class AcidStoneBlock extends Block {

    public AcidStoneBlock(Settings settings) {
        super(settings.ticksRandomly());
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextInt(200) == 0) {
            world.setBlockState(pos, net.minecraft.block.Blocks.GRAVEL.getDefaultState());
        }
    }
}
