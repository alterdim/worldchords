package li.gerard.worldchords.block;

import li.gerard.worldchords.WorldChords;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.minecraft.world.level.block.Blocks.GOLD_ORE;

public class ModBlocks {
    // Create a Deferred Register to hold Blocks which will all be registered under the "worldchords" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(WorldChords.MODID);

    // Creates a new Block with the id "worldchords:example_block", combining the namespace and path
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", p -> p.mapColor(MapColor.STONE));
    public static final DeferredBlock<Block> PUTRID_ORE_BLOCK = BLOCKS.registerSimpleBlock("putrid_ore", p -> p.mapColor(MapColor.STONE));
    public static final DeferredBlock<SculkAltarBlock> SCULK_ALTAR_BLOCK = BLOCKS.registerBlock("sculk_altar", SculkAltarBlock::new, p -> p
            .mapColor(MapColor.COLOR_CYAN).noCollision().instabreak().sound(SoundType.SCULK).pushReaction(PushReaction.IGNORE));

    public static final DeferredBlock<SculkDevourerBlock> SCULK_DEVOURER = BLOCKS.registerBlock("sculk_devourer", SculkDevourerBlock::new, p -> p
            .mapColor(MapColor.COLOR_CYAN).strength(3.0F).sound(SoundType.SCULK));

    public static final DeferredBlock<FarmlandBlock> MURKY_GROUNDS = BLOCKS.registerBlock("murky_grounds", FarmlandBlock::new, p -> p
            .mapColor(MapColor.DIRT).randomTicks().strength(0.6F).sound(SoundType.GRAVEL)
            .isViewBlocking((state, level, pos) -> true).isSuffocating((state, level, pos) -> true));

    public static final DeferredBlock<MurkyPlantBlock> MURKY_PLANT = BLOCKS.registerBlock("murky_plant", MurkyPlantBlock::new, p -> p
            .mapColor(MapColor.PLANT).noCollision().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY));

    public static final DeferredBlock<SculkFlowerBlock> SCULK_FLOWER = BLOCKS.registerBlock("sculk_flower", SculkFlowerBlock::new, p -> p
            .mapColor(MapColor.COLOR_CYAN).noCollision().instabreak().sound(SoundType.SCULK).pushReaction(PushReaction.DESTROY));

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
