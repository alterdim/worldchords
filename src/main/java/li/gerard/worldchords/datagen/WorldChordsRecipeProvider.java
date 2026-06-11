package li.gerard.worldchords.datagen;

import li.gerard.worldchords.WorldChords;
import li.gerard.worldchords.block.ModBlocks;
import li.gerard.worldchords.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WorldChordsRecipeProvider extends RecipeProvider {
    public WorldChordsRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    private void blastAndSmelt(List<ItemLike> smeltables, RecipeCategory category, CookingBookCategory cookingBookCategory, Item outputItem, float experience, int blasterTime, String group)
    {
        oreSmelting(smeltables, category, cookingBookCategory, outputItem, experience, blasterTime * 2, group);
        oreBlasting(smeltables, category, cookingBookCategory, outputItem, experience, blasterTime, group);

    }

    @Override
    protected void buildRecipes() {

        List<ItemLike> putridOres = List.of(ModBlocks.PUTRID_ORE_BLOCK.get());

        blastAndSmelt(putridOres, RecipeCategory.MISC, CookingBookCategory.BLOCKS, ModItems.PUTRID_INGOT.get(), 0.7F, 75, "test");

    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider provider, @NonNull RecipeOutput recipeOutput) {
            return new WorldChordsRecipeProvider(provider, recipeOutput);
        }

        @Override
        public @NonNull String getName() {
            return WorldChords.MODID + " Recipes";
        }
    }
}
