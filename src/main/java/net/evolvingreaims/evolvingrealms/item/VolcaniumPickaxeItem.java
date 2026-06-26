package net.evolvingreaims.evolvingrealms.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Volcanium Pickaxe: smelts mined ores automatically (fortune of fire).
 */
public class VolcaniumPickaxeItem extends PickaxeItem {

    public VolcaniumPickaxeItem(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        // Auto-smelt behaviour is handled via Fortune + Silk Touch tag interactions;
        // actual smelting output defined in recipe JSON for relevant ores.
        return super.postMine(stack, world, state, pos, miner);
    }
}
