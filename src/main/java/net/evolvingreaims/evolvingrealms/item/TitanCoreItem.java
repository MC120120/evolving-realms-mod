package net.evolvingreaims.evolvingrealms.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * Titan Core — legendary drop from the Ancient Sulfur Titan. Can be used as a
 * crafting ingredient for the ultimate Titan Beacon, or right-clicked to gain
 * a 30-second burst of Strength III + Resistance II.
 */
public class TitanCoreItem extends Item {

    public TitanCoreItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient) {
            user.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                    net.minecraft.entity.effect.StatusEffects.STRENGTH, 600, 2));
            user.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                    net.minecraft.entity.effect.StatusEffects.RESISTANCE, 600, 1));
            user.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                    net.minecraft.entity.effect.StatusEffects.FIRE_RESISTANCE, 600, 0));

            user.sendMessage(Text.literal("§4The Titan's essence surges through you!"), true);

            user.getItemCooldownManager().set(stack.getItem(), 6000); // 5-minute cooldown
        }

        return TypedActionResult.success(stack, world.isClient());
    }
}
