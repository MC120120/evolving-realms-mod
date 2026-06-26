package net.evolvingreaims.evolvingrealms.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;

/**
 * Volcanium Axe: ignites targets on hit.
 */
public class VolcaniumAxeItem extends AxeItem {

    public VolcaniumAxeItem(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setOnFireFor(8);
        return super.postHit(stack, target, attacker);
    }
}
