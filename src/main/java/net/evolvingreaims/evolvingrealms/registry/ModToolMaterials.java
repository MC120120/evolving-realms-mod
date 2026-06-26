package net.evolvingreaims.evolvingrealms.registry;

import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;

/**
 * Custom tool materials for Evolving Realms.
 *
 * Tier comparison (for reference):
 *   Wood:      durability=59,  speed=2.0, damage=0.0, enchantability=15
 *   Stone:     durability=131, speed=4.0, damage=1.0, enchantability=5
 *   Iron:      durability=250, speed=6.0, damage=2.0, enchantability=14
 *   Diamond:   durability=1561,speed=8.0, damage=3.0, enchantability=10
 *   Netherite: durability=2031,speed=9.0, damage=4.0, enchantability=15
 */
public final class ModToolMaterials {

    /**
     * Pyrite — mid-tier, between Iron and Diamond.
     * Fast mining, moderate durability, good enchantability.
     */
    public static final ToolMaterial PYRITE = new ToolMaterial(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL, // mines same level as diamond
            800,           // durability
            7.5f,          // mining speed
            2.5f,          // attack damage bonus
            16,            // enchantability
            Ingredient.ofItems(ModItems.PYRITE_INGOT)
    );

    /**
     * Volcanium — top-tier, exceeds Netherite.
     * Extremely fast, high damage, high durability, low enchantability (dense metal).
     */
    public static final ToolMaterial VOLCANIUM = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, // mines same level as netherite
            2800,          // durability
            11.0f,         // mining speed
            5.0f,          // attack damage bonus
            8,             // enchantability (heavy, hard to enchant)
            Ingredient.ofItems(ModItems.VOLCANIUM_INGOT)
    );

    private ModToolMaterials() {}
}
