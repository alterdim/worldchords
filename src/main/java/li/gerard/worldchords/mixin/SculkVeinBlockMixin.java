package li.gerard.worldchords.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import li.gerard.worldchords.block.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkVeinBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SculkVeinBlock.class)
public class SculkVeinBlockMixin {
    @WrapOperation(
            method = "attemptPlaceSculk",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/LevelAccessor;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private boolean worldchords$replaceGoldOreWithPutridOre(LevelAccessor level, BlockPos pos, BlockState newState, int flags, Operation<Boolean> original) {
        if (level.getBlockState(pos).is(Blocks.GOLD_ORE)) {
            newState = ModBlocks.PUTRID_ORE_BLOCK.get().defaultBlockState();
        }
        return original.call(level, pos, newState, flags);
    }
}
