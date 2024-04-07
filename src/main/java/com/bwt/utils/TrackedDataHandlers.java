package com.bwt.utils;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.Map;

public class TrackedDataHandlers implements ModInitializer {
    public static TrackedDataHandler<Map<Vec3i, BlockState>> blockStateMapHandler = TrackedDataHandler.of(
            (buf, map) -> buf.writeMap(map, (innerBuf, key) -> innerBuf.writeBlockPos(new BlockPos(key)), (innerBuf, value) -> innerBuf.writeInt(Block.STATE_IDS.getRawId(value))),
            buf -> buf.readMap(PacketByteBuf::readBlockPos, innerBuf -> Block.STATE_IDS.get(innerBuf.readInt()))
    );
    public static TrackedDataHandler<Map<Vec3i, NbtCompound>> blockEntityMapHandler = TrackedDataHandler.of(
            (buf, map) -> buf.writeMap(map, (innerBuf, key) -> innerBuf.writeBlockPos(new BlockPos(key)), PacketByteBuf::writeNbt),
            buf -> buf.readMap(PacketByteBuf::readBlockPos, PacketByteBuf::readNbt)
    );

    @Override
    public void onInitialize() {
        TrackedDataHandlerRegistry.register(blockStateMapHandler);
        TrackedDataHandlerRegistry.register(blockEntityMapHandler);
    }
}
