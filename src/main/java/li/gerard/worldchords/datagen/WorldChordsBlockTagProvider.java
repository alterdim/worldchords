package li.gerard.worldchords.datagen;

import li.gerard.worldchords.WorldChords;
import li.gerard.worldchords.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.world.level.block.Blocks.GOLD_ORE;

public class WorldChordsBlockTagProvider extends BlockTagsProvider {

    public WorldChordsBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, WorldChords.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.PUTRID_ORE_BLOCK.get());
        tag(BlockTags.SCULK_REPLACEABLE).add(GOLD_ORE);

    }
}
