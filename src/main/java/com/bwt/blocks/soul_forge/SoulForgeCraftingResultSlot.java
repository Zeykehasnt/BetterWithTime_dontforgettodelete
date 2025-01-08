package com.bwt.blocks.soul_forge;

import com.bwt.recipes.BwtRecipes;
import com.bwt.utils.OrderedRecipeMatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

public class SoulForgeCraftingResultSlot extends CraftingResultSlot {
    private final RecipeInputInventory input;
    private final PlayerEntity player;

    public SoulForgeCraftingResultSlot(PlayerEntity player, RecipeInputInventory input, Inventory inventory, int index, int x, int y) {
        super(player, input, inventory, index, x, y);
        this.input = input;
        this.player = player;
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        this.onCrafted(stack);
        CraftingRecipeInput.Positioned positioned = this.input.createPositionedRecipeInput();
        CraftingRecipeInput craftingRecipeInput = positioned.input();
        int i = positioned.left();
        int j = positioned.top();
        DefaultedList<ItemStack> defaultedList = OrderedRecipeMatcher.getRemainingStacks(
                player.getWorld(),
                craftingRecipeInput,
                List.of(BwtRecipes.SOUL_FORGE_RECIPE_TYPE, RecipeType.CRAFTING)
        );

        for (int k = 0; k < craftingRecipeInput.getHeight(); k++) {
            for (int l = 0; l < craftingRecipeInput.getWidth(); l++) {
                int m = l + i + (k + j) * this.input.getWidth();
                ItemStack itemStack = this.input.getStack(m);
                ItemStack itemStack2 = defaultedList.get(l + k * craftingRecipeInput.getWidth());
                if (!itemStack.isEmpty()) {
                    this.input.removeStack(m, 1);
                    itemStack = this.input.getStack(m);
                }

                if (!itemStack2.isEmpty()) {
                    if (itemStack.isEmpty()) {
                        this.input.setStack(m, itemStack2);
                    } else if (ItemStack.areItemsAndComponentsEqual(itemStack, itemStack2)) {
                        itemStack2.increment(itemStack.getCount());
                        this.input.setStack(m, itemStack2);
                    } else if (!this.player.getInventory().insertStack(itemStack2)) {
                        this.player.dropItem(itemStack2, false);
                    }
                }
            }
        }
    }
}
