package com.bwt.mixin.planter_and_vase_functionality;

import com.bwt.tags.BwtBlockTags;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.block.*;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class FlowerBlockOffsetMixin extends State<Block, BlockState> {
    @Shadow public abstract Block getBlock();

    protected FlowerBlockOffsetMixin(Block owner, Reference2ObjectArrayMap<Property<?>, Comparable<?>> propertyMap, MapCodec<BlockState> codec) {
        super(owner, propertyMap, codec);
    }

    @Inject(method="getModelOffset", at = @At("HEAD"), cancellable = true)
    public void getModelOffset(BlockView world, BlockPos pos, CallbackInfoReturnable<Vec3d> cir) {
        if (this.getBlock() instanceof FlowerBlock && world.getBlockState(pos.down()).isIn(BwtBlockTags.VASES)) {
            cir.setReturnValue(Vec3d.ZERO);
        }
    }
}
