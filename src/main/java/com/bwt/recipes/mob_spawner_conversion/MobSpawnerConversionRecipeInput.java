package com.bwt.recipes.mob_spawner_conversion;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public record MobSpawnerConversionRecipeInput(Block block) implements RecipeInput {
    @Override
    public ItemStack getStackInSlot(int slot) {
        return block.asItem().getDefaultStack();
    }

    @Override
    public int getSize() {
        return 1;
    }
}
