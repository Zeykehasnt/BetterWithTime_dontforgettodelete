package com.bwt.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.block.spawner.MobSpawnerLogic;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobSpawnerLogic.class)
public class SpawnerMossyCobbleMixin {
    @Inject(method = "serverTick", at = @At("HEAD"))
    protected void bwt$createMossyCobblestone(ServerWorld world, BlockPos pos, CallbackInfo ci) {
        if (world.random.nextInt(1200) != 0) {
            return;
        }
        BlockPos randomPos = new BlockPos(
                pos.getX() + world.random.nextBetween(-4, 4),
                pos.getY() + world.random.nextBetween(-1, 4),
                pos.getZ() + world.random.nextBetween(-4, 4)
        );
        if (!world.getBlockState(randomPos).isOf(Blocks.COBBLESTONE)) {
            return;
        }
        world.setBlockState(randomPos, Blocks.MOSSY_COBBLESTONE.getDefaultState());
    }
}
