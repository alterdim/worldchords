package li.gerard.worldchords.datagen;

import li.gerard.worldchords.WorldChords;

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
        event.createProvider(WorldChordsBlockTagProvider::new);
        event.createProvider(WorldChordsItemTagProvider::new);
        event.createProvider(WorldChordsRecipeProvider.Runner::new);

    }
}
