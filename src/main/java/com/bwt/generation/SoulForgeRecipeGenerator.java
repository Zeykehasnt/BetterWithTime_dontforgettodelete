package com.bwt.generation;

import com.bwt.blocks.BwtBlocks;
import com.bwt.blocks.SidingBlock;
import com.bwt.items.BwtItems;
import com.bwt.recipes.SoulForgeShapedRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

public class SoulForgeRecipeGenerator extends FabricRecipeProvider {
    public SoulForgeRecipeGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        for (SidingBlock sidingBlock : BwtBlocks.sidingBlocks) {
            if (sidingBlock.isWood()) {
                continue;
            }
            SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, sidingBlock, 8)
                    .pattern("XXXX")
                    .input('X', sidingBlock.fullBlock)
                    .criterion(hasItem(sidingBlock.fullBlock), conditionsFromItem(sidingBlock.fullBlock))
                    .offerTo(exporter);
        }
        SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, BwtBlocks.blockDispenserBlock)
                .pattern("mmmm")
                .pattern("muum")
                .pattern("stts")
                .pattern("srrs")
                .input('m', Items.MOSSY_COBBLESTONE)
                .input('u', BwtItems.soulUrnItem)
                .input('s', Items.STONE)
                .input('t', Items.REDSTONE_TORCH)
                .input('r', Items.REDSTONE)
                .criterion(hasItem(BwtItems.soulUrnItem), conditionsFromItem(BwtItems.soulUrnItem))
                .offerTo(exporter);
    }
}
