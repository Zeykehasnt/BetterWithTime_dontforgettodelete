package com.bwt.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class OrderedRecipeMatcher {
    public static <I extends RecipeInput, T extends Recipe<I>> Optional<RecipeEntry<T>> getFirstRecipeOfMultipleTypes(
            World world,
            I input,
            List<RecipeType<T>> recipeTypes
    ) {
        RecipeManager recipeManager = world.getRecipeManager();
        for (RecipeType<T> recipeType: recipeTypes) {
            Optional<RecipeEntry<T>> optionalResult = recipeManager.getFirstMatch(recipeType, input, world);
            if (optionalResult.isPresent()) {
                return optionalResult;
            }
        }
        return Optional.empty();
    }

    public static <I extends RecipeInput, T extends Recipe<I>> DefaultedList<ItemStack> getRemainingStacks(
            World world,
            I input,
            List<RecipeType<T>> recipeTypes
    ) {
        Optional<RecipeEntry<T>> optional = getFirstRecipeOfMultipleTypes(world, input, recipeTypes);
        if (optional.isPresent()) {
            return optional.get().value().getRemainder(input);
        } else {
            DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(input.getSize(), ItemStack.EMPTY);

            for (int i = 0; i < defaultedList.size(); i++) {
                defaultedList.set(i, input.getStackInSlot(i));
            }

            return defaultedList;
        }
    }

    public static <I extends RecipeInput, R extends Recipe<I>> void getFirstRecipe(List<RecipeEntry<R>> matches, DefaultedList<ItemStack> inventoryItems, Predicate<R> predicateConsumer) {
        // For each inventory item, in order
        for (ItemStack inventoryStack : inventoryItems) {
            // Filter down to recipes that contain that item in its ingredients.
            // If there are multiple that match the first ingredient, get the one with the most ingredients
            Iterator<RecipeEntry<R>> matchIterator = matches.stream()
                    .filter(match -> match.value().getIngredients().stream().anyMatch(ingredient -> ingredient.test(inventoryStack)))
                    .sorted(Comparator.comparing((RecipeEntry<R> match) -> match.value().getIngredients().size()).reversed())
                    .iterator();
            while (matchIterator.hasNext()) {
                R match = matchIterator.next().value();
                if (predicateConsumer.test(match)) {
                    return;
                }
            }
        }
    }
}
