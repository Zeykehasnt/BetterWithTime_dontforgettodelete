package com.bwt.mixin;

import com.bwt.blocks.BwtBlocks;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LandPathNodeMaker.class)
public class LandPathNodeMakerMixin {
    @Inject(method = "getCommonNodeType", at = @At(value = "HEAD"), cancellable = true)
    private static void bwt$getCommonNodeType(BlockView world, BlockPos pos, CallbackInfoReturnable<PathNodeType> cir) {
        if (world.getBlockState(pos).isOf(BwtBlocks.vineTrapBlock)) {
            cir.setReturnValue(PathNodeType.TRAPDOOR);
        }
    }
}
