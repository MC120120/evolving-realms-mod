package net.evolvingreaims.evolvingrealms.mixin;

import net.evolvingreaims.evolvingrealms.village.VillageManager;
import net.evolvingreaims.evolvingrealms.village.data.VillageData;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks into VillagerEntity to update the nearest VillageData
 * when a villager restocks or completes a trade.
 */
@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin {

    @Inject(method = "restock", at = @At("TAIL"))
    private void evolvingrealms$onRestock(CallbackInfo ci) {
        VillagerEntity self = (VillagerEntity)(Object)this;
        if (self.getWorld().isClient()) return;

        MinecraftServer server = self.getServer();
        if (server == null) return;

        // Find the nearest village and update its economy
        VillageManager.INSTANCE.getVillages().stream()
                .filter(v -> v.getCenter().isWithinDistance(self.getBlockPos(), 80))
                .findFirst()
                .ifPresent(v -> v.addResources(2));
    }
}
