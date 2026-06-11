package li.gerard.worldchords.event;

import li.gerard.worldchords.WorldChords;
import li.gerard.worldchords.block.ModBlocks;
import li.gerard.worldchords.item.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

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

    @SubscribeEvent
    public static void onBlockToolModification(BlockEvent.BlockToolModificationEvent event) {
        if (event.getItemAbility() == ItemAbilities.HOE_TILL
                && event.getState().is(Blocks.GRASS_BLOCK)
                && event.getHeldItemStack().is(ModItems.PUTRID_HOE.get())) {
            event.setFinalState(ModBlocks.MURKY_GROUNDS.get().defaultBlockState());
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        ItemStack stack = event.getItemStack();
        if (!stack.is(Tags.Items.SEEDS) || !level.getBlockState(event.getPos()).is(ModBlocks.MURKY_GROUNDS.get())) {
            return;
        }
        BlockPos plantPos = event.getPos().above();
        if (!level.getBlockState(plantPos).isAir()) {
            return;
        }
        if (!level.isClientSide()) {
            level.setBlock(plantPos, ModBlocks.MURKY_PLANT.get().defaultBlockState(), 3);
            level.playSound(null, plantPos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);
            stack.consume(1, event.getEntity());
        }
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }
}
