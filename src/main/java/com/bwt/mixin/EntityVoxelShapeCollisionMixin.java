package com.bwt.mixin;

import com.bwt.utils.VoxelShapedEntity;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.EntityView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(EntityView.class)
public interface EntityVoxelShapeCollisionMixin {
    @ModifyReturnValue(method = "getEntityCollisions", at = @At("TAIL"))
    default List<VoxelShape> bwt$getEntityCollisions(List<VoxelShape> original, Entity entity, @Local List<Entity> list) {
        if (list.stream().noneMatch(entity1 -> entity1 instanceof VoxelShapedEntity)) {
            return original;
        }
        VoxelShape entityVoxelShape = entity instanceof VoxelShapedEntity voxelShapedEntity ? voxelShapedEntity.getVoxelShape() : VoxelShapes.cuboid(entity.getBoundingBox().expand(1e-7));
        return list.stream().map(entity1 -> {
            if (entity1 instanceof VoxelShapedEntity voxelShapedEntity) {
                return voxelShapedEntity.getVoxelShape().offset(entity1.getPos().getX(), entity1.getPos().getY(), entity1.getPos().getZ());
            }
            return VoxelShapes.cuboid(entity1.getBoundingBox().offset(entity1.getPos()));
        }).filter(shape ->
                VoxelShapes.matchesAnywhere(shape, entityVoxelShape, BooleanBiFunction.AND)
        ).collect(Collectors.toList());
    }
}
