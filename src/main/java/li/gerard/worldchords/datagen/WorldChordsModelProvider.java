package li.gerard.worldchords.datagen;

import li.gerard.worldchords.WorldChords;
import li.gerard.worldchords.block.ModBlocks;
import li.gerard.worldchords.item.ModItems;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;

/**
 * Generates blockstate definitions, block/item models and client item definitions
 */
public class WorldChordsModelProvider extends ModelProvider {
    public WorldChordsModelProvider(PackOutput output) {
        super(output, WorldChords.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.createTrivialCube(ModBlocks.EXAMPLE_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.PUTRID_ORE_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.SCULK_ALTAR_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.SCULK_DEVOURER.get());

        createMurkyGrounds(blockModels);


        blockModels.createCrossBlock(ModBlocks.MURKY_PLANT.get(), BlockModelGenerators.PlantType.NOT_TINTED);
        blockModels.createCrossBlockWithDefaultItem(ModBlocks.SCULK_FLOWER.get(), BlockModelGenerators.PlantType.NOT_TINTED);


        itemModels.generateFlatItem(ModItems.EXAMPLE_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.PUTRID_INGOT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.MURKY_GEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.MURKY_THREAD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.PUTRID_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.PUTRID_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.PUTRID_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.PUTRID_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.PUTRID_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
    }

    private static void createMurkyGrounds(BlockModelGenerators blockModels) {
        var block = ModBlocks.MURKY_GROUNDS.get();
        var textures = new TextureMapping()
                .put(TextureSlot.DIRT, TextureMapping.getBlockTexture(Blocks.DIRT))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block));
        var model = BlockModelGenerators.plainVariant(ModelTemplates.FARMLAND.create(block, textures, blockModels.modelOutput));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, model));
    }
}
