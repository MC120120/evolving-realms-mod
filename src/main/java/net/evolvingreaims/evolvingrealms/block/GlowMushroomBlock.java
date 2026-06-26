package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

/**
 * Glow Mushroom — emits light level 12. Can grow on any solid surface in the Sulfur Dimension.
 */
public class GlowMushroomBlock extends MushroomPlantBlock {

    public GlowMushroomBlock(Settings settings) {
        super(null, settings); // No configured feature — grows manually via bonemeal
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isSolidSurface(world, pos, net.minecraft.util.math.Direction.UP)
                || super.canPlantOnTop(floor, world, pos);
    }
}
