package net.evolvingreaims.evolvingrealms.item;

import net.evolvingreaims.evolvingrealms.entity.boss.AncientSulfurTitanEntity;
import net.evolvingreaims.evolvingrealms.registry.ModDimensions;
import net.evolvingreaims.evolvingrealms.registry.ModEntities;
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
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Titan Summoning Idol — right-click in the Sulfur Dimension to summon the
 * Ancient Sulfur Titan boss. One-use, crumbles after summon.
 * Cannot be used in the Overworld.
 */
public class TitanSummoningIdolItem extends Item {

    public TitanSummoningIdolItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient) {
            if (!world.getRegistryKey().equals(ModDimensions.SULFUR_DIMENSION_KEY)) {
                user.sendMessage(Text.literal("§cThe Idol only resonates within the Sulfur Dimension!"), true);
                return TypedActionResult.fail(stack);
            }

            ServerWorld serverWorld = (ServerWorld) world;
            BlockPos summonPos = user.getBlockPos().add(0, 1, 0);

            AncientSulfurTitanEntity titan = ModEntities.ANCIENT_SULFUR_TITAN.create(serverWorld);
            if (titan != null) {
                titan.refreshPositionAndAngles(summonPos.getX() + 0.5,
                        summonPos.getY(), summonPos.getZ() + 0.5, 0, 0);
                titan.initialize(serverWorld, serverWorld.getLocalDifficulty(summonPos),
                        net.minecraft.entity.SpawnReason.TRIGGERED, null);
                serverWorld.spawnEntity(titan);

                // Visual + audio fanfare
                serverWorld.playSound(null, summonPos,
                        SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.HOSTILE, 1.5f, 0.8f);
                serverWorld.spawnParticles(ParticleTypes.EXPLOSION_EMITTER,
                        summonPos.getX(), summonPos.getY() + 3, summonPos.getZ(),
                        4, 1.5, 1.5, 1.5, 0.0);

                serverWorld.getPlayers().forEach(p ->
                        p.sendMessage(Text.literal("§4§lThe Ancient Sulfur Titan has awakened!"), false));
            }

            if (!user.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }

        return TypedActionResult.success(stack, world.isClient());
    }
}
