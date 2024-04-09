package com.bwt.mixin;

import com.bwt.items.BwtItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EggEntity.class)
public class EggEntityMixin {
    @Unique
    protected boolean chickenSpawned;

    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)V", at = @At("TAIL"))
    public void bwt$init1(World world, LivingEntity owner, CallbackInfo ci) {
        this.chickenSpawned = false;
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;DDD)V", at = @At("TAIL"))
    public void bwt$init2(World world, double x, double y, double z, CallbackInfo ci) {
        this.chickenSpawned = false;
    }

    @Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V", at = @At("TAIL"))
    public void bwt$init3(EntityType<EggEntity> entityType, World world, CallbackInfo ci) {
        this.chickenSpawned = false;
    }

    @Inject(method = "onCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    public void bwt$preventRawEggDrops(HitResult hitResult, CallbackInfo ci) {
        this.chickenSpawned = true;
    }

    @Inject(method = "onCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/thrown/EggEntity;discard()V"))
    public void bwt$spawnRawEgg(HitResult hitResult, CallbackInfo ci) {
        EggEntity eggEntity = (EggEntity)(Object)this;
        if (!this.chickenSpawned) {
            eggEntity.getWorld().spawnEntity(new ItemEntity(eggEntity.getWorld(), eggEntity.getX(), eggEntity.getY(), eggEntity.getZ(), new ItemStack(BwtItems.rawEggItem)));
        }
    }
}
