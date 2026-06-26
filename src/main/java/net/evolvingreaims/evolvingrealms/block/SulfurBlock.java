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
 * Sulfur Block — slightly toxic to breathe near; applies Nausea I on extended contact.
 */
public class SulfurBlock extends Block {

    public SulfurBlock(Settings settings) { super(settings); }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient() && entity instanceof LivingEntity living && world.getTime() % 40 == 0) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 60, 0, false, false));
        }
    }
}
