package com.bwt.mixin.animals;

import com.bwt.entities.GoToAndPickUpBreedingItemGoal;
import com.bwt.mixin.accessors.MobEntityAccessorMixin;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PigEntity.class)
public abstract class PigEntityMixin extends AnimalEntity implements MobEntityAccessorMixin {
    protected PigEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    public void addGoal(CallbackInfo ci) {
        this.getGoalSelector().add(3, new GoToAndPickUpBreedingItemGoal(this, 6, 1.5, 1));
    }
}
