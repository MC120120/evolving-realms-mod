package net.evolvingreaims.evolvingrealms.mixin;

import net.evolvingreaims.evolvingrealms.ai.adaptive.AdaptiveDifficultyManager;
import net.evolvingreaims.evolvingrealms.ai.adaptive.goal.DodgeArrowGoal;
import net.evolvingreaims.evolvingrealms.ai.adaptive.goal.SurroundPlayerGoal;
import net.evolvingreaims.evolvingrealms.ai.adaptive.goal.BlockBreakingGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SpiderEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injects adaptive AI goals into vanilla hostile mobs.
 *
 * - Zombies:   SurroundPlayerGoal + BlockBreakingGoal
 * - Skeletons: DodgeArrowGoal
 * - Spiders:   DodgeArrowGoal
 *
 * The goals are priority-8 so they don't override the mob's own core goals.
 */
@Mixin(MobEntity.class)
public abstract class MobEntityMixin {

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void evolvingrealms$injectAdaptiveGoals(CallbackInfo ci) {
        MobEntity self = (MobEntity)(Object)this;

        if (self instanceof ZombieEntity) {
            self.goalSelector.add(8, new SurroundPlayerGoal(self));
            self.goalSelector.add(9, new BlockBreakingGoal(self));
        } else if (self instanceof SkeletonEntity) {
            self.goalSelector.add(8, new DodgeArrowGoal(self));
        } else if (self instanceof SpiderEntity) {
            self.goalSelector.add(8, new DodgeArrowGoal(self));
        }
    }
}
