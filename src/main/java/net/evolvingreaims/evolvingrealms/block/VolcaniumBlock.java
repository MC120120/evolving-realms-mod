package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Block of Volcanium — radiates heat; gives Strength I to adjacent Burning Wolves.
 * Players standing on it gain Fire Resistance I for 3 seconds.
 */
public class VolcaniumBlock extends Block {

    public VolcaniumBlock(Settings settings) { super(settings); }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient() && entity instanceof LivingEntity living && world.getTime() % 20 == 0) {
            living.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                    net.minecraft.entity.effect.StatusEffects.FIRE_RESISTANCE, 60, 0, false, false));
        }
    }
}
