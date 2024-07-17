package com.bwt.emi;

import com.bwt.BetterWithTime;
import com.bwt.blocks.BwtBlocks;
import com.bwt.emi.recipehandlers.EmiCookingPotRecipeHandler;
import com.bwt.emi.recipes.EmiCookingPotRecipe;
import com.bwt.recipes.BwtRecipes;
import com.bwt.recipes.IngredientWithCount;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.Comparator;

public class BwtEmiPlugin implements EmiPlugin {

    public static EmiRecipeCategory CAULDRON = category("cauldron", EmiStack.of(BwtBlocks.cauldronBlock));
    public static EmiRecipeCategory STOKED_CAULDRON = category("stoked_cauldron", EmiStack.of(BwtBlocks.cauldronBlock));
    public static EmiRecipeCategory CRUCIBLE = category("crucible", EmiStack.of(BwtBlocks.crucibleBlock));
    public static EmiRecipeCategory STOKED_CRUCIBLE = category("crucible", EmiStack.of(BwtBlocks.crucibleBlock));

    public static EmiRecipeCategory category(String id, EmiStack icon) {
        return new EmiRecipeCategory(new Identifier("bwt", id), icon,
                new EmiTexture(new Identifier("emi", "textures/simple_icons/" + id + ".png"), 0, 0, 16, 16, 16, 16, 16, 16));
    }

    public static EmiRecipeCategory category(String id, EmiStack icon, Comparator<EmiRecipe> comp) {
        return new EmiRecipeCategory(new Identifier("btw", id), icon,
                new EmiTexture(new Identifier("emi", "textures/simple_icons/" + id + ".png"), 0, 0, 16, 16, 16, 16, 16, 16), comp);
    }


    private static <C extends Inventory, T extends Recipe<C>> Iterable<Pair<Identifier, T>> getRecipes(EmiRegistry registry, RecipeType<T> type) {
        return registry.getRecipeManager().listAllOfType(type).stream().map(e -> new Pair<>(e.id(), e.value()))::iterator;
    }

    @Override
    public void register(EmiRegistry reg) {
        reg.addCategory(CAULDRON);
        reg.addCategory(STOKED_CAULDRON);
        reg.addCategory(CRUCIBLE);
        reg.addCategory(STOKED_CRUCIBLE);

        reg.addRecipeHandler(BetterWithTime.cauldronScreenHandler, new EmiCookingPotRecipeHandler<>(CAULDRON));
        reg.addRecipeHandler(BetterWithTime.cauldronScreenHandler, new EmiCookingPotRecipeHandler<>(STOKED_CAULDRON));

        reg.addWorkstation(CAULDRON, EmiStack.of(BwtBlocks.cauldronBlock));
        reg.addWorkstation(STOKED_CAULDRON, EmiStack.of(BwtBlocks.cauldronBlock));
        reg.addWorkstation(CRUCIBLE, EmiStack.of(BwtBlocks.crucibleBlock));
        reg.addWorkstation(STOKED_CRUCIBLE, EmiStack.of(BwtBlocks.crucibleBlock));


        for (var recipe : getRecipes(reg, BwtRecipes.CAULDRON_RECIPE_TYPE)) {
            reg.addRecipe(new EmiCookingPotRecipe<>(CAULDRON, recipe.getLeft(), recipe.getRight()));
        }
        for (var recipe : getRecipes(reg, BwtRecipes.STOKED_CAULDRON_RECIPE_TYPE)) {
            reg.addRecipe(new EmiCookingPotRecipe<>(STOKED_CAULDRON, recipe.getLeft(), recipe.getRight()));
        }
        for (var recipe : getRecipes(reg, BwtRecipes.CRUCIBLE_RECIPE_TYPE)) {
            reg.addRecipe(new EmiCookingPotRecipe<>(CRUCIBLE, recipe.getLeft(), recipe.getRight()));
        }
        for (var recipe : getRecipes(reg, BwtRecipes.STOKED_CRUCIBLE_RECIPE_TYPE)) {
            reg.addRecipe(new EmiCookingPotRecipe<>(STOKED_CRUCIBLE, recipe.getLeft(), recipe.getRight()));
        }

    }

    public static EmiIngredient from(IngredientWithCount ingredientWithCount) {
        return EmiIngredient.of(ingredientWithCount.ingredient(), ingredientWithCount.count());
    }
}
