package li.gerard.worldchords.upgrade;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

/**
 * Base class for machine upgrade blocks. An upgrade connects to the machine whose
 * {@linkplain IUpgradableMachine#getUpgradeRange() upgrade range} covers it — but stops
 * working entirely when two different machines claim it. Right-clicking an upgrade opens
 * the GUI of its connected machine.
 */
public abstract class UpgradeBlock extends Block {
    /**
     * Upper bound on machine upgrade ranges, and how far an upgrade scans for machines
     * claiming it.
     */
    public static final int MAX_MACHINE_UPGRADE_RANGE = 2;

    protected UpgradeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                            Player player, BlockHitResult hit) {
        if (!level.isClientSide()) {
            var machine = findConnectedMachine(level, pos);
            if (machine != null) {
                BlockUIMenuType.openUI((ServerPlayer) player, machine.getBlockPos());
            }
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * The single machine this upgrade is connected to, or {@code null} when no machine
     * is in range or the upgrade is contested between two machines.
     */
    public static @Nullable IUpgradableMachine findConnectedMachine(Level level, BlockPos upgradePos) {
        if (!level.isAreaLoaded(upgradePos, MAX_MACHINE_UPGRADE_RANGE)) {
            return null;
        }
        IUpgradableMachine connected = null;
        for (BlockPos cursor : BlockPos.betweenClosed(
                upgradePos.offset(-MAX_MACHINE_UPGRADE_RANGE, -MAX_MACHINE_UPGRADE_RANGE, -MAX_MACHINE_UPGRADE_RANGE),
                upgradePos.offset(MAX_MACHINE_UPGRADE_RANGE, MAX_MACHINE_UPGRADE_RANGE, MAX_MACHINE_UPGRADE_RANGE))) {
            if (level.getBlockEntity(cursor) instanceof IUpgradableMachine machine) {
                int distance = chebyshevDistance(upgradePos, cursor);
                if (distance > 0 && distance <= machine.getUpgradeRange()) {
                    if (connected != null) {
                        return null; // contested between two machines: the upgrade stops working
                    }
                    connected = machine;
                }
            }
        }
        return connected;
    }

    public static int chebyshevDistance(BlockPos a, BlockPos b) {
        return Math.max(Math.abs(a.getX() - b.getX()),
                Math.max(Math.abs(a.getY() - b.getY()), Math.abs(a.getZ() - b.getZ())));
    }
}
