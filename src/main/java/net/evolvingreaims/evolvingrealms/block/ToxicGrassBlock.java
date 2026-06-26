package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

/**
 * Toxic Grass — tall, yellow-green tinted grass that thrives in acidic soil.
 */
public class ToxicGrassBlock extends PlantBlock {

    public ToxicGrassBlock(Settings settings) { super(settings); }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(net.evolvingreaims.evolvingrealms.registry.ModBlocks.SULFURIC_DIRT)
            || floor.isOf(net.evolvingreaims.evolvingrealms.registry.ModBlocks.TOXIC_SAND)
            || floor.isOf(net.evolvingreaims.evolvingrealms.registry.ModBlocks.ASH_BLOCK)
            || super.canPlantOnTop(floor, world, pos);
    }
}
