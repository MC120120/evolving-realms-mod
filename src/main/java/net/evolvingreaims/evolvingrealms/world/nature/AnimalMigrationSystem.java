package net.evolvingreaims.evolvingrealms.world.nature;

import net.evolvingreaims.evolvingrealms.world.season.SeasonManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

import java.util.List;

/**
 * AnimalMigrationSystem — each server tick (throttled) moves passive animal
 * groups toward more favourable biomes based on the current season.
 * <ul>
 *   <li>Spring: Animals near the centre of loaded chunks, normal behaviour.</li>
 *   <li>Summer: Animals drift toward water sources.</li>
 *   <li>Autumn: Animals cluster, eating before winter.</li>
 *   <li>Winter: Animals slow down; some despawn if far from player.</li>
 * </ul>
 */
public class AnimalMigrationSystem {

    private static final AnimalMigrationSystem INSTANCE = new AnimalMigrationSystem();
    public static AnimalMigrationSystem getInstance() { return INSTANCE; }

    private static final int TICK_INTERVAL = 400; // every 20 seconds
    private int ticker = 0;

    private AnimalMigrationSystem() {}

    public void tick(ServerWorld world, Random random) {
        ticker++;
        if (ticker < TICK_INTERVAL) return;
        ticker = 0;

        SeasonManager.Season season = SeasonManager.getInstance().getCurrentSeason(world);

        for (net.minecraft.server.network.ServerPlayerEntity player : world.getPlayers()) {
            BlockPos center = player.getBlockPos();
            Box searchBox = Box.of(Vec3d.of(center), 128, 64, 128);

            List<AnimalEntity> animals = world.getEntitiesByClass(AnimalEntity.class, searchBox, a -> true);

            for (AnimalEntity animal : animals) {
                applyMigration(world, animal, season, random);
            }
        }
    }

    private void applyMigration(ServerWorld world, AnimalEntity animal,
                                  SeasonManager.Season season, Random random) {
        switch (season) {
            case SUMMER -> {
                // Drift toward water — nudge velocity slightly toward a water-bearing direction
                BlockPos animalPos = animal.getBlockPos();
                for (int dx = -20; dx <= 20; dx += 4) {
                    for (int dz = -20; dz <= 20; dz += 4) {
                        BlockPos check = animalPos.add(dx, 0, dz);
                        if (world.getBlockState(check).isOf(net.minecraft.block.Blocks.WATER)) {
                            Vec3d drift = new Vec3d(dx * 0.001, 0, dz * 0.001);
                            animal.addVelocity(drift.x, 0, drift.z);
                            return;
                        }
                    }
                }
            }
            case WINTER -> {
                // Slow animals slightly
                animal.addVelocity(-animal.getVelocity().x * 0.05,
                        0, -animal.getVelocity().z * 0.05);

                // Chance of adding Slowness
                if (random.nextFloat() < 0.01f) {
                    animal.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                            net.minecraft.entity.effect.StatusEffects.SLOWNESS, 200, 0));
                }
            }
            case AUTUMN -> {
                // Animals eat grass blocks at their feet
                BlockPos feet = animal.getBlockPos();
                if (world.getBlockState(feet).isOf(net.minecraft.block.Blocks.GRASS_BLOCK)
                        && random.nextFloat() < 0.005f) {
                    world.setBlockState(feet, net.minecraft.block.Blocks.DIRT.getDefaultState());
                    animal.heal(1.0f);
                }
            }
            default -> { /* Spring — normal behaviour */ }
        }
    }
}
