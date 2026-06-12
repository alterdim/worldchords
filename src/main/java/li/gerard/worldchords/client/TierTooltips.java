package li.gerard.worldchords.client;

import com.mojang.blaze3d.platform.InputConstants;

import li.gerard.worldchords.WorldChords;
import li.gerard.worldchords.item.CraftingToolItem;
import li.gerard.worldchords.item.MaterialItem;
import li.gerard.worldchords.tier.Tier;
import li.gerard.worldchords.tier.Tiered;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jspecify.annotations.Nullable;

/**
 * Adds the tier info to tooltips of {@link Tiered} items and blocks while Alt is held:
 * the colored tier name, the RF/SF I/O levels for machines, and the crafting level for
 * crafting tools.
 */
@EventBusSubscriber(modid = WorldChords.MODID, value = Dist.CLIENT)
public final class TierTooltips {

    private TierTooltips() {}

    @SubscribeEvent
    static void onItemTooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        Tier tier = tierOf(item);
        if (tier == null) {
            return;
        }

        var tooltip = event.getToolTip();
        if (!isAltDown()) {
            tooltip.add(Component.translatable("tooltip.worldchords.hold_alt")
                    .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
            return;
        }

        Integer color = colorOf(item);
        var tierName = tier.displayName();
        if (color != null) {
            tierName.withStyle(s -> s.withColor(color));
        }
        tooltip.add(Component.translatable("tooltip.worldchords.tier", tierName)
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        // machines (tiered blocks) show their fixed transfer levels
        if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof Tiered) {
            tooltip.add(valueLine("tooltip.worldchords.tier.rf_io", tier.rfTransfer(), color));
            tooltip.add(valueLine("tooltip.worldchords.tier.sf_io", tier.sfTransfer(), color));
        }
        if (item instanceof CraftingToolItem tool) {
            tooltip.add(valueLine("tooltip.worldchords.crafting_level", tool.getCraftingLevel(), color));
        }
    }

    private static Component valueLine(String key, int value, @Nullable Integer color) {
        var coloredValue = Component.literal(String.valueOf(value));
        if (color != null) {
            coloredValue.withStyle(s -> s.withColor(color));
        }
        return Component.translatable(key, coloredValue).withStyle(ChatFormatting.GRAY);
    }

    /** The material color of the item, or null for things without a material (machines). */
    private static @Nullable Integer colorOf(Item item) {
        if (item instanceof CraftingToolItem tool) {
            return tool.getMaterial().color();
        }
        if (item instanceof MaterialItem materialItem) {
            return materialItem.getMaterial().color();
        }
        return null;
    }

    private static @Nullable Tier tierOf(Item item) {
        if (item instanceof Tiered tiered) {
            return tiered.getTier();
        }
        if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof Tiered tiered) {
            return tiered.getTier();
        }
        return null;
    }

    private static boolean isAltDown() {
        var window = Minecraft.getInstance().getWindow();
        return InputConstants.isKeyDown(window, InputConstants.KEY_LALT)
                || InputConstants.isKeyDown(window, InputConstants.KEY_RALT);
    }
}
