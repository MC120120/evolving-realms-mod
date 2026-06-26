package net.evolvingreaims.evolvingrealms.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.world.World;

/**
 * Volcanium Armor — when all 4 pieces are worn together the player gains
 * permanent Fire Resistance and a stacking Strength I bonus.
 */
public class VolcaniumArmorItem extends ArmorItem {

    private static final StatusEffectInstance FIRE_RESISTANCE =
            new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 20, 0, false, false, true);
    private static final StatusEffectInstance STRENGTH =
            new StatusEffectInstance(StatusEffects.STRENGTH, 20, 0, false, false, true);

    public VolcaniumArmorItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void inventoryTick(net.minecraft.item.ItemStack stack, World world, net.minecraft.entity.Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity player) {
            if (hasFullSet(player)) {
                player.addStatusEffect(FIRE_RESISTANCE);
                player.addStatusEffect(STRENGTH);
            }
        }
    }

    private boolean hasFullSet(PlayerEntity player) {
        for (net.minecraft.item.ItemStack armorStack : player.getArmorItems()) {
            if (!(armorStack.getItem() instanceof VolcaniumArmorItem)) {
                return false;
            }
        }
        return true;
    }
}
