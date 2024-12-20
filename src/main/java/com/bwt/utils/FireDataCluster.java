package com.bwt.utils;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FireDataCluster {
    public static final int primaryFireFactor = 5;
    public static final int secondaryFireFactor = 1; // This was changed to 3 later

    FireData fireData;
    boolean centerBlockIsStoked;

    public FireDataCluster() {
        this.fireData = new FireData();
        this.centerBlockIsStoked = false;
    }

    public static FireDataCluster fromWorld(World world, BlockPos pos, int radius) {
        FireDataCluster fireDataCluster = new FireDataCluster();
        BlockPos below = pos.down();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                BlockPos posToTest = below.offset(Direction.Axis.X, x).offset(Direction.Axis.Z, z);
                BlockState testingBlockState = world.getBlockState(posToTest);
                FireData dataToAdd = FireData.FIRE_AMOUNT_FUNCTIONS.getOrDefault(testingBlockState.getBlock().getClass(), FireData.FireAmountFunction.NONE)
                        .getFireData(world, posToTest, testingBlockState);
                // The center block determines whether a fire is active or not
                if (x == 0 && z == 0) {
                    if (!dataToAdd.anyFirePresent()) {
                        return new FireDataCluster();
                    }
                    // Stoked fire in the center dominates the resulting type
                    if (dataToAdd.stokedCount > 0) {
                        fireDataCluster.centerBlockIsStoked = true;
                    }
                }
                fireDataCluster.fireData.add(dataToAdd);
            }
        }
        return fireDataCluster;
    }

    public static FireDataCluster fromWorld(World world, BlockPos pos) {
        return fromWorld(world, pos, 1);
    }

    public boolean isStoked() {
        return centerBlockIsStoked;
    }

    public int getUnstokedCount() {
        return isStoked() ? 0 : fireData.getUnstokedCount();
    }

    public int getStokedCount() {
        return isStoked() ? fireData.getStokedCount() : 0;
    }

    public boolean anyFirePresent() {
        return getUnstokedCount() > 0 || getStokedCount() > 0;
    }

    public int getUnstokedFactor() {
        int unstokedCount = getUnstokedCount();
        return unstokedCount > 0 ? primaryFireFactor + (unstokedCount - 1) * secondaryFireFactor : 0;
    }

    public int getStokedFactor() {
        int stokedCount = getStokedCount();
        return stokedCount > 0 ? primaryFireFactor + (stokedCount - 1) * secondaryFireFactor : 0;
    }

    public int getDominantFireTypeFactor() {
        return isStoked() ? getStokedFactor() : getUnstokedFactor();
    }
}
