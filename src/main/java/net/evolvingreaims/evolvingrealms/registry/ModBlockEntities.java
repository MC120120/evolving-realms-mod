package net.evolvingreaims.evolvingrealms.registry;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Registers block entity types for Evolving Realms.
 */
public final class ModBlockEntities {

    // Crystal Shard Pillar stores stored energy (unused in MVP — reserved for future update)
    // public static final BlockEntityType<CrystalPillarBlockEntity> CRYSTAL_PILLAR =
    //         register("crystal_pillar", FabricBlockEntityTypeBuilder.create(CrystalPillarBlockEntity::new, ModBlocks.CRYSTAL_BRICKS).build());

    private static <T extends net.minecraft.block.entity.BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(EvolvingRealms.MOD_ID, name), type);
    }

    public static void register() {
        EvolvingRealms.LOGGER.debug("Registering block entities for Evolving Realms");
    }
}
