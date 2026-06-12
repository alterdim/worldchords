package li.gerard.worldchords.block.entity;

import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;

import li.gerard.worldchords.capability.SculkForceIO;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class SculkAltarBlockEntity extends SculkForceMachineBlockEntity {
    public static final int SCULK_FORCE_CAPACITY = 10000;
    public static final int SCULK_FORCE_MAX_TRANSFER = 256;

    @Persisted
    @DescSynced
    private final ItemStacksResourceHandler inputSlot = createSlot();

    @Persisted
    @DescSynced
    private final ItemStacksResourceHandler outputSlot = createSlot();

    public SculkAltarBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.SCULK_ALTAR.get(), worldPosition, blockState,
                SCULK_FORCE_CAPACITY, SCULK_FORCE_MAX_TRANSFER);
    }

    @Override
    protected SculkForceIO sculkForceIO() {
        // the altar consumes sculk force: enabled sides accept input, never output
        return SculkForceIO.INPUT;
    }

    private ItemStacksResourceHandler createSlot() {
        return new ItemStacksResourceHandler(1) {
            @Override
            protected void onContentsChanged(int index, ItemStack previousContents) {
                setChanged();
            }
        };
    }

    public ItemStacksResourceHandler getInputSlot() {
        return inputSlot;
    }

    public ItemStacksResourceHandler getOutputSlot() {
        return outputSlot;
    }
}
