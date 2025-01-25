package com.bwt.mixin.client;

import com.bwt.render_layers.KilnBlockCookingRenderLayer;
import com.bwt.utils.KilnBlockCookProgressSetter;
import com.bwt.utils.kiln_block_cook_overlay.KilnBlockCookingInfo;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements KilnBlockCookProgressSetter {
    @Unique
    private final Long2ObjectMap<KilnBlockCookingInfo> kilnBlockCookingInfos = new Long2ObjectOpenHashMap<>();

    @Accessor
    public abstract int getTicks();

    @Accessor
    public abstract BufferBuilderStorage getBufferBuilders();

    @Accessor
    public abstract MinecraftClient getClient();

    @Accessor
    public abstract ClientWorld getWorld();

    @Unique
    @Override
    public void betterWithTime$setKilnBlockCookingInfo(BlockPos pos, int stage) {
        if (stage >= 0 && stage < 10) {
            KilnBlockCookingInfo kilnBlockCookingInfo = this.kilnBlockCookingInfos.get(pos.asLong());

            if (kilnBlockCookingInfo == null
                    || kilnBlockCookingInfo.getPos().getX() != pos.getX()
                    || kilnBlockCookingInfo.getPos().getY() != pos.getY()
                    || kilnBlockCookingInfo.getPos().getZ() != pos.getZ()) {
                kilnBlockCookingInfo = new KilnBlockCookingInfo(pos);
                this.kilnBlockCookingInfos.put(pos.asLong(), kilnBlockCookingInfo);
            }

            kilnBlockCookingInfo.setStage(stage);
        } else {
            this.removeKilnBlockCookingInfo(pos);
        }
    }

    @Unique
    private void removeKilnBlockCookingInfo(BlockPos pos) {
        this.kilnBlockCookingInfos.remove(pos.asLong());
    }
    
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 12))
    public void betterWithTime$render(
            RenderTickCounter tickCounter,
            boolean renderBlockOutline,
            Camera camera,
            GameRenderer gameRenderer,
            LightmapTextureManager lightmapTextureManager,
            Matrix4f matrix4f,
            Matrix4f matrix4f2,
            CallbackInfo ci,
            @Local MatrixStack matrixStack
    ) {

        for (Long2ObjectMap.Entry<KilnBlockCookingInfo> entry : this.kilnBlockCookingInfos.long2ObjectEntrySet()) {
            BlockPos blockPos = BlockPos.fromLong(entry.getLongKey());
            Vec3d vec3d = camera.getPos();
            double d = vec3d.getX();
            double e = vec3d.getY();
            double g = vec3d.getZ();
            double l = (double)blockPos.getX() - d;
            double m = (double)blockPos.getY() - e;
            double n = (double)blockPos.getZ() - g;
            if (!(l * l + m * m + n * n > 1024.0)) {
                KilnBlockCookingInfo kilnBlockCookingInfo = entry.getValue();
                if (kilnBlockCookingInfo != null) {
                    int stage = kilnBlockCookingInfo.getStage();
                    matrixStack.push();
                    matrixStack.translate((double)blockPos.getX() - d, (double)blockPos.getY() - e, (double)blockPos.getZ() - g);
                    MatrixStack.Entry entry3 = matrixStack.peek();
                    VertexConsumer vertexConsumer2 = new OverlayVertexConsumer(
                            getBufferBuilders().getEffectVertexConsumers().getBuffer(KilnBlockCookingRenderLayer.KILN_COOKING_RENDER_LAYERS.get(stage)), entry3, 1.0F
                    );
                    getClient().getBlockRenderManager().renderDamage(getWorld().getBlockState(blockPos), blockPos, getWorld(), matrixStack, vertexConsumer2);
                    matrixStack.pop();
                }
            }
        }
    }

}
