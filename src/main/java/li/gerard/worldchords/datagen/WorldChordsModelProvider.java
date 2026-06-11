package li.gerard.worldchords.datagen;

import li.gerard.worldchords.WorldChords;
import li.gerard.worldchords.block.ModBlocks;
import li.gerard.worldchords.item.ModItems;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

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

        itemModels.generateFlatItem(ModItems.EXAMPLE_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.PUTRID_INGOT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.MURKY_GEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.PUTRID_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.PUTRID_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.PUTRID_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.PUTRID_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
    }
}
