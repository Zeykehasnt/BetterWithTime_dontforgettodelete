package com.bwt.utils;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SingleStackInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;

import java.util.Optional;

public class SimpleSingleStackInventory implements SingleStackInventory.SingleStackBlockEntityInventory {
    int maxStackSize;
    protected ItemStack stack = ItemStack.EMPTY;

    public SimpleSingleStackInventory(int maxStackSize) {
        super();
        this.maxStackSize = maxStackSize;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public int getMaxCountPerStack() {
        return maxStackSize;
    }

    @Override
    public ItemStack getStack() {
        return this.stack;
    }

    @Override
    public ItemStack decreaseStack(int count) {
        ItemStack itemStack = this.stack.split(count);
        if (this.stack.isEmpty()) {
            this.stack = ItemStack.EMPTY;
        }
        return itemStack;
    }

    @Override
    public void setStack(ItemStack stack) {
        this.stack = stack.copyWithCount(getMaxCountPerStack());
        stack.decrement(getMaxCountPerStack());
    }

    @Override
    public BlockEntity asBlockEntity() {
        return null;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (asBlockEntity() != null) {
            return SingleStackBlockEntityInventory.super.canPlayerUse(player);
        }
        return true;
    }

    public void readNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup registryLookup) {
        this.clear();
        if (nbtCompound.isEmpty()) {
            return;
        }
        Optional<ItemStack> itemStack = ItemStack.fromNbt(registryLookup, nbtCompound);
        itemStack.ifPresent(this::setStack);
    }

    public NbtElement toNbt(RegistryWrapper.WrapperLookup registryLookup) {
        ItemStack itemStack = this.getStack();
        if (itemStack.isEmpty()) return new NbtCompound();
        return itemStack.encode(registryLookup);
    }
}
