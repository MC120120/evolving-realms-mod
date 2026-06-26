package net.evolvingreaims.evolvingrealms.registry;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public final class ModFoods {

    public static final FoodComponent SULFUR_MUSHROOM_STEW = new FoodComponent(
            8, 0.7f, false,
            java.util.List.of(
                new FoodComponent.StatusEffectEntry(new StatusEffectInstance(StatusEffects.NAUSEA, 80, 0), 0.3f),
                new FoodComponent.StatusEffectEntry(new StatusEffectInstance(StatusEffects.REGENERATION, 60, 1), 1.0f)
            )
    );

    public static final FoodComponent CRYSTAL_CANDY = new FoodComponent(
            4, 0.3f, false,
            java.util.List.of(
                new FoodComponent.StatusEffectEntry(new StatusEffectInstance(StatusEffects.SPEED, 200, 1), 1.0f),
                new FoodComponent.StatusEffectEntry(new StatusEffectInstance(StatusEffects.HASTE, 100, 0), 0.5f)
            )
    );

    private ModFoods() {}
}
