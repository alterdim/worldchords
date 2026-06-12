package li.gerard.worldchords.block;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.DataBindingBuilder;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ProgressBar;
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import com.lowdragmc.lowdraglib2.gui.ui.style.StylesheetManager;
import dev.vfyjxf.taffy.style.FlexDirection;
import dev.vfyjxf.taffy.style.TaffyPosition;
import li.gerard.worldchords.block.entity.SculkForceMachineBlockEntity;
import li.gerard.worldchords.gui.SideConfigPanel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

/**
 * Base class for sculk force machine blocks: opens the GUI on use and builds the shared
 * layout (title, machine-specific content, force bar, player inventory, and the corner
 * side-config submenu). Subclasses add their own content via {@link #addMachineContent}.
 */
public abstract class SculkForceMachineBlock extends Block implements BlockUIMenuType.BlockUI, EntityBlock {

    protected SculkForceMachineBlock(Properties properties) {
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
        root.addClass("panel_bg");
        root.addChild(new Label().setText(getName()));

        if (holder.player.level().getBlockEntity(holder.pos) instanceof SculkForceMachineBlockEntity machine) {
            addMachineContent(root, machine, holder);

            var bar = new ProgressBar();
            bar.setRange(0, machine.getSculkForce().getCapacityAsInt());
            bar.setProgress(machine.getSculkForce().getAmountAsInt());
            bar.bind(DataBindingBuilder.floatValS2C(
                    () -> (float) machine.getSculkForce().getAmountAsInt()
            ).build());
            bar.label.setText("Sculk Force");
            root.addChild(bar);

            var sideConfig = new SideConfigPanel("Sculk Force Sides", machine.getSculkForceSides());
            var cornerButtons = new UIElement();
            cornerButtons.layout(l -> l.positionType(TaffyPosition.ABSOLUTE).bottom(2).right(2)
                    .flexDirection(FlexDirection.ROW).gapAll(2));
            cornerButtons.addChild(sideConfig.createOpenButton("S"));

            root.addChild(new InventorySlots());
            // overlay last, so it renders on top of the main GUI
            root.addChildren(cornerButtons, sideConfig);
        } else {
            root.addChild(new InventorySlots());
        }

        return ModularUI.of(UI.of(root, StylesheetManager.ORE), holder.player);
    }

    /**
     * Machine-specific UI content, placed between the title and the force bar.
     */
    protected void addMachineContent(UIElement root, SculkForceMachineBlockEntity machine,
                                     BlockUIMenuType.BlockUIHolder holder) {}

    @SuppressWarnings("unchecked")
    protected static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return givenType == expectedType ? (BlockEntityTicker<A>) ticker : null;
    }
}
