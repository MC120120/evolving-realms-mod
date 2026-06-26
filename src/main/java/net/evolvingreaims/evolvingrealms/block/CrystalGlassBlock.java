package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class CrystalGlassBlock extends Block {

    public CrystalGlassBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean allowsSpawning(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return false;
    }
}
