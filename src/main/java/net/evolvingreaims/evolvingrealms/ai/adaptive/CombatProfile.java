package net.evolvingreaims.evolvingrealms.ai.adaptive;

/**
 * Describes the dominant combat strategy a player has been using.
 * Mob AI goals read this value to select counter-strategies.
 */
public enum CombatProfile {
    /** No clear dominant strategy yet. Mobs behave normally. */
    DEFAULT,

    /**
     * Player kills primarily with bows / crossbows.
     * Counter: skeletons dodge, zombies raise shields, spiders zig-zag,
     *          creepers rush while the player is reloading.
     */
    BOW_DOMINANT,

    /**
     * Player kills primarily in melee.
     * Counter: enemies maintain distance, creepers coordinate attacks,
     *          zombies try to surround, skeletons stay protected behind allies.
     */
    MELEE_DOMINANT,

    /**
     * Player frequently places blocks during combat (pillaring/towering).
     * Counter: zombies try to stack-climb, some mobs break blocks,
     *          improved climbing pathfinding.
     */
    BUILDER,

    /**
     * Player sleeps very often.
     * Counter: night events become stronger; phantoms arrive earlier.
     */
    SLEEPER,

    /**
     * Player primarily farms / interacts with animals.
     * Counter: wildlife behaves naturally; passive mob aggression increases
     *          slightly to add tension.
     */
    FARMER
}
