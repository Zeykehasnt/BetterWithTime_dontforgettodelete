package com.bwt.utils;

import com.bwt.blocks.StokedFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.HashMap;

public class FireData {
    public interface FireAmountFunction {
        FireAmountFunction NONE = (world, pos, state) -> new FireData();

        FireData getFireData(World world, BlockPos pos, BlockState state);
    }

    public static final HashMap<Class<? extends Block>, FireAmountFunction> FIRE_AMOUNT_FUNCTIONS = new HashMap<>();

    static {
        FIRE_AMOUNT_FUNCTIONS.put(FireBlock.class, (world, pos, state) -> new FireData(1, 0));
        FIRE_AMOUNT_FUNCTIONS.put(StokedFireBlock.class, (world, pos, state) -> new FireData(0, 1));
    }

    int unstokedCount;
    int stokedCount;

    public FireData(int unstokedCount, int stokedCount) {
        this.unstokedCount = unstokedCount;
        this.stokedCount = stokedCount;
    }

    public FireData(int unstokedCount) {
        this(unstokedCount, 0);
    }

    public FireData() {
        this(0);
    }

    public void add(FireData otherData) {
        this.unstokedCount += otherData.unstokedCount;
        this.stokedCount += otherData.stokedCount;
    }

    public boolean anyFirePresent() {
        return unstokedCount > 0 || stokedCount > 0;
    }

    public int getUnstokedCount() {
        return unstokedCount;
    }

    public int getStokedCount() {
        return stokedCount;
    }

}