package net.evolvingreaims.evolvingrealms.mixin;

import net.evolvingreaims.evolvingrealms.ai.adaptive.AdaptiveDifficultyManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Records bow kills on spiders for adaptive difficulty tracking.
 */
@Mixin(SpiderEntity.class)
public abstract class SpiderEntityMixin {

    @Inject(method = "damage", at = @At("TAIL"))
    private void evolvingrealms$trackKill(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && source.getAttacker() instanceof PlayerEntity player) {
            if (source.isIn(net.minecraft.registry.tag.DamageTypeTags.IS_PROJECTILE)) {
                AdaptiveDifficultyManager.INSTANCE.recordBowKill(player.getUuid());
            } else {
                AdaptiveDifficultyManager.INSTANCE.recordMeleeKill(player.getUuid());
            }
        }
    }
}
