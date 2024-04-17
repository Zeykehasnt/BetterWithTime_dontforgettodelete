package com.bwt.mixin;

import com.bwt.blocks.BwtBlocks;
import com.bwt.blocks.mining_charge.MiningChargeExplosion;
import com.bwt.tags.BwtItemTags;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(Entity.class)
public abstract class MiningChargeExplosionImmunityMixin {
    @Inject(method = "isImmuneToExplosion", at = @At("HEAD"), cancellable = true)
    public void bwt$isImmuneToExplosion(Explosion explosion, CallbackInfoReturnable<Boolean> cir) {
        if (!(explosion instanceof MiningChargeExplosion)) {
            return;
        }
        if (!((Entity) ((Object) this) instanceof ItemEntity itemEntity)) {
            return;
        }
        if (itemEntity.getStack().isIn(BwtItemTags.MINING_CHARGE_IMMUNE)) {
            cir.setReturnValue(true);
            return;
        }
    }
}
