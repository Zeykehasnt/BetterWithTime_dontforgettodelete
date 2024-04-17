package com.bwt.mixin;

import com.bwt.blocks.BwtBlocks;
import com.bwt.blocks.mining_charge.MiningChargeBlock;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FireBlock.class)
public abstract class FireBlockMixin extends AbstractFireBlock {
    public FireBlockMixin(Settings settings, float damage) {
        super(settings, damage);
    }

    @Inject(method = "trySpreadingFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void primeMiningCharge(World world, BlockPos pos, int spreadFactor, Random random, int currentAge, CallbackInfo ci, @Local BlockState blockState) {
        if (blockState.isOf(BwtBlocks.miningChargeBlock)) {
            MiningChargeBlock.prime(world, pos, blockState);
        }
    }
}
