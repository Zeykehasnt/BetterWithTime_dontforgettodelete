package com.bwt.generation;

import com.bwt.blocks.BwtBlocks;
import com.bwt.blocks.CornerBlock;
import com.bwt.blocks.MouldingBlock;
import com.bwt.blocks.SidingBlock;
import com.bwt.items.BwtItems;
import com.bwt.recipes.SoulForgeShapedRecipe;
import com.bwt.recipes.SoulForgeShapelessRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;

public class SoulForgeRecipeGenerator extends FabricRecipeProvider {
    public SoulForgeRecipeGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        for (int i = 0; i < BwtBlocks.sidingBlocks.size(); i++) {
            SidingBlock sidingBlock = BwtBlocks.sidingBlocks.get(i);
            MouldingBlock mouldingBlock = BwtBlocks.mouldingBlocks.get(i);
            CornerBlock cornerBlock = BwtBlocks.cornerBlocks.get(i);
            if (sidingBlock.isWood()) {
                continue;
            }
            SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, sidingBlock, 8)
                    .pattern("XXXX")
                    .input('X', sidingBlock.fullBlock)
                    .group("siding")
                    .criterion(hasItem(sidingBlock.fullBlock), conditionsFromItem(sidingBlock.fullBlock))
                    .offerTo(exporter);
            SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, mouldingBlock, 8)
                    .pattern("XXXX")
                    .input('X', sidingBlock)
                    .group("moulding")
                    .criterion(hasItem(sidingBlock), conditionsFromItem(sidingBlock))
                    .offerTo(exporter);
            SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, cornerBlock, 8)
                    .pattern("XXXX")
                    .input('X', mouldingBlock)
                    .group("corners")
                    .criterion(hasItem(mouldingBlock), conditionsFromItem(mouldingBlock))
                    .offerTo(exporter);

            // Stone Mini block recombining recipes
            SoulForgeShapelessRecipe.JsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, sidingBlock.fullBlock)
                    .input(sidingBlock, 2)
                    .criterion(hasItem(sidingBlock), conditionsFromItem(sidingBlock))
                    .offerTo(exporter, "recombine_" + Registries.BLOCK.getId(sidingBlock).getPath());
            SoulForgeShapelessRecipe.JsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, sidingBlock)
                    .input(mouldingBlock, 2)
                    .group("siding")
                    .criterion(hasItem(mouldingBlock), conditionsFromItem(mouldingBlock))
                    .offerTo(exporter, "recombine_" + Registries.BLOCK.getId(mouldingBlock).getPath());
            SoulForgeShapelessRecipe.JsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, mouldingBlock)
                    .input(cornerBlock, 2)
                    .group("moulding")
                    .criterion(hasItem(cornerBlock), conditionsFromItem(cornerBlock))
                    .offerTo(exporter, "recombine_" + Registries.BLOCK.getId(cornerBlock).getPath());
        }

        SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.TOOLS, Items.NETHERITE_BLOCK)
                .pattern("ssss")
                .pattern("ssss")
                .pattern("ssss")
                .pattern("ssss")
                .input('s', Items.NETHERITE_INGOT)
                .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(Items.NETHERITE_INGOT))
                .offerTo(exporter);

        // Netherite Tools
        SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.TOOLS, Items.NETHERITE_PICKAXE)
                .pattern("sss")
                .pattern(" h ")
                .pattern(" h ")
                .pattern(" h ")
                .input('s', Items.NETHERITE_INGOT)
                .input('h', BwtItems.haftItem)
                .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(Items.NETHERITE_INGOT))
                .offerTo(exporter);
        SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.TOOLS, Items.NETHERITE_SHOVEL)
                .pattern("s")
                .pattern("h")
                .pattern("h")
                .pattern("h")
                .input('s', Items.NETHERITE_INGOT)
                .input('h', BwtItems.haftItem)
                .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(Items.NETHERITE_INGOT))
                .offerTo(exporter);
        SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.TOOLS, Items.NETHERITE_AXE)
                .pattern("ss")
                .pattern("sh")
                .pattern(" h")
                .pattern(" h")
                .input('s', Items.NETHERITE_INGOT)
                .input('h', BwtItems.haftItem)
                .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(Items.NETHERITE_INGOT))
                .offerTo(exporter);
        SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.TOOLS, Items.NETHERITE_HOE)
                .pattern("ss")
                .pattern(" h")
                .pattern(" h")
                .pattern(" h")
                .input('s', Items.NETHERITE_INGOT)
                .input('h', BwtItems.haftItem)
                .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(Items.NETHERITE_INGOT))
                .offerTo(exporter);
        SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.TOOLS, Items.NETHERITE_SWORD)
                .pattern("s")
                .pattern("s")
                .pattern("s")
                .pattern("h")
                .input('s', Items.NETHERITE_INGOT)
                .input('h', BwtItems.haftItem)
                .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(Items.NETHERITE_INGOT))
                .offerTo(exporter);

        // Netherite armor
        SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.TOOLS, BwtItems.armorPlateItem)
                .pattern("SnpS")
                .input('n', Items.NETHERITE_INGOT)
                .input('S', BwtItems.strapItem)
                .input('p', BwtItems.paddingItem)
                .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(Items.NETHERITE_INGOT))
                .offerTo(exporter);
        SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.TOOLS, Items.NETHERITE_HELMET)
                .pattern("ssss")
                .pattern("s  s")
                .pattern("s  s")
                .pattern(" pp ")
                .input('s', Items.NETHERITE_INGOT)
                .input('p', BwtItems.armorPlateItem)
                .criterion(hasItem(BwtItems.armorPlateItem), conditionsFromItem(BwtItems.armorPlateItem))
                .offerTo(exporter);
        SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.TOOLS, Items.NETHERITE_CHESTPLATE)
                .pattern("p  p")
                .pattern("ssss")
                .pattern("ssss")
                .pattern("ssss")
                .input('s', Items.NETHERITE_INGOT)
                .input('p', BwtItems.armorPlateItem)
                .criterion(hasItem(BwtItems.armorPlateItem), conditionsFromItem(BwtItems.armorPlateItem))
                .offerTo(exporter);
        SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.TOOLS, Items.NETHERITE_LEGGINGS)
                .pattern("ssss")
                .pattern("pssp")
                .pattern("p  p")
                .pattern("p  p")
                .input('s', Items.NETHERITE_INGOT)
                .input('p', BwtItems.armorPlateItem)
                .criterion(hasItem(BwtItems.armorPlateItem), conditionsFromItem(BwtItems.armorPlateItem))
                .offerTo(exporter);
        SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.TOOLS, Items.NETHERITE_BOOTS)
                .pattern(" ss ")
                .pattern(" ss ")
                .pattern("spps")
                .input('s', Items.NETHERITE_INGOT)
                .input('p', BwtItems.armorPlateItem)
                .criterion(hasItem(BwtItems.armorPlateItem), conditionsFromItem(BwtItems.armorPlateItem))
                .offerTo(exporter);

        SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.REDSTONE, BwtBlocks.obsidianPressuePlateBlock)
                .pattern("oooo")
                .input('o', Items.OBSIDIAN)
                .criterion(hasItem(Items.OBSIDIAN), conditionsFromItem(Items.OBSIDIAN))
                .offerTo(exporter);
        SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.TOOLS, BwtItems.broadheadItem, 16)
                .pattern(" s ")
                .pattern("sss")
                .pattern(" s ")
                .pattern(" s ")
                .input('s', Items.NETHERITE_INGOT)
                .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(Items.NETHERITE_INGOT))
                .offerTo(exporter);
        SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.TOOLS, BwtItems.redstoneEyeItem)
                .pattern("lll")
                .pattern("ggg")
                .pattern(" r ")
                .input('l', Items.LAPIS_LAZULI)
                .input('g', Items.GOLD_NUGGET)
                .input('r', Items.REDSTONE)
                .criterion(hasItem(Items.LAPIS_LAZULI), conditionsFromItem(Items.LAPIS_LAZULI))
                .offerTo(exporter);

        SoulForgeShapedRecipe.JsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, BwtBlocks.detectorBlock)
                .pattern("cccc")
                .pattern("ette")
                .pattern("srrs")
                .pattern("srrs")
                .input('c', Items.COBBLESTONE)
                .input('e', BwtItems.redstoneEyeItem)
                .input('s', Items.STONE)
                .input('t', Items.REDSTONE_TORCH)
                .input('r', Items.REDSTONE)
                .criterion(hasItem(BwtItems.redstoneEyeItem), conditionsFromItem(BwtItems.redstoneEyeItem))
                .offerTo(exporter);

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
