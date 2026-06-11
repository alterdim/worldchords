package li.gerard.worldchords.block;

import li.gerard.worldchords.WorldChords;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.minecraft.world.level.block.Blocks.GOLD_ORE;

public class ModBlocks {
    // Create a Deferred Register to hold Blocks which will all be registered under the "worldchords" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(WorldChords.MODID);

    // Creates a new Block with the id "worldchords:example_block", combining the namespace and path
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", p -> p.mapColor(MapColor.STONE));
    public static final DeferredBlock<Block> PUTRID_ORE_BLOCK = BLOCKS.registerSimpleBlock("putrid_ore_block", p -> p.mapColor(MapColor.STONE));

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
