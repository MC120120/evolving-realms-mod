package net.evolvingreaims.evolvingrealms.registry;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.entity.mob.*;
import net.evolvingreaims.evolvingrealms.entity.boss.AncientSulfurTitanEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Registers all custom entity types for Evolving Realms.
 */
public final class ModEntities {

    public static final EntityType<SulfurSlimeEntity> SULFUR_SLIME = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "sulfur_slime"),
            FabricEntityTypeBuilder.<SulfurSlimeEntity>create(SpawnGroup.MONSTER, SulfurSlimeEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0f, 1.0f))
                    .trackRangeBlocks(80)
                    .build());

    public static final EntityType<AcidSpiderEntity> ACID_SPIDER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "acid_spider"),
            FabricEntityTypeBuilder.<AcidSpiderEntity>create(SpawnGroup.MONSTER, AcidSpiderEntity::new)
                    .dimensions(EntityDimensions.fixed(1.4f, 0.9f))
                    .trackRangeBlocks(80)
                    .build());

    public static final EntityType<CrystalGolemEntity> CRYSTAL_GOLEM = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "crystal_golem"),
            FabricEntityTypeBuilder.<CrystalGolemEntity>create(SpawnGroup.MONSTER, CrystalGolemEntity::new)
                    .dimensions(EntityDimensions.fixed(1.4f, 2.9f))
                    .trackRangeBlocks(80)
                    .build());

    public static final EntityType<ToxicSkeletonEntity> TOXIC_SKELETON = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "toxic_skeleton"),
            FabricEntityTypeBuilder.<ToxicSkeletonEntity>create(SpawnGroup.MONSTER, ToxicSkeletonEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.99f))
                    .trackRangeBlocks(80)
                    .build());

    public static final EntityType<AshZombieEntity> ASH_ZOMBIE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "ash_zombie"),
            FabricEntityTypeBuilder.<AshZombieEntity>create(SpawnGroup.MONSTER, AshZombieEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                    .trackRangeBlocks(80)
                    .build());

    public static final EntityType<LavaWormEntity> LAVA_WORM = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "lava_worm"),
            FabricEntityTypeBuilder.<LavaWormEntity>create(SpawnGroup.MONSTER, LavaWormEntity::new)
                    .dimensions(EntityDimensions.fixed(2.0f, 0.8f))
                    .trackRangeBlocks(80)
                    .build());

    public static final EntityType<CrystalBeetleEntity> CRYSTAL_BEETLE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "crystal_beetle"),
            FabricEntityTypeBuilder.<CrystalBeetleEntity>create(SpawnGroup.MONSTER, CrystalBeetleEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0f, 0.6f))
                    .trackRangeBlocks(64)
                    .build());

    public static final EntityType<SulfurPhantomEntity> SULFUR_PHANTOM = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "sulfur_phantom"),
            FabricEntityTypeBuilder.<SulfurPhantomEntity>create(SpawnGroup.MONSTER, SulfurPhantomEntity::new)
                    .dimensions(EntityDimensions.fixed(0.9f, 0.5f))
                    .trackRangeBlocks(96)
                    .build());

    public static final EntityType<AcidFishEntity> ACID_FISH = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "acid_fish"),
            FabricEntityTypeBuilder.<AcidFishEntity>create(SpawnGroup.WATER_CREATURE, AcidFishEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.3f))
                    .trackRangeBlocks(48)
                    .build());

    public static final EntityType<BurningWolfEntity> BURNING_WOLF = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "burning_wolf"),
            FabricEntityTypeBuilder.<BurningWolfEntity>create(SpawnGroup.MONSTER, BurningWolfEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 0.85f))
                    .trackRangeBlocks(80)
                    .build());

    public static final EntityType<AncientSulfurTitanEntity> ANCIENT_SULFUR_TITAN = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "ancient_sulfur_titan"),
            FabricEntityTypeBuilder.<AncientSulfurTitanEntity>create(SpawnGroup.MONSTER, AncientSulfurTitanEntity::new)
                    .dimensions(EntityDimensions.fixed(4.0f, 6.0f))
                    .trackRangeBlocks(160)
                    .trackedUpdateRate(2)
                    .build());

    public static void register() {
        EvolvingRealms.LOGGER.debug("Registering entities for Evolving Realms");
    }
}
