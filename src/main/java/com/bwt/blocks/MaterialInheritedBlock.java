package com.bwt.blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.WoodType;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public abstract class MaterialInheritedBlock extends Block {
    public Block fullBlock;

    public MaterialInheritedBlock(Settings settings, Block fullBlock) {
        super(settings);
        this.fullBlock = fullBlock;
    }

    public boolean isWood() {
        return Registries.BLOCK.getId(fullBlock).getPath().contains("planks");
    }

    public static void registerMaterialBlocks(
            ArrayList<SidingBlock> sidingBlocks,
            ArrayList<MouldingBlock> mouldingBlocks,
            ArrayList<CornerBlock> cornerBlocks,
            ArrayList<ColumnBlock> columnBlocks,
            ArrayList<PedestalBlock> pedestalBlocks,
            ArrayList<TableBlock> tableBlocks
    ) {
        WoodType.stream().forEach(woodType -> {
            Block fullBlock = Registries.BLOCK.get(new Identifier(woodType.name() + "_planks"));
            Block slabBlock = Registries.BLOCK.get(new Identifier(woodType.name() + "_slab"));
            sidingBlocks.add(SidingBlock.ofBlock(fullBlock, slabBlock));
            mouldingBlocks.add(MouldingBlock.ofBlock(fullBlock, slabBlock));
            cornerBlocks.add(CornerBlock.ofBlock(fullBlock, slabBlock));
            columnBlocks.add(ColumnBlock.ofBlock(fullBlock, slabBlock));
            pedestalBlocks.add(PedestalBlock.ofBlock(fullBlock, slabBlock));
            tableBlocks.add(TableBlock.ofBlock(fullBlock, slabBlock));
        });
        List<Block> blockSlabPairs = List.of(
                Blocks.STONE, Blocks.STONE_SLAB,
                Blocks.STONE_BRICKS, Blocks.STONE_BRICK_SLAB,
                Blocks.SANDSTONE, Blocks.SANDSTONE_SLAB,
                Blocks.BRICKS, Blocks.BRICK_SLAB,
                Blocks.NETHER_BRICKS, Blocks.NETHER_BRICK_SLAB
        );
        for (int i = 0; i < blockSlabPairs.size() - 1; i += 2) {
            sidingBlocks.add(SidingBlock.ofBlock(blockSlabPairs.get(i), blockSlabPairs.get(i + 1)));
            mouldingBlocks.add(MouldingBlock.ofBlock(blockSlabPairs.get(i), blockSlabPairs.get(i + 1)));
            cornerBlocks.add(CornerBlock.ofBlock(blockSlabPairs.get(i), blockSlabPairs.get(i + 1)));
            columnBlocks.add(ColumnBlock.ofBlock(blockSlabPairs.get(i), blockSlabPairs.get(i + 1)));
            pedestalBlocks.add(PedestalBlock.ofBlock(blockSlabPairs.get(i), blockSlabPairs.get(i + 1)));
            tableBlocks.add(TableBlock.ofBlock(blockSlabPairs.get(i), blockSlabPairs.get(i + 1)));
        }
        for (int i = 0; i < sidingBlocks.size(); i++) {
            SidingBlock sidingBlock = sidingBlocks.get(i);
            MouldingBlock mouldingBlock = mouldingBlocks.get(i);
            CornerBlock cornerBlock = cornerBlocks.get(i);
            ColumnBlock columnBlock = columnBlocks.get(i);
            PedestalBlock pedestalBlock = pedestalBlocks.get(i);
            TableBlock tableBlock = tableBlocks.get(i);
            Identifier blockId = Registries.BLOCK.getId(sidingBlock.fullBlock);
            Identifier sidingId = new Identifier("bwt", blockId.getPath() + "_siding");
            Identifier mouldingId = new Identifier("bwt", blockId.getPath() + "_moulding");
            Identifier cornerId = new Identifier("bwt", blockId.getPath() + "_corner");
            Identifier columnId = new Identifier("bwt", blockId.getPath() + "_column");
            Identifier pedestalId = new Identifier("bwt", blockId.getPath() + "_pedestal");
            Identifier tableId = new Identifier("bwt", blockId.getPath() + "_table");
            Registry.register(Registries.BLOCK, sidingId, sidingBlock);
            Registry.register(Registries.BLOCK, mouldingId, mouldingBlock);
            Registry.register(Registries.BLOCK, cornerId, cornerBlock);
            Registry.register(Registries.BLOCK, columnId, columnBlock);
            Registry.register(Registries.BLOCK, pedestalId, pedestalBlock);
            Registry.register(Registries.BLOCK, tableId, tableBlock);
            Registry.register(Registries.ITEM, sidingId, new BlockItem(sidingBlock, new FabricItemSettings()));
            Registry.register(Registries.ITEM, mouldingId, new BlockItem(mouldingBlock, new FabricItemSettings()));
            Registry.register(Registries.ITEM, cornerId, new BlockItem(cornerBlock, new FabricItemSettings()));
            Registry.register(Registries.ITEM, columnId, new BlockItem(columnBlock, new FabricItemSettings()));
            Registry.register(Registries.ITEM, pedestalId, new BlockItem(pedestalBlock, new FabricItemSettings()));
            Registry.register(Registries.ITEM, tableId, new BlockItem(tableBlock, new FabricItemSettings()));
        }
    }
}
