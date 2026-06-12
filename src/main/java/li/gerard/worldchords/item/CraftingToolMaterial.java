package li.gerard.worldchords.item;

import li.gerard.worldchords.tier.Tier;

/**
 * A crafting tool material: declaring one in {@link CraftingTools#MATERIALS} is all it
 * takes — one item per {@link CraftingToolType} is registered as
 * {@code <name>_<type>} with this durability, and the crafting level decides which
 * recipes the tools can perform. The color tints all of the material's item textures
 * and its tooltip values.
 */
public record CraftingToolMaterial(String name, Tier tier, int color, int durability, int craftingLevel) {
}
