package net.evolvingreaims.evolvingrealms.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

/**
 * Volcanium Sword: sets targets on fire for 5 seconds on hit and applies
 * a brief Wither I effect, representing the scorching volcanic essence.
 */
public class VolcaniumSwordItem extends SwordItem {

    public VolcaniumSwordItem(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setOnFireFor(5);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 60, 0));

        World world = target.getWorld();
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.FLAME,
                    target.getX(), target.getY() + 1.0, target.getZ(),
                    12, 0.3, 0.5, 0.3, 0.05);
        }

        return super.postHit(stack, target, attacker);
    }
}
