package li.gerard.worldchords.block;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot;
import dev.vfyjxf.taffy.style.FlexDirection;
import li.gerard.worldchords.block.entity.SculkAltarBlockEntity;
import li.gerard.worldchords.block.entity.generic.SculkForceMachineBlockEntity;
import li.gerard.worldchords.block.generic.SculkForceMachineBlock;
import li.gerard.worldchords.tier.Tier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class SculkAltarBlock extends SculkForceMachineBlock {

    public SculkAltarBlock(Properties properties) {
        super(Tier.MURKY, properties);
    }

    @Override
    protected void addMachineContent(UIElement root, SculkForceMachineBlockEntity machine,
                                     BlockUIMenuType.BlockUIHolder holder) {
        if (machine instanceof SculkAltarBlockEntity altar) {
            var altarSlots = new UIElement();
            altarSlots.layout(l -> l.flexDirection(FlexDirection.ROW).gapAll(4));
            altarSlots.addChildren(
                    new ItemSlot().bind(altar.getInputSlot(), 0),
                    new ItemSlot().bind(altar.getOutputSlot(), 0));
            root.addChild(altarSlots);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SculkAltarBlockEntity(blockPos, blockState);
    }
}
