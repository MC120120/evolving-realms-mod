package net.evolvingreaims.evolvingrealms.registry;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.entity.mob.*;
import net.evolvingreaims.evolvingrealms.entity.boss.AncientSulfurTitanEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModEntities {

    public static final EntityType<SulfurSlimeEntity> SULFUR_SLIME = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "sulfur_slime"),
            EntityType.Builder.<SulfurSlimeEntity>create(SulfurSlimeEntity::new, SpawnGroup.MONSTER)
                    .dimensions(1.0f, 1.0f).trackRangeBlocks(80).build());

    public static final EntityType<AcidSpiderEntity> ACID_SPIDER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "acid_spider"),
            EntityType.Builder.<AcidSpiderEntity>create(AcidSpiderEntity::new, SpawnGroup.MONSTER)
                    .dimensions(1.4f, 0.9f).trackRangeBlocks(80).build());

    public static final EntityType<CrystalGolemEntity> CRYSTAL_GOLEM = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "crystal_golem"),
            EntityType.Builder.<CrystalGolemEntity>create(CrystalGolemEntity::new, SpawnGroup.MONSTER)
                    .dimensions(1.4f, 2.9f).trackRangeBlocks(80).build());

    public static final EntityType<ToxicSkeletonEntity> TOXIC_SKELETON = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "toxic_skeleton"),
            EntityType.Builder.<ToxicSkeletonEntity>create(ToxicSkeletonEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6f, 1.99f).trackRangeBlocks(80).build());

    public static final EntityType<AshZombieEntity> ASH_ZOMBIE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "ash_zombie"),
            EntityType.Builder.<AshZombieEntity>create(AshZombieEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6f, 1.95f).trackRangeBlocks(80).build());

    public static final EntityType<LavaWormEntity> LAVA_WORM = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "lava_worm"),
            EntityType.Builder.<LavaWormEntity>create(LavaWormEntity::new, SpawnGroup.MONSTER)
                    .dimensions(2.0f, 0.8f).trackRangeBlocks(80).build());

    public static final EntityType<CrystalBeetleEntity> CRYSTAL_BEETLE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "crystal_beetle"),
            EntityType.Builder.<CrystalBeetleEntity>create(CrystalBeetleEntity::new, SpawnGroup.MONSTER)
                    .dimensions(1.0f, 0.6f).trackRangeBlocks(64).build());

    public static final EntityType<SulfurPhantomEntity> SULFUR_PHANTOM = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "sulfur_phantom"),
            EntityType.Builder.<SulfurPhantomEntity>create(SulfurPhantomEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.9f, 0.5f).trackRangeBlocks(96).build());

    public static final EntityType<AcidFishEntity> ACID_FISH = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "acid_fish"),
            EntityType.Builder.<AcidFishEntity>create(AcidFishEntity::new, SpawnGroup.WATER_CREATURE)
                    .dimensions(0.5f, 0.3f).trackRangeBlocks(48).build());

    public static final EntityType<BurningWolfEntity> BURNING_WOLF = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "burning_wolf"),
            EntityType.Builder.<BurningWolfEntity>create(BurningWolfEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6f, 0.85f).trackRangeBlocks(80).build());

    public static final EntityType<AncientSulfurTitanEntity> ANCIENT_SULFUR_TITAN = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EvolvingRealms.MOD_ID, "ancient_sulfur_titan"),
            EntityType.Builder.<AncientSulfurTitanEntity>create(AncientSulfurTitanEntity::new, SpawnGroup.MONSTER)
                    .dimensions(4.0f, 6.0f).trackRangeBlocks(160).build());

    public static void register() {
        EvolvingRealms.LOGGER.debug("Registering entities for Evolving Realms");
    }
}
