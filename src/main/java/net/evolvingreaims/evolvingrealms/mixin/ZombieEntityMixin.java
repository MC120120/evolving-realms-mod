package net.evolvingreaims.evolvingrealms.mixin;

import net.evolvingreaims.evolvingrealms.ai.adaptive.AdaptiveDifficultyManager;
import net.evolvingreaims.evolvingrealms.ai.adaptive.CombatProfile;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Records melee kills for the adaptive difficulty system when a zombie
 * deals the killing blow to a player (unlikely but possible when player
 * is the damage source — this records player kills on zombies instead).
 */
@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin {

    @Inject(method = "damage", at = @At("TAIL"))
    private void evolvingrealms$trackMeleeKill(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && source.getAttacker() instanceof PlayerEntity player) {
            if (!player.isUsingItem()) { // Not using a ranged weapon
                AdaptiveDifficultyManager.INSTANCE.recordMeleeKill(player.getUuid());
            }
        }
    }
}
