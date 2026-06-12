package li.gerard.worldchords.capability;

import net.neoforged.neoforge.transfer.energy.DelegatingEnergyHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

/**
 * A view of the handler that restricts insertion/extraction, for exposing
 * input-only or output-only access on specific sides of machine
 */
public class SidedSculkForceHandler extends DelegatingEnergyHandler {
    private final SculkForceIO io;

    public SidedSculkForceHandler(EnergyHandler delegate, SculkForceIO io) {
        super(delegate);
        this.io = io;
    }

    @Override
    public int insert(int amount, TransactionContext transaction) {
        return io.canInsert() ? super.insert(amount, transaction) : 0;
    }

    @Override
    public int extract(int amount, TransactionContext transaction) {
        return io.canExtract() ? super.extract(amount, transaction) : 0;
    }
}
