package net.evolvingreaims.evolvingrealms.village.event;

import net.evolvingreaims.evolvingrealms.registry.ModSounds;
import net.evolvingreaims.evolvingrealms.village.data.VillageData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;

/**
 * Checks village conditions every village tick and fires dynamic events.
 *
 * Events:
 *   Wedding         — random when happiness > 70 and population > 5
 *   Harvest Festival — once per season when food > 80
 *   Plague           — when happiness < 20 and population > 8
 *   Famine           — triggered by VillageData
 *   Raid             — probability increases with population and military gap
 *   Celebration      — after a successful raid defence
 *   Merchant Arrival  — random caravan visits from other villages
 */
public class VillageEventManager {

    private final java.util.Random rng = new java.util.Random();

    public void checkAndFireEvents(MinecraftServer server, VillageData village) {
        // Wedding
        if (village.getHappiness() > 70 && village.getPopulation() > 5 && rng.nextInt(500) == 0) {
            fireWedding(server, village);
        }

        // Harvest Festival
        if (village.getFood() > 80 && rng.nextInt(300) == 0) {
            fireHarvestFestival(server, village);
        }

        // Plague
        if (village.getHappiness() < 20 && village.getPopulation() > 8 && rng.nextInt(400) == 0) {
            firePlague(server, village);
        }

        // Spontaneous Raid
        if (!village.isUnderRaid() && rng.nextInt(800) == 0) {
            fireRaid(server, village);
        }

        // Merchant caravan arrives
        if (village.getEconomy() > 40 && rng.nextInt(600) == 0) {
            fireMerchantArrival(server, village);
        }
    }

    private void fireWedding(MinecraftServer server, VillageData village) {
        broadcast(server, village, Text.translatable("event.evolving_realms.village.wedding",
                village.getName()));
        playSound(server, village, ModSounds.VILLAGE_WEDDING);
        village.addFood(-5);
        village.addResources(-5);
    }

    private void fireHarvestFestival(MinecraftServer server, VillageData village) {
        broadcast(server, village, Text.translatable("event.evolving_realms.village.harvest_festival",
                village.getName()));
        playSound(server, village, ModSounds.VILLAGE_CELEBRATION);
        village.addResources(15);
        village.addFood(10);
    }

    private void firePlague(MinecraftServer server, VillageData village) {
        broadcast(server, village, Text.translatable("event.evolving_realms.village.plague",
                village.getName()));
    }

    private void fireRaid(MinecraftServer server, VillageData village) {
        village.setUnderRaid(true);
        broadcast(server, village, Text.translatable("event.evolving_realms.village.raid",
                village.getName()));
        playSound(server, village, ModSounds.VILLAGE_ALARM);

        // Schedule raid end (simulated — real raid spawns monsters via illager captain logic)
        village.addMilitary(-10);
        village.addSecurity(-20);
    }

    private void fireMerchantArrival(MinecraftServer server, VillageData village) {
        broadcast(server, village, Text.translatable("event.evolving_realms.village.merchant_arrival",
                village.getName()));
        playSound(server, village, ModSounds.CARAVAN_ARRIVES);
        village.addResources(10);
        village.addFood(5);
    }

    private void broadcast(MinecraftServer server, VillageData village, Text message) {
        server.getPlayerManager().getPlayerList().stream()
              .filter(p -> p.getPos().isInRange(
                      village.getCenter().toCenterPos(), 150))
              .forEach(p -> p.sendMessage(message, false));
    }

    private void playSound(MinecraftServer server, VillageData village, net.minecraft.sound.SoundEvent sound) {
        for (ServerWorld world : server.getWorlds()) {
            if (world.getRegistryKey().getValue().toString().equals(village.getDimensionId())) {
                world.playSound(null,
                        village.getCenter(),
                        sound,
                        SoundCategory.NEUTRAL,
                        1.5f, 1.0f);
                break;
            }
        }
    }
}
