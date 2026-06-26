package net.evolvingreaims.evolvingrealms.mixin;

import net.evolvingreaims.evolvingrealms.ai.adaptive.AdaptiveDifficultyManager;
import net.evolvingreaims.evolvingrealms.ai.adaptive.CombatProfile;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Makes Creepers rush faster when the player has BOW_DOMINANT profile
 * (creepers exploit the time between ranged shots).
 */
@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void evolvingrealms$tickAdaptive(CallbackInfo ci) {
        CreeperEntity self = (CreeperEntity)(Object)this;
        if (self.getWorld().isClient()) return;

        PlayerEntity target = self.getWorld().getClosestPlayer(self, 24.0);
        if (target == null) return;

        CombatProfile profile = AdaptiveDifficultyManager.INSTANCE.getProfile(target.getUuid());
        if (profile == CombatProfile.BOW_DOMINANT) {
            // Increase fuse speed when player is a bow user (creeper exploits reload time)
            self.getAttributeInstance(net.minecraft.entity.attribute.EntityAttributes.GENERIC_MOVEMENT_SPEED)
                .setBaseValue(0.30); // slightly faster than default 0.25
        } else {
            self.getAttributeInstance(net.minecraft.entity.attribute.EntityAttributes.GENERIC_MOVEMENT_SPEED)
                .setBaseValue(0.25);
        }
    }
}
