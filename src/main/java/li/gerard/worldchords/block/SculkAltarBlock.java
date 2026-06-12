package li.gerard.worldchords.block;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.DataBindingBuilder;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import com.lowdragmc.lowdraglib2.gui.ui.style.StylesheetManager;
import dev.vfyjxf.taffy.style.AlignItems;
import dev.vfyjxf.taffy.style.FlexDirection;
import li.gerard.worldchords.block.entity.SculkAltarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class SculkAltarBlock extends Block implements BlockUIMenuType.BlockUI, EntityBlock {

    public SculkAltarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                            Player player, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockUIMenuType.openUI((ServerPlayer) player, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public ModularUI createUI(BlockUIMenuType.BlockUIHolder holder) {

        var root = new UIElement();
        root.layout(l -> l.gapAll(4).alignItems(AlignItems.CENTER)).addClass("panel_bg");
        root.addChild(new Label().setText("Sculk Altar Block"));

        if (holder.player.level().getBlockEntity(holder.pos) instanceof SculkAltarBlockEntity altar) {
            var altarSlots = new UIElement();
            altarSlots.layout(l -> l.flexDirection(FlexDirection.ROW).gapAll(4));
            altarSlots.addChildren(
                    new ItemSlot().bind(altar.getInputSlot(), 0),
                    new ItemSlot().bind(altar.getOutputSlot(), 0));
            root.addChild(altarSlots);
            root.addChild(new Label().bind(DataBindingBuilder.componentS2C(() -> Component.literal(
                    "Sculk Force: " + altar.getSculkForce().getAmountAsInt() + " / " + altar.getSculkForce().getCapacityAsInt())).build()));
        }

        root.addChild(new InventorySlots());
        var ui = UI.of(root, StylesheetManager.ORE);

        return ModularUI.of(ui, holder.player);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SculkAltarBlockEntity(blockPos, blockState);
    }
}
