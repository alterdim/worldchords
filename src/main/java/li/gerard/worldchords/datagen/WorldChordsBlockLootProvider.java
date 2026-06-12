package li.gerard.worldchords.datagen;

import li.gerard.worldchords.block.ModBlocks;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Set;

public class WorldChordsBlockLootProvider extends BlockLootSubProvider {

    protected WorldChordsBlockLootProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.SCULK_FLOWER.get());
        dropSelf(ModBlocks.SCULK_DEVOURER.get());
        dropSelf(ModBlocks.HUNGER_UPGRADE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return List.of(ModBlocks.SCULK_FLOWER.get(), ModBlocks.SCULK_DEVOURER.get(), ModBlocks.HUNGER_UPGRADE.get());
    }
}
