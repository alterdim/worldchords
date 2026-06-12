package li.gerard.worldchords.datagen;

import li.gerard.worldchords.WorldChords;
import li.gerard.worldchords.block.ModBlocks;
import li.gerard.worldchords.item.*;
import li.gerard.worldchords.recipe.ExtractionRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredItem;
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

    /**
     * Declares an extraction. Emits two recipes from the one declaration: the
     * machine-facing {@code worldchords:extraction} recipe, and a companion shapeless
     * crafting recipe (input + any needle of crafting level >= {@code level}) so the
     * same extraction works in the crafting table. The needle survives with +1 damage
     * via {@code CraftingToolItem#getCraftingRemainder}.
     */
    private void extraction(ItemLike input, int level, ItemLike result, int count) {
        String name = getItemName(result) + "_from_extraction";
        this.output.accept(
                ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(WorldChords.MODID, "extraction/" + name)),
                new ExtractionRecipe(Ingredient.of(input), level, new ItemStackTemplate(result.asItem(), count)),
                null);

        Item[] needles = CraftingTools.withLevelAtLeast(CraftingToolType.NEEDLE, level)
                .stream().map(DeferredItem::get).toArray(Item[]::new);
        this.shapeless(RecipeCategory.MISC, result, count)
                .requires(input)
                .requires(Ingredient.of(needles))
                .unlockedBy(getHasName(input), this.has(input))
                .save(this.output, WorldChords.MODID + ":extraction/" + name + "_crafting");
    }

    @Override
    protected void buildRecipes() {

        List<ItemLike> murkyOres = List.of(ModBlocks.MURKY_ORE_BLOCK.get());

        blastAndSmelt(murkyOres, RecipeCategory.MISC, CookingBookCategory.BLOCKS,
                MaterialItems.get("murky", MaterialItemType.INGOT).get(), 0.7F, 75, "murky_ingot");

        // every material smelts its dust into its ingot
        for (var material : CraftingTools.MATERIALS) {
            var dust = MaterialItems.get(material, MaterialItemType.DUST).get();
            var ingot = MaterialItems.get(material, MaterialItemType.INGOT).get();
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(dust), RecipeCategory.MISC, CookingBookCategory.MISC,
                            new ItemStackTemplate(ingot), 0.7F, 200)
                    .group(material.name() + "_ingot")
                    .unlockedBy(getHasName(dust), this.has(dust))
                    .save(this.output, WorldChords.MODID + ":" + material.name() + "_ingot_from_smelting_dust");
        }

        extraction(ModBlocks.SCULK_FLOWER.get(), 2, MaterialItems.get("murky", MaterialItemType.EXTRACT), 2);


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
