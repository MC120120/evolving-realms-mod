package net.evolvingreaims.evolvingrealms.item;

import net.evolvingreaims.evolvingrealms.registry.ModDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

/**
 * Dimension Key — right-click while on solid ground to open a rift to the
 * Sulfur Dimension (or back to the Overworld if already there). One-use item.
 */
public class DimensionKeyItem extends Item {

    public DimensionKeyItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient && user instanceof ServerPlayerEntity serverPlayer) {
            ServerWorld currentWorld = (ServerWorld) world;

            ServerWorld targetWorld;
            if (currentWorld.getRegistryKey().equals(ModDimensions.SULFUR_DIMENSION_KEY)) {
                targetWorld = serverPlayer.getServer().getOverworld();
            } else {
                targetWorld = serverPlayer.getServer().getWorld(ModDimensions.SULFUR_DIMENSION_KEY);
            }

            if (targetWorld != null) {
                BlockPos targetPos = targetWorld.getSpawnPos().up();
                serverPlayer.teleportTo(new TeleportTarget(
                    targetWorld,
                    targetPos.toCenterPos(),
                    net.minecraft.util.math.Vec3d.ZERO,
                    serverPlayer.getYaw(),
                    serverPlayer.getPitch(),
                    TeleportTarget.NO_OP));

                world.playSound(null, user.getBlockPos(),
                    SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.PLAYERS, 1.0f, 1.0f);

                serverPlayer.sendMessage(Text.literal("§5The Dimension Key shatters as reality bends..."), false);

                if (!serverPlayer.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
            }
        }

        return ActionResult.SUCCESS;
    }
}
