package net.evolvingreaims.evolvingrealms.item;

import net.evolvingreaims.evolvingrealms.registry.ModDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

/**
 * Sulfur Compass — right-click to display the direction and distance
 * to the nearest Sulfur Dimension portal or dungeon anchor.
 * While in the Sulfur Dimension it points toward the spawn portal home.
 */
public class SulfurCompassItem extends Item {

    public SulfurCompassItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient && user instanceof ServerPlayerEntity serverPlayer) {
            ServerWorld serverWorld = (ServerWorld) world;

            if (serverWorld.getRegistryKey().equals(ModDimensions.SULFUR_DIMENSION_KEY)) {
                // In Sulfur Dimension: point to world spawn (portal home)
                int spawnX = serverWorld.getSpawnPos().getX();
                int spawnZ = serverWorld.getSpawnPos().getZ();
                int dx = spawnX - (int) serverPlayer.getX();
                int dz = spawnZ - (int) serverPlayer.getZ();
                int dist = (int) Math.sqrt(dx * dx + dz * dz);
                String dir = getCardinal(dx, dz);
                serverPlayer.sendMessage(
                    Text.literal("§6[Sulfur Compass] §eHome portal is §f" + dist + "m §e" + dir), true);
            } else {
                serverPlayer.sendMessage(
                    Text.literal("§6[Sulfur Compass] §eCompass only active in the Sulfur Dimension or near a portal."), true);
            }
        }

        return ActionResult.SUCCESS;
    }

    private String getCardinal(int dx, int dz) {
        double angle = Math.toDegrees(Math.atan2(dz, dx));
        if (angle < 0) angle += 360;
        if (angle < 22.5 || angle >= 337.5) return "East";
        if (angle < 67.5) return "South-East";
        if (angle < 112.5) return "South";
        if (angle < 157.5) return "South-West";
        if (angle < 202.5) return "West";
        if (angle < 247.5) return "North-West";
        if (angle < 292.5) return "North";
        return "North-East";
    }
}
