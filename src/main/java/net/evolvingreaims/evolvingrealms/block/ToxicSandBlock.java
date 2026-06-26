package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Toxic Sand — falls like sand, applies Poison I on contact.
 */
public class ToxicSandBlock extends FallingBlock {

    public ToxicSandBlock(Settings settings) { super(settings); }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient() && entity instanceof LivingEntity living && world.getTime() % 20 == 0) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 60, 0, false, true));
        }
    }

    @Override
    protected int getColor() { return 0x6BAA4B; }
}
