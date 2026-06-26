package net.evolvingreaims.evolvingrealms.block;

import net.evolvingreaims.evolvingrealms.registry.ModFluids;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/**
 * Acid fluid block — damages living entities that stand in it.
 * Applies Poison II and Weakness I every 2 seconds.
 */
public class AcidBlock extends FluidBlock {

    public AcidBlock(AbstractBlock.Settings settings) {
        super(ModFluids.ACID_SOURCE, settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClient()) return;
        if (entity instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON,  40, 1, false, true));
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 60, 0, false, true));
            // Direct damage once per second
            if (world.getTime() % 20 == 0) {
                living.damage(world.getDamageSources().generic(), 2.0f);
            }
        }
    }
}
