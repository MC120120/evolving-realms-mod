package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Hardened Lava — solidified lava rock. Emits a faint heat glow (luminance 3).
 * Gives anyone standing on it Fire Resistance I for 1 second.
 */
public class HardenedLavaBlock extends Block {

    public HardenedLavaBlock(Settings settings) { super(settings); }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient() && entity instanceof net.minecraft.entity.LivingEntity living && world.getTime() % 20 == 0) {
            living.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                    net.minecraft.entity.effect.StatusEffects.FIRE_RESISTANCE, 25, 0, false, false));
        }
    }
}
