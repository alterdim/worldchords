package li.gerard.worldchords.block.entity;

import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.syncdata.holder.blockentity.ISyncPersistRPCBlockEntity;
import com.lowdragmc.lowdraglib2.syncdata.storage.FieldManagedStorage;

import li.gerard.worldchords.capability.SculkForceIO;
import li.gerard.worldchords.capability.SculkForceStorage;
import li.gerard.worldchords.capability.SideConfig;
import li.gerard.worldchords.upgrade.HungerUpgradeBlock;
import li.gerard.worldchords.upgrade.IUpgradableMachine;
import li.gerard.worldchords.upgrade.UpgradeBlock;

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
public abstract class SculkForceMachineBlockEntity extends BlockEntity implements ISyncPersistRPCBlockEntity, IUpgradableMachine {
    /** How often the cached upgrade count is refreshed by {@link #getWorkRateUpgrades()}. */
    public static final int UPGRADE_RECHECK_INTERVAL_TICKS = 40;

    private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);

    private int cachedHungerUpgrades;
    private long nextUpgradeRecheckTime = Long.MIN_VALUE;

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

    /**
     * Connected hunger upgrades, cached for {@value UPGRADE_RECHECK_INTERVAL_TICKS}
     * ticks so per-tick callers don't rescan the neighborhood.
     */
    public int getHungerUpgrades() {
        if (level == null) {
            return 0;
        }
        long now = level.getGameTime();
        if (now >= nextUpgradeRecheckTime) {
            nextUpgradeRecheckTime = now + UPGRADE_RECHECK_INTERVAL_TICKS;
            cachedHungerUpgrades = countConnectedUpgrades(HungerUpgradeBlock.class);
        }
        return cachedHungerUpgrades;
    }

    /**
     * Counts upgrades of the given type within this machine's upgrade range that are
     * connected exclusively to this machine (contested upgrades count for nobody).
     */
    public int countConnectedUpgrades(Class<? extends UpgradeBlock> upgradeType) {
        int range = getUpgradeRange();
        if (range <= 0 || level == null || !level.isAreaLoaded(getBlockPos(), range)) {
            return 0;
        }
        int count = 0;
        for (BlockPos cursor : BlockPos.betweenClosed(
                getBlockPos().offset(-range, -range, -range), getBlockPos().offset(range, range, range))) {
            if (upgradeType.isInstance(level.getBlockState(cursor).getBlock())
                    && UpgradeBlock.findConnectedMachine(level, cursor.immutable()) == this) {
                count++;
            }
        }
        return count;
    }

    private void onSideConfigChanged() {
        setChanged();
        //tell stuff around that we changed
        if (level != null && !level.isClientSide()) {
            invalidateCapabilities();
        }
    }
}
