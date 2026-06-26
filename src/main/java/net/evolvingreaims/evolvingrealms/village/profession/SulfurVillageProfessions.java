package net.evolvingreaims.evolvingrealms.village.profession;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;

/**
 * Custom villager professions for the Evolving Realms living-civilisation system.
 */
public final class SulfurVillageProfessions {

    /** Builder: constructs village structures using {@code BuilderVillagerTask}. */
    public static final VillagerProfession BUILDER = register("builder",
            VillagerProfession.IS_ACQUIRABLE_JOB_SITE,
            VillagerProfession.IS_ACQUIRABLE_JOB_SITE,
            null, null,
            SoundEvents.ENTITY_VILLAGER_WORK_TOOLSMITH);

    /** Alchemist: brews Sulfur potions and trades exotic reagents. */
    public static final VillagerProfession ALCHEMIST = register("alchemist",
            VillagerProfession.IS_ACQUIRABLE_JOB_SITE,
            VillagerProfession.IS_ACQUIRABLE_JOB_SITE,
            null, null,
            SoundEvents.ENTITY_VILLAGER_WORK_CLERIC);

    /** Scout: patrols village perimeter and detects threats early. */
    public static final VillagerProfession SCOUT = register("scout",
            VillagerProfession.IS_ACQUIRABLE_JOB_SITE,
            VillagerProfession.IS_ACQUIRABLE_JOB_SITE,
            null, null,
            SoundEvents.ENTITY_VILLAGER_WORK_WEAPONSMITH);

    private static VillagerProfession register(
            String name,
            java.util.function.Predicate<net.minecraft.block.entity.BlockEntityType<?>> workStation,
            java.util.function.Predicate<net.minecraft.block.entity.BlockEntityType<?>> secondary,
            com.google.common.collect.ImmutableSet<net.minecraft.item.Item> gatherItems,
            com.google.common.collect.ImmutableSet<net.minecraft.block.Block> secondaryPOI,
            net.minecraft.sound.SoundEvent workSound) {
        return Registry.register(
                Registries.VILLAGER_PROFESSION,
                Identifier.of(EvolvingRealms.MOD_ID, name),
                new VillagerProfession(name, workStation, secondary,
                        gatherItems != null ? gatherItems : com.google.common.collect.ImmutableSet.of(),
                        secondaryPOI != null ? secondaryPOI : com.google.common.collect.ImmutableSet.of(),
                        workSound));
    }

    public static void register() {
        EvolvingRealms.LOGGER.debug("Registering villager professions for Evolving Realms");
    }
}
