package net.evolvingreaims.evolvingrealms.mixin;

import net.evolvingreaims.evolvingrealms.ai.adaptive.AdaptiveDifficultyManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Records bow kills when a player kills a Skeleton (proxy for ranged-dominant play style).
 */
@Mixin(SkeletonEntity.class)
public abstract class SkeletonEntityMixin {

    @Inject(method = "damage", at = @At("TAIL"))
    private void evolvingrealms$trackBowKill(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && source.getAttacker() instanceof PlayerEntity player) {
            if (source.isIn(net.minecraft.registry.tag.DamageTypeTags.IS_PROJECTILE)) {
                AdaptiveDifficultyManager.INSTANCE.recordBowKill(player.getUuid());
            }
        }
    }
}
