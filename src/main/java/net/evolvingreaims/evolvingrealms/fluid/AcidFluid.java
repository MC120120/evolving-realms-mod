package net.evolvingreaims.evolvingrealms.fluid;

import net.evolvingreaims.evolvingrealms.registry.ModBlocks;
import net.evolvingreaims.evolvingrealms.registry.ModFluids;
import net.evolvingreaims.evolvingrealms.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public abstract class AcidFluid extends FlowableFluid {

    @Override
    public Fluid getFlowing() { return ModFluids.ACID_FLOWING; }

    @Override
    public Fluid getStill() { return ModFluids.ACID_SOURCE; }

    @Override
    public Item getBucketItem() { return ModItems.ACID_BUCKET; }

    @Override
    protected boolean isInfinite(net.minecraft.world.World world) { return false; }

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == ModFluids.ACID_SOURCE || fluid == ModFluids.ACID_FLOWING;
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return ModBlocks.ACID_BLOCK.getDefaultState()
                .with(Properties.LEVEL_15, getBlockStateLevel(state));
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos,
                                         Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.matchesType(this);
    }

    @Override
    protected int getFlowSpeed(WorldAccess world) { return 4; }

    public static class Source extends AcidFluid {
        @Override public int getLevel(FluidState state) { return 8; }
        @Override public boolean isStill(FluidState state) { return true; }
        @Override protected int getLevelDecreasePerBlock(WorldAccess world) { return 1; }
    }

    public static class Flowing extends AcidFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }
        @Override public int getLevel(FluidState state) { return state.get(LEVEL); }
        @Override public boolean isStill(FluidState state) { return false; }
        @Override protected int getLevelDecreasePerBlock(WorldAccess world) { return 2; }
    }
}
