package com.bwt.emi;

import com.bwt.BetterWithTime;
import com.bwt.blocks.BwtBlocks;
import com.bwt.blocks.mech_hopper.MechHopperBlock;
import com.bwt.blocks.mech_hopper.MechHopperBlockEntity;
import com.bwt.emi.recipehandlers.EmiCookingPotRecipeHandler;
import com.bwt.emi.recipehandlers.EmiSoulForgeRecipeHandler;
import com.bwt.emi.recipes.*;
import com.bwt.recipes.AbstractCookingPotRecipe;
import com.bwt.recipes.BlockIngredient;
import com.bwt.recipes.BwtRecipes;
import com.bwt.recipes.IngredientWithCount;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiRender;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
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
    public static EmiRecipeCategory STOKED_CRUCIBLE = category("stoked_crucible", EmiStack.of(BwtBlocks.crucibleBlock));
    public static EmiRecipeCategory STOKED_CRUCIBLE_RECLAIM = category("stoked_crucible_reclaim", EmiStack.of(BwtBlocks.crucibleBlock));

    public static EmiRecipeCategory MILL_STONE = category("mill_stone", EmiStack.of(BwtBlocks.millStoneBlock));
    public static EmiRecipeCategory SAW = category("saw", EmiStack.of(BwtBlocks.sawBlock));
    public static EmiRecipeCategory TURNTABLE = category("turntable", EmiStack.of(BwtBlocks.turntableBlock));
    public static EmiRecipeCategory KILN = category("kiln", EmiStack.of(Blocks.BRICKS));
    public static EmiRecipeCategory SOUL_FORGE = category("soul_forge", EmiStack.of(BwtBlocks.soulForgeBlock));
    public static EmiRecipeCategory HOPPER_SOULS = category("hopper_souls", EmiStack.of(BwtBlocks.hopperBlock));
    public static EmiRecipeCategory HOPPER_FILTERING = category("hopper_filtering", EmiStack.of(BwtBlocks.hopperBlock));

    public static EmiRenderable simplifiedEmiStack(EmiStack stack) {
        return stack::render;
    }

    public static EmiRecipeCategory category(String id, EmiStack icon) {
        return new EmiRecipeCategory(new Identifier("bwt", id), icon, icon::render);
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
        reg.addCategory(STOKED_CRUCIBLE_RECLAIM);
        reg.addCategory(MILL_STONE);
        reg.addCategory(SAW);
        reg.addCategory(TURNTABLE);
        reg.addCategory(KILN);
        reg.addCategory(SOUL_FORGE);
        reg.addCategory(HOPPER_SOULS);
        reg.addCategory(HOPPER_FILTERING);


//        reg.addRecipeHandler(BetterWithTime.cauldronScreenHandler, new EmiCookingPotRecipeHandler<>(CAULDRON));
//        reg.addRecipeHandler(BetterWithTime.cauldronScreenHandler, new EmiCookingPotRecipeHandler<>(STOKED_CAULDRON));
        reg.addRecipeHandler(BetterWithTime.soulForgeScreenHandler, new EmiSoulForgeRecipeHandler.FourByFour());
        reg.addRecipeHandler(BetterWithTime.soulForgeScreenHandler, new EmiSoulForgeRecipeHandler.ThreeByThree());

        reg.addWorkstation(CAULDRON, EmiStack.of(BwtBlocks.cauldronBlock));
        reg.addWorkstation(STOKED_CAULDRON, EmiStack.of(BwtBlocks.cauldronBlock));
        reg.addWorkstation(CRUCIBLE, EmiStack.of(BwtBlocks.crucibleBlock));
        reg.addWorkstation(STOKED_CRUCIBLE, EmiStack.of(BwtBlocks.crucibleBlock));
        reg.addWorkstation(MILL_STONE, EmiStack.of(BwtBlocks.millStoneBlock));
        reg.addWorkstation(SAW, EmiStack.of(BwtBlocks.sawBlock));
        reg.addWorkstation(TURNTABLE, EmiStack.of(BwtBlocks.turntableBlock));
        reg.addWorkstation(KILN, EmiStack.of(Blocks.BRICKS));
        reg.addWorkstation(SOUL_FORGE, EmiStack.of(BwtBlocks.soulForgeBlock));
        reg.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(BwtBlocks.soulForgeBlock));
        reg.addWorkstation(HOPPER_SOULS, EmiStack.of(BwtBlocks.hopperBlock));
        reg.addWorkstation(HOPPER_FILTERING, EmiStack.of(BwtBlocks.hopperBlock));

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
            var category = STOKED_CRUCIBLE;
            if( recipe.getRight().getCategory().equals(AbstractCookingPotRecipe.CookingPotRecipeCategory.RECLAIM)) {
                category = STOKED_CRUCIBLE_RECLAIM;
            }
            reg.addRecipe(new EmiCookingPotRecipe<>(category, recipe.getLeft(), recipe.getRight()));
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
        for (var r : getRecipes(reg, BwtRecipes.SOUL_FORGE_RECIPE_TYPE)) {
            reg.addRecipe(new EmiSoulForgeRecipe(r.getRight(),r.getLeft()));
        }
        reg.addRecipe(new EmiSoulUrnRecipe());
        reg.addRecipe(new EmiHellfireRecipe());

        reg.addRecipe(new EmiHopperFilterRecipe(
                new Identifier("bwt", "gravel_through_wicker"),
                EmiStack.of(BwtBlocks.wickerPaneBlock),
                EmiStack.of(Blocks.GRAVEL),
                EmiStack.of(Blocks.SAND),
                EmiStack.of(Items.FLINT)
        ));

        for(var filterEntry: MechHopperBlock.filterMap.entrySet()) {
            var filter = filterEntry.getKey();
            var permitted = filterEntry.getValue();
            if(permitted instanceof MechHopperBlock.TagFilter f) {
                var emiPermitted = EmiIngredient.of(f.tagKey());
                Identifier id = new Identifier("bwt", Registries.ITEM.getId(filter).getPath() + "_hopper_filter");
                reg.addRecipe(new EmiHopperFilterPermitList(id, EmiStack.of(filter), emiPermitted));
            }
        }
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

