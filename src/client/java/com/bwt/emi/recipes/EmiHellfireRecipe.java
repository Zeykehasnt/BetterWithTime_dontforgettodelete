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

public class EmiHellfireRecipe implements EmiRecipe {

    public static final EmiTexture FULL_GEAR = new EmiTexture(BwtEmiPlugin.WIDGETS, 14, 0, 14, 14);

    private EmiStack netherrack, hellfire;

    public EmiHellfireRecipe() {
        netherrack = EmiStack.of(BwtItems.groundNetherrackItem, 1);
        hellfire = EmiStack.of(BwtItems.hellfireDustItem, 1);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return BwtEmiPlugin.HOPPER_SOULS;
    }

    @Override
    public @Nullable Identifier getId() {
        return new Identifier("bwt", "hopper_hellfire");
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(this.netherrack);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(this.hellfire);
    }

    @Override
    public int getDisplayWidth() {
        return 18 * 3;
    }

    @Override
    public int getDisplayHeight() {
        return 18 * 2;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        var y = 0;
        var x = 0;
        var i = 0;

        widgets.addSlot(this.netherrack, x+18, y).drawBack(false);
        widgets.addSlot(this.hellfire, x + 18*2, y).drawBack(false);

        y = 18;
        widgets.addSlot(EmiStack.of(Blocks.SOUL_SAND), x,y);
        widgets.addSlot(EmiStack.of(BwtBlocks.hopperBlock), x + 18, y).drawBack(false);
        widgets.addTexture(FULL_GEAR, x + 18 + 20, y);

    }
}
