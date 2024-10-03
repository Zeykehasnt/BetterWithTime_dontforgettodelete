package com.bwt.mixin.accessors;

import com.mojang.serialization.DataResult;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mixin(RawShapedRecipe.class)
public interface RawShapedRecipeAccessorMixin {
    @Accessor
    Optional<RawShapedRecipe.Data> getData();

    @Invoker("fromData")
    static DataResult<RawShapedRecipe> fromData(RawShapedRecipe.Data data) {
        throw new AssertionError();
    }
}
