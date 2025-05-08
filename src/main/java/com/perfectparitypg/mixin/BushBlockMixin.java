package com.perfectparitypg.mixin;

import com.perfectparitypg.world.level.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BushBlock.class)
public abstract class BushBlockMixin {
    // target the *static* helper used by every flower/sapling
    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
    private void pg$allowOnCustomMoss(BlockState stateBelow, BlockGetter level,
                                      BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (stateBelow.is(ModBlocks.PALE_MOSS_BLOCK)) {
            cir.setReturnValue(true);   // allow the placement
        }
    }
}