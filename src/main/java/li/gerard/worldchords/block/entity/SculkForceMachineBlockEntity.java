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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import org.jspecify.annotations.Nullable;

/**
 * Base class for machines with a sculk force buffer, owns the storage, the per-side
 * config, and the sided capability handler. Subclasses define capacity and whether
 * enabled sides accept input or offer output via {@link #sculkForceIO()}
 */
public abstract class SculkForceMachineBlockEntity extends BlockEntity implements ISyncPersistRPCBlockEntity {
    private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);

    @Persisted
    @DescSynced
    private final SculkForceStorage sculkForce;

    @Persisted
    @DescSynced
    private final SideConfig sculkForceSides = new SideConfig(true).setOnChanged(this::onSideConfigChanged);

    protected SculkForceMachineBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState,
                                           int forceCapacity, int forceMaxTransfer) {
        super(type, worldPosition, blockState);
        this.sculkForce = new SculkForceStorage(forceCapacity, forceMaxTransfer).setOnChanged(this::setChanged);
    }

    /**
     * The IO mode exposed on sides enabled in the side config {@link SculkForceIO#INPUT}
     * for consumers, {@link SculkForceIO#OUTPUT} for generators
     */
    protected abstract SculkForceIO sculkForceIO();

    @Override
    public FieldManagedStorage getSyncStorage() {
        return syncStorage;
    }

    public SculkForceStorage getSculkForce() {
        return sculkForce;
    }

    public SideConfig getSculkForceSides() {
        return sculkForceSides;
    }

    /**
     * A {@code null} side gets the unrestricted storage
     */
    public @Nullable EnergyHandler getSculkForceHandler(@Nullable Direction side) {
        if (side == null) {
            return sculkForce;
        }
        return sculkForceSides.isEnabled(side) ? sculkForce.sided(sculkForceIO()) : null;
    }

    private void onSideConfigChanged() {
        setChanged();
        // neighboring pipes/machines cache capability lookups; tell them to re-query
        if (level != null && !level.isClientSide()) {
            invalidateCapabilities();
        }
    }
}
