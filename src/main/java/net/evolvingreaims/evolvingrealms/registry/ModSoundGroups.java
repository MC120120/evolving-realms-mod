package net.evolvingreaims.evolvingrealms.registry;

import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;

/**
 * Custom BlockSoundGroups for Evolving Realms blocks.
 */
public final class ModSoundGroups {

    public static final BlockSoundGroup SULFUR = new BlockSoundGroup(
            1.0f, 0.8f,
            ModSounds.SULFUR_BREAK,
            ModSounds.SULFUR_STEP,
            ModSounds.SULFUR_PLACE,
            ModSounds.SULFUR_BREAK,
            ModSounds.SULFUR_STEP);

    public static final BlockSoundGroup CRYSTAL = new BlockSoundGroup(
            0.9f, 1.3f,
            ModSounds.CRYSTAL_BREAK,
            ModSounds.CRYSTAL_STEP,
            ModSounds.CRYSTAL_PLACE,
            ModSounds.CRYSTAL_BREAK,
            ModSounds.CRYSTAL_STEP);

    public static final BlockSoundGroup ASH = new BlockSoundGroup(
            0.7f, 0.6f,
            ModSounds.ASH_BREAK,
            ModSounds.ASH_STEP,
            ModSounds.ASH_PLACE,
            ModSounds.ASH_BREAK,
            ModSounds.ASH_STEP);

    public static final BlockSoundGroup VOLCANIC = new BlockSoundGroup(
            1.2f, 0.7f,
            ModSounds.VOLCANIC_BREAK,
            ModSounds.VOLCANIC_STEP,
            ModSounds.VOLCANIC_PLACE,
            ModSounds.VOLCANIC_BREAK,
            ModSounds.VOLCANIC_STEP);

    private ModSoundGroups() {}
}
