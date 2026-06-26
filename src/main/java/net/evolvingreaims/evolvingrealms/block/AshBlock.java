package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Ash Block — soft, muffles sounds. Applies Slowness I when walked on.
 * Ash Zombies regenerate HP when standing on ash.
 */
public class AshBlock extends Block {

    public AshBlock(Settings settings) { super(settings); }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient() && entity instanceof LivingEntity living && world.getTime() % 30 == 0) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 0, false, false));
        }
    }
}
