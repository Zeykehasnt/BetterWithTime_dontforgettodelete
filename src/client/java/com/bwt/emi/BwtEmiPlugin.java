package com.bwt.emi;

import com.bwt.BetterWithTime;
import com.bwt.blocks.BwtBlocks;
import com.bwt.emi.recipehandlers.EmiCookingPotRecipeHandler;
import com.bwt.emi.recipes.*;
import com.bwt.recipes.BlockIngredient;
import com.bwt.recipes.BwtRecipes;
import com.bwt.recipes.IngredientWithCount;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class BwtEmiPlugin implements EmiPlugin {
    public static final Identifier WIDGETS = new Identifier("bwt", "textures/gui/container/emiwidgets.png");

    public static EmiRecipeCategory CAULDRON = category("cauldron", EmiStack.of(BwtBlocks.cauldronBlock));
    public static EmiRecipeCategory STOKED_CAULDRON = category("stoked_cauldron", EmiStack.of(BwtBlocks.cauldronBlock));
    public static EmiRecipeCategory CRUCIBLE = category("crucible", EmiStack.of(BwtBlocks.crucibleBlock));
    public static EmiRecipeCategory STOKED_CRUCIBLE = category("crucible", EmiStack.of(BwtBlocks.crucibleBlock));
    public static EmiRecipeCategory MILL_STONE = category("mill_stone", EmiStack.of(BwtBlocks.millStoneBlock));
    public static EmiRecipeCategory SAW = category("saw", EmiStack.of(BwtBlocks.sawBlock));
    public static EmiRecipeCategory TURNTABLE = category("turntable", EmiStack.of(BwtBlocks.turntableBlock));
    public static EmiRecipeCategory KILN = category("kiln", EmiStack.of(Blocks.BRICKS));
    public static EmiRecipeCategory SOUL_FORGE_ONLY = category("soul_forge_only", EmiStack.of(BwtBlocks.soulForgeBlock));
    public static EmiRecipeCategory SOUL_FORGE = category("soul_forge", EmiStack.of(BwtBlocks.soulForgeBlock));

    public static EmiRecipeCategory category(String id, EmiStack icon) {
        return new EmiRecipeCategory(new Identifier("bwt", id), icon,
                new EmiTexture(new Identifier("emi", "textures/simple_icons/" + id + ".png"), 0, 0, 16, 16, 16, 16, 16, 16));
    }

    public static EmiRecipeCategory category(String id, EmiStack icon, Comparator<EmiRecipe> comp) {
        return new EmiRecipeCategory(new Identifier("btw", id), icon,
                new EmiTexture(new Identifier("emi", "textures/simple_icons/" + id + ".png"), 0, 0, 16, 16, 16, 16, 16, 16), comp);
    }


    private static <C extends Inventory, T extends Recipe<C>> Collection<Pair<Identifier, T>> getRecipes(EmiRegistry registry, RecipeType<T> type) {
        return registry.getRecipeManager().listAllOfType(type).stream().map(e -> new Pair<>(e.id(), e.value())).toList();
    }

    @Override
    public void register(EmiRegistry reg) {
        reg.addCategory(CAULDRON);
        reg.addCategory(STOKED_CAULDRON);
        reg.addCategory(CRUCIBLE);
        reg.addCategory(STOKED_CRUCIBLE);
        reg.addCategory(MILL_STONE);
        reg.addCategory(SAW);
        reg.addCategory(TURNTABLE);
        reg.addCategory(KILN);
        reg.addCategory(SOUL_FORGE);
        reg.addCategory(SOUL_FORGE_ONLY);

        reg.addRecipeHandler(BetterWithTime.cauldronScreenHandler, new EmiCookingPotRecipeHandler<>(CAULDRON));
        reg.addRecipeHandler(BetterWithTime.cauldronScreenHandler, new EmiCookingPotRecipeHandler<>(STOKED_CAULDRON));

        reg.addWorkstation(CAULDRON, EmiStack.of(BwtBlocks.cauldronBlock));
        reg.addWorkstation(STOKED_CAULDRON, EmiStack.of(BwtBlocks.cauldronBlock));
        reg.addWorkstation(CRUCIBLE, EmiStack.of(BwtBlocks.crucibleBlock));
        reg.addWorkstation(STOKED_CRUCIBLE, EmiStack.of(BwtBlocks.crucibleBlock));
        reg.addWorkstation(MILL_STONE, EmiStack.of(BwtBlocks.millStoneBlock));
        reg.addWorkstation(SAW, EmiStack.of(BwtBlocks.sawBlock));
        reg.addWorkstation(TURNTABLE, EmiStack.of(BwtBlocks.turntableBlock));
        reg.addWorkstation(KILN, EmiStack.of(Blocks.BRICKS));
        reg.addWorkstation(SOUL_FORGE, EmiStack.of(BwtBlocks.soulForgeBlock));
        reg.addWorkstation(SOUL_FORGE_ONLY, EmiStack.of(BwtBlocks.soulForgeBlock));
        reg.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(BwtBlocks.soulForgeBlock));

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
        for (var recipe : getRecipes(reg, BwtRecipes.MILL_STONE_RECIPE_TYPE)) {
            reg.addRecipe(new EmiMillstoneRecipe(MILL_STONE, recipe.getLeft(), recipe.getRight()));
        }
        for (var recipe : getRecipes(reg, BwtRecipes.SAW_RECIPE_TYPE)) {
            reg.addRecipe(new EmiSawRecipe(SAW, recipe.getLeft(), recipe.getRight()));
        }
        for (var recipe : getRecipes(reg, BwtRecipes.TURNTABLE_RECIPE_TYPE)) {
            reg.addRecipe(new EmiTurntableRecipe(TURNTABLE, recipe.getLeft(), recipe.getRight()));
        }
        for (var recipe : getRecipes(reg, BwtRecipes.KILN_RECIPE_TYPE)) {
            reg.addRecipe(new EmiKilnRecipe(KILN, recipe.getLeft(), recipe.getRight()));
        }
//        for (var r : getRecipes(reg, RecipeType.CRAFTING)) {
//            reg.addRecipe(new EmiSoulForgeRecipe(r.getRight(),r.getLeft()));
//        }
    }

    public static EmiIngredient from(IngredientWithCount ingredientWithCount) {
        return EmiIngredient.of(ingredientWithCount.ingredient(), ingredientWithCount.count());
    }

    public static EmiIngredient from(BlockIngredient blockIngredient) {
        List<EmiIngredient> ingredientList = new ArrayList<>();
        blockIngredient.optionalBlock().map(EmiStack::of).ifPresent(ingredientList::add);
        blockIngredient.optionalBlockTagKey().map(EmiIngredient::of).ifPresent(ingredientList::add);
        return EmiIngredient.of(ingredientList);
    }

}

