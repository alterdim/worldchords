package li.gerard.worldchords.block.entity;

import li.gerard.worldchords.capability.SculkForceIO;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class SculkDevourerBlockEntity extends SculkForceMachineBlockEntity {
    public static final int SCULK_FORCE_CAPACITY = 10000;
    public static final int SCULK_FORCE_MAX_TRANSFER = 256;
    public static final int RANGE = 2;
    public static final int UPGRADE_RANGE = 1;
    public static final int FORCE_PER_SCULK = 10;
    private static final int DEVOUR_INTERVAL_TICKS = 200;

    public SculkDevourerBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.SCULK_DEVOURER.get(), worldPosition, blockState,
                SCULK_FORCE_CAPACITY, SCULK_FORCE_MAX_TRANSFER);
    }

    @Override
    protected SculkForceIO sculkForceIO() {
        return SculkForceIO.OUTPUT;
    }

    @Override
    public int getUpgradeRange() {
        return UPGRADE_RANGE;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SculkDevourerBlockEntity devourer) {
        // hunger upgrades shorten the devour interval
        int interval = Math.max(1, DEVOUR_INTERVAL_TICKS / (1 + devourer.getHungerUpgrades()));
        // stagger by position so multiple devourers don't all scan on the same tick
        if (Math.floorMod(level.getGameTime() + pos.asLong(), interval) != 0) {
            return;
        }
        var force = devourer.getSculkForce();
        if (force.getCapacityAsInt() - force.getAmountAsInt() < FORCE_PER_SCULK) {
            return;
        }
        // don't sync-load neighboring chunks at a chunk border just to scan them
        if (!level.isAreaLoaded(pos, RANGE)) {
            return;
        }
        // one cursor
        for (BlockPos cursor : BlockPos.betweenClosed(pos.offset(-RANGE, -RANGE, -RANGE), pos.offset(RANGE, RANGE, RANGE))) {
            if (level.getBlockState(cursor).is(Blocks.SCULK)) {
                BlockPos sculkPos = cursor.immutable();
                level.setBlockAndUpdate(sculkPos, Blocks.STONE.defaultBlockState());
                level.playSound(null, sculkPos, SoundEvents.SCULK_BLOCK_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                force.set(force.getAmountAsInt() + FORCE_PER_SCULK);
                return;
            }
        }
    }
}
