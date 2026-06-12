package li.gerard.worldchords.datagen;

import li.gerard.worldchords.WorldChords;

import java.util.List;
import java.util.Set;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/**
 * Registers all data providers
 */
@EventBusSubscriber(modid = WorldChords.MODID)
public class WorldChordsDataGen {
    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        event.createProvider(WorldChordsModelProvider::new);
        event.createProvider(WorldChordsLanguageProvider::new);
        event.createProvider(WorldChordsBlockTagProvider::new);
        event.createProvider(WorldChordsItemTagProvider::new);
        event.createProvider(WorldChordsRecipeProvider.Runner::new);
        event.createProvider((output, lookupProvider) -> new LootTableProvider(output, Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(WorldChordsBlockLootProvider::new, LootContextParamSets.BLOCK)), lookupProvider));

    }
}
