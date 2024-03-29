package com.bwt.mixin.animals;

import com.bwt.entities.GoToAndPickUpBreedingItemGoal;
import com.bwt.mixin.accessors.MobEntityAccessorMixin;
import net.minecraft.entity.passive.SheepEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin implements MobEntityAccessorMixin {
    @Inject(method = "initGoals", at = @At("TAIL"))
    public void addGoal(CallbackInfo ci) {
        this.getGoalSelector().add(3, new GoToAndPickUpBreedingItemGoal((SheepEntity) ((Object) this), 6, 1.5, 1));
    }
}
