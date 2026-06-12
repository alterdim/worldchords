package li.gerard.worldchords.item;

import li.gerard.worldchords.WorldChords;
import li.gerard.worldchords.block.ModBlocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WorldChords.MODID);

    public static final TagKey<Item> PUTRID_TOOL_MATERIALS = TagKey.create(Registries.ITEM,
            Identifier.fromNamespaceAndPath(WorldChords.MODID, "putrid_tool_materials"));
    public static final ToolMaterial PUTRID_MATERIAL = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 32, 12.0F, 0.0F, 22, PUTRID_TOOL_MATERIALS);

    //Block items
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", ModBlocks.EXAMPLE_BLOCK);
    public static final DeferredItem<BlockItem> PUTRID_ORE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("putrid_ore", ModBlocks.PUTRID_ORE_BLOCK);
    public static final DeferredItem<BlockItem> MURKY_GROUNDS_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("murky_grounds", ModBlocks.MURKY_GROUNDS);
    public static final DeferredItem<BlockItem> SCULK_FLOWER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("sculk_flower", ModBlocks.SCULK_FLOWER);
    public static final DeferredItem<BlockItem> SCULK_ALTAR_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("sculk_altar", ModBlocks.SCULK_ALTAR_BLOCK);
    public static final DeferredItem<BlockItem> SCULK_DEVOURER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("sculk_devourer", ModBlocks.SCULK_DEVOURER);
    public static final DeferredItem<BlockItem> HUNGER_UPGRADE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("hunger_upgrade", ModBlocks.HUNGER_UPGRADE);

    // Crafting mats
    public static final DeferredItem<Item> PUTRID_INGOT = ITEMS.registerSimpleItem("putrid_ingot");
    public static final DeferredItem<Item> MURKY_GEM = ITEMS.registerSimpleItem("murky_gem");
    public static final DeferredItem<Item> MURKY_THREAD = ITEMS.registerSimpleItem("murky_thread");

    // Tools
    public static final DeferredItem<Item> PUTRID_PICKAXE = ITEMS.registerItem("putrid_pickaxe", Item::new, p -> p.pickaxe(PUTRID_MATERIAL, 1.0F, -2.8F));
    public static final DeferredItem<AxeItem> PUTRID_AXE = ITEMS.registerItem("putrid_axe", p -> new AxeItem(PUTRID_MATERIAL, 6.0F, -3.0F, p));
    public static final DeferredItem<HoeItem> PUTRID_HOE = ITEMS.registerItem("putrid_hoe", p -> new HoeItem(PUTRID_MATERIAL, 0.0F, -3.0F, p));
    public static final DeferredItem<ShovelItem> PUTRID_SHOVEL = ITEMS.registerItem("putrid_shovel", p -> new ShovelItem(PUTRID_MATERIAL, 1.5F, -3.0F, p));
    public static final DeferredItem<Item> PUTRID_SWORD = ITEMS.registerItem("putrid_sword", Item::new, p -> p.sword(PUTRID_MATERIAL, 3.0F, -2.4F));

    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", p -> p.food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    public static void register(IEventBus modEventBus) {
        // force-loads CraftingTools so its static registrations land in ITEMS first
        CraftingTools.register();
        ITEMS.register(modEventBus);
    }
}
