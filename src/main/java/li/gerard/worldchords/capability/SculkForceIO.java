package li.gerard.worldchords.capability;

/**
 * Per-side IO configuration for machines exposing the sculk force capability.
 */
public enum SculkForceIO {
    NONE(false, false),
    INPUT(true, false),
    OUTPUT(false, true),
    BOTH(true, true);

    private final boolean canInsert;
    private final boolean canExtract;

    SculkForceIO(boolean canInsert, boolean canExtract) {
        this.canInsert = canInsert;
        this.canExtract = canExtract;
    }

    public boolean canInsert() {
        return canInsert;
    }

    public boolean canExtract() {
        return canExtract;
    }
}
