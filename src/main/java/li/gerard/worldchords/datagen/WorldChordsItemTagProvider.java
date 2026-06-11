package li.gerard.worldchords.datagen;

import li.gerard.worldchords.WorldChords;
import li.gerard.worldchords.item.ModItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class WorldChordsItemTagProvider extends ItemTagsProvider {

    public WorldChordsItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, WorldChords.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModItems.PUTRID_TOOL_MATERIALS).add(ModItems.PUTRID_INGOT.get());
    }
}
