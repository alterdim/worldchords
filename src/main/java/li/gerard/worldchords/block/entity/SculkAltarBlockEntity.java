package li.gerard.worldchords.block.entity;

import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.syncdata.holder.blockentity.ISyncPersistRPCBlockEntity;
import com.lowdragmc.lowdraglib2.syncdata.storage.FieldManagedStorage;

import li.gerard.worldchords.capability.SculkForceIO;
import li.gerard.worldchords.capability.SculkForceStorage;
import li.gerard.worldchords.capability.SideConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.Nullable;

public class SculkAltarBlockEntity extends BlockEntity implements ISyncPersistRPCBlockEntity {
    public static final int SCULK_FORCE_CAPACITY = 10000;
    public static final int SCULK_FORCE_MAX_TRANSFER = 256;

    private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);

    @Persisted
    @DescSynced
    private final SculkForceStorage sculkForce = new SculkForceStorage(SCULK_FORCE_CAPACITY, SCULK_FORCE_MAX_TRANSFER)
            .setOnChanged(this::setChanged);

    @Persisted
    @DescSynced
    private final SideConfig sculkForceSides = new SideConfig(true).setOnChanged(this::onSideConfigChanged);

    @Persisted
    @DescSynced
    private final ItemStacksResourceHandler inputSlot = createSlot();

    @Persisted
    @DescSynced
    private final ItemStacksResourceHandler outputSlot = createSlot();

    public SculkAltarBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.SCULK_ALTAR.get(), worldPosition, blockState);
    }

    private ItemStacksResourceHandler createSlot() {
        return new ItemStacksResourceHandler(1) {
            @Override
            protected void onContentsChanged(int index, ItemStack previousContents) {
                setChanged();
            }
        };
    }

    @Override
    public FieldManagedStorage getSyncStorage() {
        return syncStorage;
    }

    public ItemStacksResourceHandler getInputSlot() {
        return inputSlot;
    }

    public ItemStacksResourceHandler getOutputSlot() {
        return outputSlot;
    }

    public SculkForceStorage getSculkForce() {
        return sculkForce;
    }

    public SideConfig getSculkForceSides() {
        return sculkForceSides;
    }

    /**
     * The altar consumes sculk force: it accepts input on sides enabled in the side
     * config and never outputs. A {@code null} side (internal access) gets the
     * unrestricted storage.
     */
    public @Nullable EnergyHandler getSculkForceHandler(@Nullable Direction side) {
        if (side == null) {
            return sculkForce;
        }
        return sculkForceSides.isEnabled(side) ? sculkForce.sided(SculkForceIO.INPUT) : null;
    }

    private void onSideConfigChanged() {
        setChanged();
        // neighboring pipes/machines cache capability lookups; tell them to re-query
        if (level != null && !level.isClientSide()) {
            invalidateCapabilities();
        }
    }
}
