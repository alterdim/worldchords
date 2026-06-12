package li.gerard.worldchords.item;

import li.gerard.worldchords.tier.Tier;

/**
 * A crafting tool material: declaring one in {@link CraftingTools#MATERIALS} is all it
 * takes — one item per {@link CraftingToolType} is registered as
 * {@code <name>_<type>} with this durability, and the crafting level decides which
 * recipes the tools can perform. The tier provides the tooltip color.
 */
public record CraftingToolMaterial(String name, Tier tier, int durability, int craftingLevel) {
}
