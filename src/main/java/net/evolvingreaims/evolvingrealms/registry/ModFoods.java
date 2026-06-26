package net.evolvingreaims.evolvingrealms.registry;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

/**
 * Food components for Evolving Realms consumables.
 */
public final class ModFoods {

    /** Sulfur Mushroom Stew — nourishing but briefly disorients. */
    public static final FoodComponent SULFUR_MUSHROOM_STEW = new FoodComponent.Builder()
            .nutrition(8)
            .saturationModifier(0.7f)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,       80, 0), 0.3f)
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 60, 1), 1.0f)
            .build();

    /** Crystal Candy — sweet and energising, grants brief Speed boost. */
    public static final FoodComponent CRYSTAL_CANDY = new FoodComponent.Builder()
            .nutrition(4)
            .saturationModifier(0.3f)
            .snack()
            .statusEffect(new StatusEffectInstance(StatusEffects.SPEED,  200, 1), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.HASTE,  100, 0), 0.5f)
            .build();

    private ModFoods() {}
}
