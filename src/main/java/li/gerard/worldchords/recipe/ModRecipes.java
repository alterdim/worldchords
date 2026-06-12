package li.gerard.worldchords.recipe;

import li.gerard.worldchords.WorldChords;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, WorldChords.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, WorldChords.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<ExtractionRecipe>> EXTRACTION_TYPE =
            RECIPE_TYPES.register("extraction",
                    () -> RecipeType.simple(Identifier.fromNamespaceAndPath(WorldChords.MODID, "extraction")));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ExtractionRecipe>> EXTRACTION_SERIALIZER =
            RECIPE_SERIALIZERS.register("extraction",
                    () -> new RecipeSerializer<>(ExtractionRecipe.MAP_CODEC, ExtractionRecipe.STREAM_CODEC));

    private ModRecipes() {}

    public static void register(IEventBus modEventBus) {
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
    }
}
