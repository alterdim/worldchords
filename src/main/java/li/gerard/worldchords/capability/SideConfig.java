package li.gerard.worldchords.capability;

import net.minecraft.core.Direction;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

/**
 * Per-side enable/disable configuration for one capability of a machine.
 * SideConfig per capability and consults it in its capability provider.
 *
 * <p>{@code ValueIOSerializable}, so it works as a {@code @Persisted}/{@code @DescSynced} field.
 */
public class SideConfig implements ValueIOSerializable {
    private int mask;
    private Runnable onChanged = () -> {};

    public SideConfig(boolean enabledByDefault) {
        this.mask = enabledByDefault ? 0b111111 : 0;
    }

    public SideConfig setOnChanged(Runnable onChanged) {
        this.onChanged = onChanged;
        return this;
    }

    public boolean isEnabled(Direction side) {
        return (mask & (1 << side.ordinal())) != 0;
    }

    public void setEnabled(Direction side, boolean enabled) {
        int bit = 1 << side.ordinal();
        int newMask = enabled ? mask | bit : mask & ~bit;
        if (newMask != mask) {
            mask = newMask;
            onChanged.run();
        }
    }

    public void toggle(Direction side) {
        setEnabled(side, !isEnabled(side));
    }

    @Override
    public void serialize(ValueOutput output) {
        output.putInt("sides", mask);
    }

    @Override
    public void deserialize(ValueInput input) {
        mask = input.getIntOr("sides", mask);
    }
}
