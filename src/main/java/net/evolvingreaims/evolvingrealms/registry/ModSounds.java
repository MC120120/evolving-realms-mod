package net.evolvingreaims.evolvingrealms.registry;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/**
 * Registers all custom sounds for Evolving Realms.
 * Sound files live in src/main/resources/assets/evolving_realms/sounds/
 */
public final class ModSounds {

    // -------------------------------------------------------------------------
    // Ambient / Dimension
    // -------------------------------------------------------------------------
    public static final SoundEvent SULFUR_DIMENSION_AMBIENT = register("ambient.sulfur_dimension");
    public static final SoundEvent SULFUR_WIND              = register("ambient.sulfur_wind");
    public static final SoundEvent ACID_BUBBLING            = register("ambient.acid_bubbling");
    public static final SoundEvent CRYSTAL_RESONANCE        = register("ambient.crystal_resonance");
    public static final SoundEvent VOLCANIC_RUMBLE          = register("ambient.volcanic_rumble");

    // -------------------------------------------------------------------------
    // Music
    // -------------------------------------------------------------------------
    public static final SoundEvent MUSIC_SULFUR_DIMENSION   = register("music.sulfur_dimension");
    public static final SoundEvent MUSIC_BOSS_TITAN         = register("music.boss.ancient_sulfur_titan");
    public static final SoundEvent MUSIC_TITAN_PHASE_2      = register("music.boss.titan_phase_2");

    // -------------------------------------------------------------------------
    // Block Sounds
    // -------------------------------------------------------------------------
    public static final SoundEvent SULFUR_BREAK  = register("block.sulfur.break");
    public static final SoundEvent SULFUR_PLACE  = register("block.sulfur.place");
    public static final SoundEvent SULFUR_STEP   = register("block.sulfur.step");
    public static final SoundEvent CRYSTAL_BREAK = register("block.crystal.break");
    public static final SoundEvent CRYSTAL_PLACE = register("block.crystal.place");
    public static final SoundEvent CRYSTAL_STEP  = register("block.crystal.step");
    public static final SoundEvent ASH_BREAK     = register("block.ash.break");
    public static final SoundEvent ASH_PLACE     = register("block.ash.place");
    public static final SoundEvent ASH_STEP      = register("block.ash.step");
    public static final SoundEvent VOLCANIC_BREAK = register("block.volcanic.break");
    public static final SoundEvent VOLCANIC_PLACE = register("block.volcanic.place");
    public static final SoundEvent VOLCANIC_STEP  = register("block.volcanic.step");
    public static final SoundEvent ACID_BURN      = register("block.acid.burn");

    // -------------------------------------------------------------------------
    // Sulfur Slime
    // -------------------------------------------------------------------------
    public static final SoundEvent SULFUR_SLIME_AMBIENT = register("entity.sulfur_slime.ambient");
    public static final SoundEvent SULFUR_SLIME_HURT    = register("entity.sulfur_slime.hurt");
    public static final SoundEvent SULFUR_SLIME_DEATH   = register("entity.sulfur_slime.death");
    public static final SoundEvent SULFUR_SLIME_JUMP    = register("entity.sulfur_slime.jump");
    public static final SoundEvent SULFUR_SLIME_SQUISH  = register("entity.sulfur_slime.squish");

    // -------------------------------------------------------------------------
    // Acid Spider
    // -------------------------------------------------------------------------
    public static final SoundEvent ACID_SPIDER_AMBIENT = register("entity.acid_spider.ambient");
    public static final SoundEvent ACID_SPIDER_HURT    = register("entity.acid_spider.hurt");
    public static final SoundEvent ACID_SPIDER_DEATH   = register("entity.acid_spider.death");
    public static final SoundEvent ACID_SPIDER_SPIT    = register("entity.acid_spider.spit");

    // -------------------------------------------------------------------------
    // Crystal Golem
    // -------------------------------------------------------------------------
    public static final SoundEvent CRYSTAL_GOLEM_AMBIENT = register("entity.crystal_golem.ambient");
    public static final SoundEvent CRYSTAL_GOLEM_HURT    = register("entity.crystal_golem.hurt");
    public static final SoundEvent CRYSTAL_GOLEM_DEATH   = register("entity.crystal_golem.death");
    public static final SoundEvent CRYSTAL_GOLEM_STEP    = register("entity.crystal_golem.step");
    public static final SoundEvent CRYSTAL_GOLEM_SHATTER = register("entity.crystal_golem.shatter");

    // -------------------------------------------------------------------------
    // Toxic Skeleton
    // -------------------------------------------------------------------------
    public static final SoundEvent TOXIC_SKELETON_AMBIENT = register("entity.toxic_skeleton.ambient");
    public static final SoundEvent TOXIC_SKELETON_HURT    = register("entity.toxic_skeleton.hurt");
    public static final SoundEvent TOXIC_SKELETON_DEATH   = register("entity.toxic_skeleton.death");
    public static final SoundEvent TOXIC_SKELETON_SHOOT   = register("entity.toxic_skeleton.shoot");

    // -------------------------------------------------------------------------
    // Ash Zombie
    // -------------------------------------------------------------------------
    public static final SoundEvent ASH_ZOMBIE_AMBIENT = register("entity.ash_zombie.ambient");
    public static final SoundEvent ASH_ZOMBIE_HURT    = register("entity.ash_zombie.hurt");
    public static final SoundEvent ASH_ZOMBIE_DEATH   = register("entity.ash_zombie.death");

    // -------------------------------------------------------------------------
    // Lava Worm
    // -------------------------------------------------------------------------
    public static final SoundEvent LAVA_WORM_AMBIENT  = register("entity.lava_worm.ambient");
    public static final SoundEvent LAVA_WORM_HURT     = register("entity.lava_worm.hurt");
    public static final SoundEvent LAVA_WORM_DEATH    = register("entity.lava_worm.death");
    public static final SoundEvent LAVA_WORM_BURROW   = register("entity.lava_worm.burrow");
    public static final SoundEvent LAVA_WORM_EMERGE   = register("entity.lava_worm.emerge");
    public static final SoundEvent LAVA_WORM_SHOOT    = register("entity.lava_worm.shoot");

    // -------------------------------------------------------------------------
    // Sulfur Phantom
    // -------------------------------------------------------------------------
    public static final SoundEvent SULFUR_PHANTOM_AMBIENT = register("entity.sulfur_phantom.ambient");
    public static final SoundEvent SULFUR_PHANTOM_HURT    = register("entity.sulfur_phantom.hurt");
    public static final SoundEvent SULFUR_PHANTOM_DEATH   = register("entity.sulfur_phantom.death");
    public static final SoundEvent SULFUR_PHANTOM_BITE    = register("entity.sulfur_phantom.bite");

    // -------------------------------------------------------------------------
    // Burning Wolf
    // -------------------------------------------------------------------------
    public static final SoundEvent BURNING_WOLF_AMBIENT = register("entity.burning_wolf.ambient");
    public static final SoundEvent BURNING_WOLF_HURT    = register("entity.burning_wolf.hurt");
    public static final SoundEvent BURNING_WOLF_DEATH   = register("entity.burning_wolf.death");
    public static final SoundEvent BURNING_WOLF_GROWL   = register("entity.burning_wolf.growl");

    // -------------------------------------------------------------------------
    // Ancient Sulfur Titan (Boss)
    // -------------------------------------------------------------------------
    public static final SoundEvent TITAN_AMBIENT        = register("entity.ancient_sulfur_titan.ambient");
    public static final SoundEvent TITAN_HURT           = register("entity.ancient_sulfur_titan.hurt");
    public static final SoundEvent TITAN_DEATH          = register("entity.ancient_sulfur_titan.death");
    public static final SoundEvent TITAN_ROAR           = register("entity.ancient_sulfur_titan.roar");
    public static final SoundEvent TITAN_GROUND_SLAM    = register("entity.ancient_sulfur_titan.ground_slam");
    public static final SoundEvent TITAN_LAVA_EXPLOSION = register("entity.ancient_sulfur_titan.lava_explosion");
    public static final SoundEvent TITAN_CRYSTAL_SPIKE  = register("entity.ancient_sulfur_titan.crystal_spike");
    public static final SoundEvent TITAN_FIRE_BREATH    = register("entity.ancient_sulfur_titan.fire_breath");
    public static final SoundEvent TITAN_PHASE_CHANGE   = register("entity.ancient_sulfur_titan.phase_change");
    public static final SoundEvent TITAN_RAGE           = register("entity.ancient_sulfur_titan.rage");
    public static final SoundEvent TITAN_SUMMON_MINION  = register("entity.ancient_sulfur_titan.summon_minion");
    public static final SoundEvent TITAN_STEP           = register("entity.ancient_sulfur_titan.step");

    // -------------------------------------------------------------------------
    // Village Events
    // -------------------------------------------------------------------------
    public static final SoundEvent VILLAGE_CELEBRATION  = register("event.village.celebration");
    public static final SoundEvent VILLAGE_WEDDING       = register("event.village.wedding");
    public static final SoundEvent VILLAGE_ALARM         = register("event.village.alarm");
    public static final SoundEvent CARAVAN_ARRIVES       = register("event.caravan.arrives");

    // -------------------------------------------------------------------------
    // Living Earth
    // -------------------------------------------------------------------------
    public static final SoundEvent EARTHQUAKE           = register("event.earth.earthquake");
    public static final SoundEvent VOLCANO_ERUPT        = register("event.earth.volcano_erupt");
    public static final SoundEvent WILDFIRE_SPREAD      = register("event.earth.wildfire");
    public static final SoundEvent TREE_FALL            = register("event.earth.tree_fall");

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------
    private static SoundEvent register(String name) {
        Identifier id = Identifier.of(EvolvingRealms.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void register() {
        EvolvingRealms.LOGGER.debug("Registering sounds for Evolving Realms");
    }
}
