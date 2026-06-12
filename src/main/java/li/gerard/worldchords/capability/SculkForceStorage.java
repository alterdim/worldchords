package li.gerard.worldchords.capability;

import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import org.jspecify.annotations.Nullable;

/**
 * A sculk force buffer for machines. Transaction-safe and {@code ValueIOSerializable},
 * so it can be a {@code @Persisted}/{@code @DescSynced} field on a block entity.
 */
public class SculkForceStorage extends SimpleEnergyHandler {
    private Runnable onChanged = () -> {};

    public SculkForceStorage(int capacity) {
        super(capacity);
    }

    public SculkForceStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public SculkForceStorage(int capacity, int maxInsert, int maxExtract) {
        super(capacity, maxInsert, maxExtract);
    }

    /**
     * Sets a callback fired whenever the stored amount changes
     * typically {@code BlockEntity::setChanged}.
     */
    public SculkForceStorage setOnChanged(Runnable onChanged) {
        this.onChanged = onChanged;
        return this;
    }

    @Override
    protected void onEnergyChanged(int previousAmount) {
        onChanged.run();
    }

    /**
     * Returns the handler to expose for a side with the given IO mode
     */
    public @Nullable EnergyHandler sided(SculkForceIO io) {
        return switch (io) {
            case NONE -> null;
            case BOTH -> this;
            case INPUT, OUTPUT -> new SidedSculkForceHandler(this, io);
        };
    }
}
