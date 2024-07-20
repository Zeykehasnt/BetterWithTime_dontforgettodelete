package com.bwt.emi.recipes;

import com.bwt.blocks.BwtBlocks;
import com.bwt.emi.BwtEmiPlugin;
import com.bwt.items.BwtItems;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EmiSoulUrnRecipe implements EmiRecipe {

    public static final EmiTexture FULL_GEAR = new EmiTexture(BwtEmiPlugin.WIDGETS, 14, 0, 14, 14);

    private final EmiStack urn;
    private final EmiStack soulUrn;
    private final EmiStack netherrack;
    private final EmiStack hellfire;

    public EmiSoulUrnRecipe() {
        urn = EmiStack.of(BwtBlocks.urnBlock);
        soulUrn = EmiStack.of(BwtItems.soulUrnItem);

        netherrack = EmiStack.of(BwtItems.groundNetherrackItem, 8);
        hellfire = EmiStack.of(BwtItems.hellfireDustItem, 8);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return BwtEmiPlugin.HOPPER_SOULS;
    }

    @Override
    public @Nullable Identifier getId() {
        return new Identifier("bwt", "hopper_soul_urn");
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(this.urn, this.netherrack);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(this.soulUrn, this.hellfire);
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


        widgets.addSlot(this.netherrack, x + 18, y).drawBack(false);
        widgets.addSlot(this.hellfire, x + 18*2, y).drawBack(false);

        y = 18;
        widgets.addSlot(EmiStack.of(Blocks.SOUL_SAND), x,y);
        widgets.addSlot(EmiStack.of(BwtBlocks.hopperBlock), x+18, y).drawBack(false);
        widgets.addTexture(FULL_GEAR, x + 20+18, y).tooltip(List.of(EmiTooltipComponents.of(Text.translatable("emi.tooltip.bwt.hopper_souls_power"))));
        y = 18 * 2;

        widgets.addSlot(this.urn, x+18, y).drawBack(false);
        widgets.addSlot(this.soulUrn, x + 18*2, y).drawBack(false);
    }
}
