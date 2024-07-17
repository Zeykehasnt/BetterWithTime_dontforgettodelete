package com.bwt.emi.recipes;

import com.bwt.blocks.BwtBlocks;
import com.bwt.emi.BwtEmiPlugin;
import com.bwt.items.BwtItems;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EmiHopperFilterRecipe implements EmiRecipe {

    public static final EmiTexture FULL_GEAR = new EmiTexture(BwtEmiPlugin.WIDGETS, 14, 0, 14, 14);

    private final Identifier id;
    private EmiIngredient filter;
    private EmiStack input;
    private EmiStack passedThrough, blocked;

    public EmiHopperFilterRecipe(Identifier id, EmiIngredient filter, EmiStack input, EmiStack passedThrough, EmiStack blocked) {
        this.id = id;
        this.filter = filter;
        this.input = input;
        this.passedThrough = passedThrough;
        this.blocked = blocked;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return BwtEmiPlugin.HOPPER_FILTERING;
    }

    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(this.input);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(this.passedThrough, this.blocked);
    }

    @Override
    public int getDisplayWidth() {
        return 18 * 3;
    }

    @Override
    public int getDisplayHeight() {
        return 18 * 3;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        var y = 0;
        var x = 0;

        widgets.addSlot(this.input, x+18, y).drawBack(false);
        if(this.blocked != null) {
            widgets.addSlot(this.blocked, x + 18 * 2, y).drawBack(false);
        }

        y = 18;
        widgets.addSlot(this.filter, x,y);
        widgets.addSlot(EmiStack.of(BwtBlocks.hopperBlock), x + 18, y).drawBack(false);

        y = 18*2;
        widgets.addSlot(this.passedThrough, x + 18, y).drawBack(false);
    }
}
