package li.gerard.worldchords.datagen;

import li.gerard.worldchords.WorldChords;
import li.gerard.worldchords.block.ModBlocks;
import li.gerard.worldchords.item.CraftingToolType;
import li.gerard.worldchords.item.CraftingTools;
import li.gerard.worldchords.item.MaterialItemType;
import li.gerard.worldchords.item.MaterialItems;
import li.gerard.worldchords.item.ModItems;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;
import java.util.Set;

/**
 * Generates blockstate definitions, block/item models and client item definitions
 */
public class WorldChordsModelProvider extends ModelProvider {
    /** Like {@link ModelTemplates#TWO_LAYERED_ITEM} but with handheld display transforms. */
    private static final ModelTemplate TWO_LAYERED_HANDHELD_ITEM = new ModelTemplate(
            Optional.of(Identifier.fromNamespaceAndPath("minecraft", "item/handheld")), Optional.empty(),
            TextureSlot.LAYER0, TextureSlot.LAYER1);

    /** Crafting tool types that have base+overlay art and use the tier-tinted model. */
    private static final Set<CraftingToolType> TINTED_TOOL_TYPES =
            Set.of(CraftingToolType.NEEDLE, CraftingToolType.SIPHON, CraftingToolType.RESONATOR);

    public WorldChordsModelProvider(PackOutput output) {
        super(output, WorldChords.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.createTrivialCube(ModBlocks.EXAMPLE_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.MURKY_ORE_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.SCULK_ALTAR_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.SCULK_DEVOURER.get());
        blockModels.createTrivialCube(ModBlocks.HUNGER_UPGRADE.get());

        createMurkyGrounds(blockModels);


        blockModels.createCrossBlock(ModBlocks.MURKY_PLANT.get(), BlockModelGenerators.PlantType.NOT_TINTED);
        blockModels.createCrossBlockWithDefaultItem(ModBlocks.SCULK_FLOWER.get(), BlockModelGenerators.PlantType.NOT_TINTED);


        itemModels.generateFlatItem(ModItems.EXAMPLE_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.MURKY_GEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.MURKY_THREAD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.PUTRID_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.PUTRID_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.PUTRID_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.PUTRID_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.PUTRID_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        for (var type : TINTED_TOOL_TYPES) {
            createTintedCraftingTool(itemModels, type);
        }
        for (var type : MaterialItemType.values()) {
            createTintedMaterialItem(itemModels, type);
        }
        // the rest still use flat per-material placeholder textures
        for (var material : CraftingTools.MATERIALS) {
            for (var type : CraftingToolType.values()) {
                if (!TINTED_TOOL_TYPES.contains(type)) {
                    itemModels.generateFlatItem(CraftingTools.get(material, type).get(), ModelTemplates.FLAT_HANDHELD_ITEM);
                }
            }
        }
    }

    /**
     * One shared two-layer model per tool type (grayscale {@code <type>_base} tinted by
     * the material's color, static {@code <type>_overlay} on top); each material
     * only gets its own client item definition carrying the tint.
     */
    private static void createTintedCraftingTool(ItemModelGenerators itemModels, CraftingToolType type) {
        var model = Identifier.fromNamespaceAndPath(WorldChords.MODID, "item/" + type.id());
        TWO_LAYERED_HANDHELD_ITEM.create(model, TextureMapping.layered(
                new Material(Identifier.fromNamespaceAndPath(WorldChords.MODID, "item/" + type.id() + "_base")),
                new Material(Identifier.fromNamespaceAndPath(WorldChords.MODID, "item/" + type.id() + "_overlay"))),
                itemModels.modelOutput);
        for (var material : CraftingTools.MATERIALS) {
            itemModels.itemModelOutput.accept(CraftingTools.get(material, type).get(),
                    ItemModelUtils.tintedModel(model, ItemModelUtils.constantTint(material.color())));
        }
    }

    /**
     * One shared flat model per material item type, tinted per material like the
     * crafting tools , a single grayscale layer for overlay-less types or base + overlay
     * Texture names come from {@link MaterialItemType#texture()}.
     */
    private static void createTintedMaterialItem(ItemModelGenerators itemModels, MaterialItemType type) {
        var model = Identifier.fromNamespaceAndPath(WorldChords.MODID, "item/" + type.id());
        if (type.hasOverlay()) {
            ModelTemplates.TWO_LAYERED_ITEM.create(model, TextureMapping.layered(
                    new Material(itemTexture(type.texture() + "_base")),
                    new Material(itemTexture(type.texture() + "_overlay"))),
                    itemModels.modelOutput);
        } else {
            ModelTemplates.FLAT_ITEM.create(model,
                    TextureMapping.layer0(new Material(itemTexture(type.texture()))), itemModels.modelOutput);
        }
        for (var material : CraftingTools.MATERIALS) {
            itemModels.itemModelOutput.accept(MaterialItems.get(material, type).get(),
                    ItemModelUtils.tintedModel(model, ItemModelUtils.constantTint(material.color())));
        }
    }

    private static Identifier itemTexture(String name) {
        return Identifier.fromNamespaceAndPath(WorldChords.MODID, "item/" + name);
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
