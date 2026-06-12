package li.gerard.worldchords.block.generic;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.DataBindingBuilder;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.BindableValue;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ProgressBar;
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import com.lowdragmc.lowdraglib2.gui.ui.style.StylesheetManager;
import dev.vfyjxf.taffy.style.FlexDirection;
import dev.vfyjxf.taffy.style.TaffyPosition;
import li.gerard.worldchords.block.entity.generic.SculkForceMachineBlockEntity;
import li.gerard.worldchords.gui.SideConfigPanel;
import li.gerard.worldchords.tier.Tier;
import li.gerard.worldchords.tier.Tiered;
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

import java.util.function.Supplier;

/**
 * Base class for sculk force machine blocks: opens the GUI on use and builds the shared
 * layout (title, machine-specific content, force bar, player inventory, and the corner
 * side-config submenu). Subclasses add their own content via {@link #addMachineContent}
 */
public abstract class SculkForceMachineBlock extends Block implements BlockUIMenuType.BlockUI, EntityBlock, Tiered {

    private final Tier tier;

    protected SculkForceMachineBlock(Tier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    @Override
    public Tier getTier() {
        return tier;
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

            addUpgradeCountLine(root, "Hunger Upgrades", machine::getHungerUpgrades);

            var sideConfig = new SideConfigPanel("Sculk Force Sides", machine.getSculkForceSides());
            var cornerButtons = new UIElement();
            cornerButtons.layout(l -> l.positionType(TaffyPosition.ABSOLUTE).bottom(1).right(1)
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
     * Machine-specific UI content, placed between the title and the force bar
     */
    protected void addMachineContent(UIElement root, SculkForceMachineBlockEntity machine,
                                     BlockUIMenuType.BlockUIHolder holder) {}

    /**
     * Adds a "name: count" line that stays hidden while the count is zero
     */
    protected static void addUpgradeCountLine(UIElement root, String name, Supplier<Integer> counter) {
        var label = new Label();
        label.setDisplay(false);

        var count = new BindableValue<Integer>();
        count.bind(DataBindingBuilder.intValS2C(counter::get).build());
        count.registerValueListener(value -> {
            int upgrades = value == null ? 0 : value;
            label.setText(name + ": " + upgrades, false);
            label.setDisplay(upgrades > 0);
        });

        root.addChildren(label, count);
    }

    @SuppressWarnings("unchecked")
    protected static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return givenType == expectedType ? (BlockEntityTicker<A>) ticker : null;
    }
}
