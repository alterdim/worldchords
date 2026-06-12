package li.gerard.worldchords.datagen;

import li.gerard.worldchords.WorldChords;
import li.gerard.worldchords.block.ModBlocks;
import li.gerard.worldchords.item.CraftingToolType;
import li.gerard.worldchords.item.CraftingTools;
import li.gerard.worldchords.item.MaterialItemType;
import li.gerard.worldchords.item.MaterialItems;
import li.gerard.worldchords.item.ModItems;
import li.gerard.worldchords.tier.Tier;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

/**
 * Generates {@code en_us.json}. Material items, crafting tools and tiers are named
 * automatically from their ids ({@code berserkium_needle} -> "Berserkium Needle");
 * everything else is declared here. There is no handwritten lang file — new manual
 * entries go in {@link #addTranslations()}.
 */
public class WorldChordsLanguageProvider extends LanguageProvider {

    public WorldChordsLanguageProvider(PackOutput output) {
        super(output, WorldChords.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // auto-named from registry ids: every material's tools and items, and the tiers
        for (var material : CraftingTools.MATERIALS) {
            for (var type : CraftingToolType.values()) {
                addItem(CraftingTools.get(material, type), titleCase(material.name() + "_" + type.id()));
            }
            for (var type : MaterialItemType.values()) {
                addItem(MaterialItems.get(material, type), titleCase(material.name() + "_" + type.id()));
            }
        }
        for (var tier : Tier.values()) {
            add("worldchords.tier." + tier.id(), titleCase(tier.id()));
        }

        add("itemGroup.worldchords", "Example Mod Tab");

        addBlock(ModBlocks.EXAMPLE_BLOCK, "Example Block");
        addBlock(ModBlocks.MURKY_ORE_BLOCK, "Murky Ore");
        addBlock(ModBlocks.MURKY_GROUNDS, "Murky Grounds");
        addBlock(ModBlocks.MURKY_PLANT, "Murky Plant");
        addBlock(ModBlocks.SCULK_FLOWER, "Sculk Flower");
        addBlock(ModBlocks.SCULK_ALTAR_BLOCK, "Sculk Altar");
        addBlock(ModBlocks.SCULK_DEVOURER, "Sculk Devourer");
        addBlock(ModBlocks.HUNGER_UPGRADE, "Hunger Upgrade");

        addItem(ModItems.EXAMPLE_ITEM, "Example Item");
        addItem(ModItems.MURKY_GEM, "Murky Gem");
        addItem(ModItems.MURKY_THREAD, "Murky Thread");
        addItem(ModItems.PUTRID_PICKAXE, "Putrid Pickaxe");
        addItem(ModItems.PUTRID_AXE, "Putrid Axe");
        addItem(ModItems.PUTRID_HOE, "Putrid Hoe");
        addItem(ModItems.PUTRID_SHOVEL, "Putrid Shovel");
        addItem(ModItems.PUTRID_SWORD, "Putrid Sword");

        add("tooltip.worldchords.hold_alt", "Hold Alt for tier info");
        add("tooltip.worldchords.tier", "Tier: %s");
        add("tooltip.worldchords.tier.rf_io", "RF I/O: %s RF/t");
        add("tooltip.worldchords.tier.sf_io", "SF I/O: %s SF/t");
        add("tooltip.worldchords.crafting_level", "Crafting Level: %s");

        add("worldchords.configuration.title", "World Chords Configs");
        add("worldchords.configuration.section.worldchords.common.toml", "World Chords Configs");
        add("worldchords.configuration.section.worldchords.common.toml.title", "World Chords Configs");
        add("worldchords.configuration.items", "Item List");
        add("worldchords.configuration.logDirtBlock", "Log Dirt Block");
        add("worldchords.configuration.magicNumberIntroduction", "Magic Number Text");
        add("worldchords.configuration.magicNumber", "Magic Number");
    }

    /** {@code "murky_siphon"} -> {@code "Murky Siphon"}, {@code "tier_1"} -> {@code "Tier 1"}. */
    private static String titleCase(String id) {
        var result = new StringBuilder(id.length());
        for (String word : id.split("_")) {
            if (!result.isEmpty()) {
                result.append(' ');
            }
            result.append(Character.toUpperCase(word.charAt(0))).append(word, 1, word.length());
        }
        return result.toString();
    }
}
