package com.bwt.emi.recipes;

import com.bwt.blocks.abstract_cooking_pot.AbstractCookingPotBlockEntity;
import com.bwt.emi.BwtEmiPlugin;
import com.bwt.recipes.AbstractCookingPotRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EmiCookingPotRecipe<R extends AbstractCookingPotRecipe> implements EmiRecipe {

    private final EmiRecipeCategory category;
    private final Identifier id;
    private final List<EmiIngredient> ingredients;
    private final List<EmiStack> results;

    public EmiCookingPotRecipe(EmiRecipeCategory category, Identifier id, R recipe) {
        this.category = category;
        this.id = id;
        this.ingredients = recipe.getIngredientsWithCount().stream().map(BwtEmiPlugin::from).toList();
        this.results = recipe.getResults().stream().map(EmiStack::of).toList();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return category;
    }

    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return this.ingredients;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return this.results;
    }

    @Override
    public int getDisplayWidth() {
        return 20 * 7;
    }

    @Override
    public int getDisplayHeight() {
        return 20 * 3;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        var y = 0;
        var x = 0;
        widgets.addTexture(EmiTexture.EMPTY_FLAME, 20 * 3, 9);
        widgets.addAnimatedTexture(EmiTexture.FULL_FLAME, 20 * 3, 9, (AbstractCookingPotBlockEntity.timeToCompleteCook * 10), false, true, false);

        var i = 0;

        int constantInputSlots = 6;
        int constantOutputSlots = 3;

        for (EmiIngredient ingredient : this.ingredients) {
            widgets.addSlot(ingredient, x + (i % 3 * 18), y + (i / 3 * 18));
            i++;
        }
        while (i < constantInputSlots) {
            widgets.addSlot(EmiStack.EMPTY, x + (i % 3 * 18), y + (i / 3 * 18));
            i++;
        }

        i = 0;
        x = 20 * 4;
        for (EmiIngredient ingredient : this.results) {
            widgets.addSlot(ingredient, x + (i * 18), y);
            i++;
        }
        while (i < constantOutputSlots) {
            widgets.addSlot(EmiStack.EMPTY, x + (i % 3 * 18), y + (i / 3 * 18));
            i++;
        }
    }
}
