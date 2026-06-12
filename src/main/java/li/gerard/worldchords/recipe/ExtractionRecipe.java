package li.gerard.worldchords.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

/**
 * Extraction: pull a result out of an input item, gated by a minimum crafting level.
 * Queried by extraction machines via
 * {@code serverLevel.recipeAccess().getRecipeFor(ModRecipes.EXTRACTION_TYPE.get(), input, level)};
 * in the crafting table the same extractions are available as datagen-emitted companion
 * shapeless recipes (input + needle of sufficient level), see the recipe provider.
 */
public class ExtractionRecipe implements Recipe<ExtractionRecipeInput> {

    public static final MapCodec<ExtractionRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(ExtractionRecipe::input),
                    Codec.INT.optionalFieldOf("level", 1).forGetter(ExtractionRecipe::craftingLevel),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(ExtractionRecipe::result))
            .apply(i, ExtractionRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ExtractionRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, ExtractionRecipe::input,
            ByteBufCodecs.VAR_INT, ExtractionRecipe::craftingLevel,
            ItemStackTemplate.STREAM_CODEC, ExtractionRecipe::result,
            ExtractionRecipe::new);

    private final Ingredient input;
    private final int craftingLevel;
    private final ItemStackTemplate result;

    public ExtractionRecipe(Ingredient input, int craftingLevel, ItemStackTemplate result) {
        this.input = input;
        this.craftingLevel = craftingLevel;
        this.result = result;
    }

    public Ingredient input() {
        return input;
    }

    /** Minimum crafting level of the needle or machine performing the extraction. */
    public int craftingLevel() {
        return craftingLevel;
    }

    public ItemStackTemplate result() {
        return result;
    }

    @Override
    public boolean matches(ExtractionRecipeInput input, Level level) {
        return input.toolLevel() >= craftingLevel && this.input.test(input.item());
    }

    @Override
    public ItemStack assemble(ExtractionRecipeInput input) {
        return result.create();
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<ExtractionRecipe> getSerializer() {
        return ModRecipes.EXTRACTION_SERIALIZER.get();
    }

    @Override
    public RecipeType<ExtractionRecipe> getType() {
        return ModRecipes.EXTRACTION_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        // never shown in the recipe book (display() is empty); the companion crafting
        // recipes carry their own category
        return RecipeBookCategories.CRAFTING_MISC;
    }
}
