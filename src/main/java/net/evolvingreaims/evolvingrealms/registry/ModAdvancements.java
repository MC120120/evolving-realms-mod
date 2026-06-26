package net.evolvingreaims.evolvingrealms.registry;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;

/**
 * Advancement registration hook for Evolving Realms.
 *
 * Advancement JSON files live in:
 *   data/evolving_realms/advancements/
 *
 * This class is kept minimal — Fabric loads advancements automatically
 * from the data pack. We only need a registration hook for any
 * code-driven criterion triggers we add in the future.
 */
public final class ModAdvancements {

    public static void register() {
        EvolvingRealms.LOGGER.debug("Advancements loaded from data pack for Evolving Realms.");
    }
}
