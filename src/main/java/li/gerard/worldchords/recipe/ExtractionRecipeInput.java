package li.gerard.worldchords.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

/**
 * Input for {@link ExtractionRecipe} lookups: the item being extracted from, plus the
 * crafting level of whatever performs the extraction (a needle in the crafting table,
 * an extraction machine later).
 */
public record ExtractionRecipeInput(ItemStack item, int toolLevel) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        if (index != 0) {
            throw new IllegalArgumentException("No item for index " + index);
        }
        return item;
    }

    @Override
    public int size() {
        return 1;
    }
}
