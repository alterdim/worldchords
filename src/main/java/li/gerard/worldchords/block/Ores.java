package li.gerard.worldchords.block;

import li.gerard.worldchords.item.CraftingTools;
import li.gerard.worldchords.item.ModItems;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Declares ores and auto-registers their block and block item. One line in
 * {@link #ORES} yields the {@code <material>_ore} block (shared tinted ore model),
 * loot, mineable tag, lang, ore-to-ingot smelting and natural generation — no other
 * files to touch.
 */
public final class Ores {

    public static final List<OreDeclaration> ORES = List.of(
            new OreDeclaration(CraftingTools.material("berserkium"), 9, 8, -60, 20));

    /** material name -> ore block, in declaration order. */
    private static final Map<String, DeferredBlock<Block>> BLOCKS;
    private static final Map<String, DeferredItem<BlockItem>> ITEMS;

    static {
        var blocks = new LinkedHashMap<String, DeferredBlock<Block>>();
        var items = new LinkedHashMap<String, DeferredItem<BlockItem>>();
        for (OreDeclaration ore : ORES) {
            var block = ModBlocks.BLOCKS.registerSimpleBlock(ore.name(),
                    p -> p.mapColor(MapColor.STONE).strength(3.0F).sound(net.minecraft.world.level.block.SoundType.STONE));
            blocks.put(ore.material().name(), block);
            items.put(ore.material().name(), ModItems.ITEMS.registerSimpleBlockItem(ore.name(), block));
        }
        BLOCKS = Collections.unmodifiableMap(blocks);
        ITEMS = Collections.unmodifiableMap(items);
    }

    private Ores() {}

    public static DeferredBlock<Block> block(OreDeclaration ore) {
        return BLOCKS.get(ore.material().name());
    }

    public static DeferredItem<BlockItem> item(OreDeclaration ore) {
        return ITEMS.get(ore.material().name());
    }

    /** Forces class load so the static registrations run before the registry event. */
    public static void register() {}
}
