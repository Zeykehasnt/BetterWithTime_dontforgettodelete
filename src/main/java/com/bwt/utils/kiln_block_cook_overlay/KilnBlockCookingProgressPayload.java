package com.bwt.utils.kiln_block_cook_overlay;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record KilnBlockCookingProgressPayload(BlockPos blockPos, int progress) implements CustomPayload {
    public static final Identifier KILN_COOK_PACKET_ID = com.bwt.utils.Id.of("kiln_cook_overlay");
    public static final CustomPayload.Id<KilnBlockCookingProgressPayload> ID = new CustomPayload.Id<>(KILN_COOK_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, KilnBlockCookingProgressPayload> CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, KilnBlockCookingProgressPayload::blockPos,
            PacketCodecs.INTEGER, KilnBlockCookingProgressPayload::progress,
            KilnBlockCookingProgressPayload::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
