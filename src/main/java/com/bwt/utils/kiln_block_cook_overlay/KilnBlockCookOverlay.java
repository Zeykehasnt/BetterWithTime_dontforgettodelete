package com.bwt.utils.kiln_block_cook_overlay;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class KilnBlockCookOverlay {
    public static void setKilnBlockCookingInfo(ServerWorld serverWorld, BlockPos pos, int progress) {
        for (ServerPlayerEntity player : PlayerLookup.tracking(serverWorld, pos)) {
            ServerPlayNetworking.send(player, new KilnBlockCookingProgressPayload(pos, progress));
        }
    }

    public static void setKilnBlockCookingInfo(World world, BlockPos pos, int progress) {
        if (world instanceof ServerWorld serverWorld) {
            setKilnBlockCookingInfo(serverWorld, pos, progress);
        }
    }
}
