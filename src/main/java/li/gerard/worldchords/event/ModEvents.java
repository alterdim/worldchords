package li.gerard.worldchords.event;

import li.gerard.worldchords.WorldChords;
import li.gerard.worldchords.item.ModItems;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

@EventBusSubscriber(modid = WorldChords.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        if (event.getState().is(BlockTags.DIAMOND_ORES) && event.getTool().is(ModItems.PUTRID_PICKAXE.get())) {
            for (var itemEntity : event.getDrops()) {
                ItemStack stack = itemEntity.getItem();
                itemEntity.setItem(new ItemStack(ModItems.MURKY_GEM.get(), stack.getCount()));
            }
        }
    }
}
