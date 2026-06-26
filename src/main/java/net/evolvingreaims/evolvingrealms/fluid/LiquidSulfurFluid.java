package net.evolvingreaims.evolvingrealms.fluid;

import net.evolvingreaims.evolvingrealms.registry.ModBlocks;
import net.evolvingreaims.evolvingrealms.registry.ModFluids;
import net.evolvingreaims.evolvingrealms.registry.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.fluid.FlowableFluid;

/**
 * Liquid Sulfur fluid — viscous (flow speed 2), glows faintly (luminance via block),
 * slows movement and applies Slowness I to players wading through it.
 */
public abstract class LiquidSulfurFluid extends FlowableFluid {

    @Override
    public Fluid getFlowing() { return ModFluids.LIQUID_SULFUR_FLOWING; }

    @Override
    public Fluid getStill() { return ModFluids.LIQUID_SULFUR_SOURCE; }

    @Override
    public Item getBucketItem() { return ModItems.LIQUID_SULFUR_BUCKET; }

    @Override
    protected boolean isInfinite(net.minecraft.world.World world) { return false; }

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == ModFluids.LIQUID_SULFUR_SOURCE || fluid == ModFluids.LIQUID_SULFUR_FLOWING;
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return ModBlocks.LIQUID_SULFUR_BLOCK.getDefaultState()
                .with(Properties.LEVEL_15, getBlockStateLevel(state));
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos,
                                         Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.matchesType(this);
    }

    @Override
    protected int getFlowSpeed(WorldAccess world) { return 2; } // slower than water

    @Override
    protected int getLevelDecreasePerBlock(WorldAccess world) { return 3; } // limited spread

    @Override
    public int getLevel(FluidState state) {
        return isStill(state) ? 8 : state.get(LEVEL);
    }

    @Override
    public boolean isStill(FluidState state) { return false; }

    public static class Source extends LiquidSulfurFluid {
        @Override public int getLevel(FluidState state) { return 8; }
        @Override public boolean isStill(FluidState state) { return true; }
    }

    public static class Flowing extends LiquidSulfurFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }
        @Override public int getLevel(FluidState state) { return state.get(LEVEL); }
        @Override public boolean isStill(FluidState state) { return false; }
    }
}
