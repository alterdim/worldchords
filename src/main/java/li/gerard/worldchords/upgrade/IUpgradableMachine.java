package li.gerard.worldchords.upgrade;

import net.minecraft.core.BlockPos;

/**
 * A machine block entity that can be boosted by {@link UpgradeBlock}s placed in a cuboid
 * around it. Implemented by block entities ({@link net.minecraft.world.level.block.entity.BlockEntity#getBlockPos()}
 * satisfies {@link #getBlockPos()}).
 */
public interface IUpgradableMachine {

    /**
     * The Chebyshev distance within which upgrade blocks connect to this machine:
     * {@code 0} accepts no upgrades, {@code 1} accepts upgrades in the surrounding
     * cuboid. Must not exceed {@link UpgradeBlock#MAX_MACHINE_UPGRADE_RANGE}.
     */
    int getUpgradeRange();

    BlockPos getBlockPos();
}
