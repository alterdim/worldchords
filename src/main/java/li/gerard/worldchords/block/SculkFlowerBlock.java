package li.gerard.worldchords.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SculkFlowerBlock extends MurkyPlantBlock {
    public static final MapCodec<SculkFlowerBlock> CODEC = simpleCodec(SculkFlowerBlock::new);

    public SculkFlowerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<SculkFlowerBlock> codec() {
        return CODEC;
    }

    @Override
    public int attemptUseCharge(SculkSpreader.ChargeCursor cursor, LevelAccessor level, BlockPos originPos, RandomSource random, SculkSpreader spreader, boolean spreadVeins) {
        return cursor.getDecayDelay() > 0 ? cursor.getCharge() : 0;
    }
}
