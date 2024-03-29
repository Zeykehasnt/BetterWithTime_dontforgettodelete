package com.bwt.entities;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.function.Predicate;

public class PickUpBreedingItemGoal extends Goal {
    protected final AnimalEntity animal;
    protected final double searchRadius;
    protected final double pickupRadius;
    protected final double speed;
    @Nullable
    protected ItemEntity targetBreedingItem;

    @Nullable
    protected final Predicate<AnimalEntity> wantsFoodCondition;

    public PickUpBreedingItemGoal(AnimalEntity animal, double searchRadius, double pickupRadius, double speed, @Nullable Predicate<AnimalEntity> wantsFoodCondition) {
        this.animal = animal;
        this.searchRadius = searchRadius;
        this.pickupRadius = pickupRadius;
        this.speed = speed;
        this.wantsFoodCondition = wantsFoodCondition;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    public PickUpBreedingItemGoal(AnimalEntity animal, double searchRadius, double pickupRadius, double speed) {
        this(animal, searchRadius, pickupRadius, speed, null);
    }

    protected boolean wantsFood() {
        if (wantsFoodCondition != null && wantsFoodCondition.test(animal)) {
            return true;
        }
        return animal.getBreedingAge() == 0 && animal.canEat();
    }


    protected boolean isTargetValid() {
        return targetBreedingItem != null && targetBreedingItem.isAlive() && !targetBreedingItem.getStack().isEmpty();
    }

    @Nullable
    protected ItemEntity findClosestBreedingItem() {
        return animal.getWorld()
                .getEntitiesByClass(
                        ItemEntity.class,
                        animal.getBoundingBox().expand(searchRadius),
                        itemEntity -> animal.isBreedingItem(itemEntity.getStack())
                )
                .stream()
                .min(Comparator.comparingDouble(animal::squaredDistanceTo))
                .filter(itemEntity -> animal.distanceTo(itemEntity) < searchRadius)
                .orElse(null);
    }

    @Override
    public boolean canStart() {
        if (!wantsFood()) {
            return false;
        }
        targetBreedingItem = findClosestBreedingItem();
        return isTargetValid();
    }

    @Override
    public boolean shouldContinue() {
        return isTargetValid() && wantsFood();
    }

    @Override
    public void tick() {
        if (targetBreedingItem == null || !isTargetValid() || !wantsFood() || !animal.canMoveVoluntarily()) {
            return;
        }
        animal.getLookControl().lookAt(targetBreedingItem, animal.getMaxLookYawChange(), animal.getMaxLookPitchChange());
        if (!(animal instanceof TameableEntity tameableEntity) || !tameableEntity.isSitting()) {
            animal.getNavigation().startMovingTo(targetBreedingItem, speed);
        }
        if (animal.distanceTo(targetBreedingItem) <= pickupRadius) {
            animal.getNavigation().stop();
            targetBreedingItem.getStack().decrement(1);
            animal.lovePlayer(null);
        }
        targetBreedingItem = null;
    }
}
