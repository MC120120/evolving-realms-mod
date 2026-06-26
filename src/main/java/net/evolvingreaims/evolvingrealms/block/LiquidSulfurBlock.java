package net.evolvingreaims.evolvingrealms.block;

import net.evolvingreaims.evolvingrealms.registry.ModFluids;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Liquid Sulfur fluid block — viscous; applies Slowness I on contact.
 */
public class LiquidSulfurBlock extends FluidBlock {

    public LiquidSulfurBlock(AbstractBlock.Settings settings) {
        super(ModFluids.LIQUID_SULFUR_SOURCE, settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClient()) return;
        if (entity instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 0, false, true));
        }
    }
}
