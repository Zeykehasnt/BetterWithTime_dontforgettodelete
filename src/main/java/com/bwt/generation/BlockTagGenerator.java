package com.bwt.generation;

import com.bwt.blocks.BwtBlocks;
import com.bwt.blocks.CornerBlock;
import com.bwt.blocks.MouldingBlock;
import com.bwt.blocks.SidingBlock;
import com.bwt.tags.BwtBlockTags;
import com.bwt.utils.DyeUtils;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends FabricTagProvider.BlockTagProvider {
    public BlockTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BlockTags.REPLACEABLE).add(BwtBlocks.detectorLogicBlock);
        getOrCreateTagBuilder(BlockTags.CLIMBABLE).add(BwtBlocks.ropeBlock);
        getOrCreateTagBuilder(BlockTags.RAILS).add(BwtBlocks.stoneDetectorRailBlock, BwtBlocks.obsidianDetectorRailBlock);
        getOrCreateTagBuilder(BlockTags.PRESSURE_PLATES).add(BwtBlocks.obsidianPressuePlateBlock);
        getOrCreateTagBuilder(BlockTags.SLABS).add(BwtBlocks.companionSlabBlock);

        // Make planters behave like their corresponding blocks
        getOrCreateTagBuilder(BlockTags.DIRT).add(BwtBlocks.soilPlanterBlock, BwtBlocks.grassPlanterBlock);
        getOrCreateTagBuilder(BlockTags.SOUL_FIRE_BASE_BLOCKS).add(BwtBlocks.soulSandPlanterBlock);
        getOrCreateTagBuilder(BlockTags.SOUL_SPEED_BLOCKS).add(BwtBlocks.soulSandPlanterBlock);
        getOrCreateTagBuilder(BwtBlockTags.CROPS_CAN_PLANT_ON).add(Blocks.FARMLAND, BwtBlocks.soilPlanterBlock);
        getOrCreateTagBuilder(BwtBlockTags.SOUL_SAND_PLANTS_CAN_PLANT_ON).add(Blocks.SOUL_SAND, BwtBlocks.soulSandPlanterBlock);

        getOrCreateTagBuilder(BwtBlockTags.BLOCK_DISPENSER_INHALE_VOID)
                .add(Blocks.NETHER_PORTAL);
        getOrCreateTagBuilder(BwtBlockTags.BLOCK_DISPENSER_INHALE_NOOP)
                .add(Blocks.STRUCTURE_VOID)
                .add(Blocks.STRUCTURE_BLOCK)
                .add(Blocks.PISTON_HEAD)
                .add(Blocks.MOVING_PISTON)
                .add(Blocks.END_PORTAL)
                .forceAddTag(BlockTags.WITHER_IMMUNE)
                .forceAddTag(BlockTags.FIRE);
        getOrCreateTagBuilder(BwtBlockTags.DETECTABLE_SMALL_CROPS)
                .add(Blocks.WHEAT)
                .add(Blocks.CARROTS)
                .add(Blocks.POTATOES);
        getOrCreateTagBuilder(BlockTags.INFINIBURN_OVERWORLD).add(BwtBlocks.hibachiBlock);
        getOrCreateTagBuilder(BlockTags.INFINIBURN_NETHER).add(BwtBlocks.hibachiBlock);
        getOrCreateTagBuilder(BlockTags.INFINIBURN_END).add(BwtBlocks.hibachiBlock);
        getOrCreateTagBuilder(BlockTags.FIRE).add(BwtBlocks.stokedFireBlock);
        getOrCreateTagBuilder(BlockTags.WOOL).add(BwtBlocks.companionCubeBlock, BwtBlocks.companionSlabBlock);

        addTools();
        addMiniBlockTags();
        addVases();
        addSawTags();
    }

    private void addTools() {
        BwtBlocks.sidingBlocks.forEach(sidingBlock -> getOrCreateTagBuilder(sidingBlock.isWood() ? BlockTags.AXE_MINEABLE : BlockTags.PICKAXE_MINEABLE).add(sidingBlock));
        BwtBlocks.mouldingBlocks.forEach(mouldingBlock -> getOrCreateTagBuilder(mouldingBlock.isWood() ? BlockTags.AXE_MINEABLE : BlockTags.PICKAXE_MINEABLE).add(mouldingBlock));
        BwtBlocks.cornerBlocks.forEach(cornerBlock -> getOrCreateTagBuilder(cornerBlock.isWood() ? BlockTags.AXE_MINEABLE : BlockTags.PICKAXE_MINEABLE).add(cornerBlock));

        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(BwtBlocks.anchorBlock)
//                .add(BwtBlocks.anvilBlock)
                .add(BwtBlocks.blockDispenserBlock)
//                .add(BwtBlocks.buddyBlockBlock)
                .add(BwtBlocks.cauldronBlock)
//                .add(BwtBlocks.columnBlock)
                .add(BwtBlocks.crucibleBlock)
                .add(BwtBlocks.detectorBlock)
                .add(BwtBlocks.handCrankBlock)
                .add(BwtBlocks.hibachiBlock)
//                .add(BwtBlocks.infernalEnchanterBlock)
                .add(BwtBlocks.kilnBlock)
//                .add(BwtBlocks.lensBlock)
                .add(BwtBlocks.lightBlockBlock)
                .add(BwtBlocks.millStoneBlock)
                .add(BwtBlocks.obsidianDetectorRailBlock)
                .add(BwtBlocks.obsidianPressuePlateBlock)
//                .add(BwtBlocks.pedestalBlock)
                .add(BwtBlocks.planterBlock)
                .add(BwtBlocks.soilPlanterBlock)
                .add(BwtBlocks.soulSandPlanterBlock)
                .add(BwtBlocks.grassPlanterBlock)
                .add(BwtBlocks.stoneDetectorRailBlock)
                .add(BwtBlocks.turntableBlock)
                .add(BwtBlocks.urnBlock);

        getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE)
                .add(BwtBlocks.unfiredCrucibleBlock)
                .add(BwtBlocks.unfiredPlanterBlock)
                .add(BwtBlocks.unfiredVaseBlock)
                .add(BwtBlocks.unfiredUrnBlock)
                .add(BwtBlocks.unfiredMouldBlock);

        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .add(BwtBlocks.axleBlock)
                .add(BwtBlocks.axlePowerSourceBlock)
                .add(BwtBlocks.bellowsBlock)
//                .add(BwtBlocks.bloodWoodBlock)
                .add(BwtBlocks.gearBoxBlock)
                .add(BwtBlocks.grateBlock)
                .add(BwtBlocks.hopperBlock)
                .add(BwtBlocks.platformBlock)
                .add(BwtBlocks.pulleyBlock)
                .add(BwtBlocks.ropeBlock)
                .add(BwtBlocks.sawBlock)
                .add(BwtBlocks.slatsBlock)
//                .add(BwtBlocks.screwPumpBlock)
//                .add(BwtBlocks.tableBlock)
                .add(BwtBlocks.wickerBlock);

        // Where do these go?
//        .add(BwtBlocks.miningChargeBlock)
//        .add(BwtBlocks.netherGrothBlock)
//        .add(BwtBlocks.stakeBlock)

        getOrCreateTagBuilder(BlockTags.SWORD_EFFICIENT).add(BwtBlocks.ropeBlock, BwtBlocks.hempCropBlock);
    }

    protected void addMiniBlockTags() {
        FabricTagBuilder woodenSidingBuilder = getOrCreateTagBuilder(BwtBlockTags.WOODEN_SIDING_BLOCKS);
        FabricTagBuilder woodenMouldingBuilder = getOrCreateTagBuilder(BwtBlockTags.WOODEN_MOULDING_BLOCKS);
        FabricTagBuilder woodenCornerBuilder = getOrCreateTagBuilder(BwtBlockTags.WOODEN_CORNER_BLOCKS);
        FabricTagBuilder sidingBuilder = getOrCreateTagBuilder(BwtBlockTags.SIDING_BLOCKS);
        FabricTagBuilder mouldingBuilder = getOrCreateTagBuilder(BwtBlockTags.MOULDING_BLOCKS);
        FabricTagBuilder cornerBuilder = getOrCreateTagBuilder(BwtBlockTags.CORNER_BLOCKS);
        BwtBlocks.sidingBlocks.stream().filter(SidingBlock::isWood).forEach(woodenSidingBuilder::add);
        BwtBlocks.mouldingBlocks.stream().filter(MouldingBlock::isWood).forEach(woodenMouldingBuilder::add);
        BwtBlocks.cornerBlocks.stream().filter(CornerBlock::isWood).forEach(woodenCornerBuilder::add);
        sidingBuilder.addTag(BwtBlockTags.WOODEN_SIDING_BLOCKS);
        mouldingBuilder.addTag(BwtBlockTags.WOODEN_MOULDING_BLOCKS);
        cornerBuilder.addTag(BwtBlockTags.WOODEN_CORNER_BLOCKS);
        BwtBlocks.sidingBlocks.stream().filter(sidingBlock -> !sidingBlock.isWood()).forEach(sidingBuilder::add);
        BwtBlocks.mouldingBlocks.stream().filter(mouldingBlock -> !mouldingBlock.isWood()).forEach(mouldingBuilder::add);
        BwtBlocks.cornerBlocks.stream().filter(cornerBlock -> !cornerBlock.isWood()).forEach(cornerBuilder::add);
    }

    private void addVases() {
        FabricTagBuilder vasesBuilder = getOrCreateTagBuilder(BwtBlockTags.VASES);
        DyeUtils.streamColorItemsSorted(BwtBlocks.vaseBlocks).forEach(vasesBuilder::add);
    }

    protected void addSawTags() {
        getOrCreateTagBuilder(BwtBlockTags.SURVIVES_SAW_BLOCK)
                .add(BwtBlocks.companionSlabBlock)
                .add(Blocks.MELON_STEM)
                .add(Blocks.PUMPKIN_STEM)
                .add(Blocks.ATTACHED_MELON_STEM)
                .add(Blocks.ATTACHED_PUMPKIN_STEM)
                .add(Blocks.FROGSPAWN)
                .forceAddTag(BlockTags.FIRE)
                .forceAddTag(BlockTags.SAPLINGS);

        getOrCreateTagBuilder(BwtBlockTags.SAW_BREAKS_DROPS_LOOT)
                .forceAddTag(BlockTags.FLOWERS)
                .forceAddTag(BlockTags.LEAVES)
                .forceAddTag(BlockTags.WOODEN_DOORS)
                .forceAddTag(BlockTags.WOODEN_TRAPDOORS)
                .forceAddTag(BlockTags.WOODEN_BUTTONS)
                .forceAddTag(BlockTags.WOODEN_PRESSURE_PLATES)
                .forceAddTag(BlockTags.ALL_SIGNS)
                .forceAddTag(BlockTags.BANNERS)
                .forceAddTag(BlockTags.CANDLE_CAKES)
                .forceAddTag(BlockTags.CORAL_PLANTS)
                .forceAddTag(BlockTags.CORAL_PLANTS)
                .add(Blocks.BAMBOO)
                .add(Blocks.BARREL)
                .add(Blocks.BEEHIVE)
                .add(Blocks.BEETROOTS)
                .add(Blocks.BIG_DRIPLEAF)
                .add(Blocks.BIG_DRIPLEAF_STEM)
                .add(Blocks.BOOKSHELF)
                .add(Blocks.CACTUS)
                .add(Blocks.CAKE)
                .add(Blocks.CAMPFIRE)
                .add(Blocks.CARROTS)
                .add(Blocks.CARVED_PUMPKIN)
                .add(Blocks.CHEST)
                .add(Blocks.CHISELED_BOOKSHELF)
                .add(Blocks.CHORUS_PLANT)
                .add(Blocks.COBWEB)
                .add(Blocks.COCOA)
                .add(Blocks.COMPOSTER)
                .add(Blocks.CRAFTING_TABLE)
                .add(Blocks.CRIMSON_FUNGUS)
                .add(Blocks.CAVE_VINES)
                .add(Blocks.CAVE_VINES_PLANT)
                .add(Blocks.FERN)
                .add(Blocks.JACK_O_LANTERN)
                .add(Blocks.JUKEBOX)
                .add(Blocks.KELP)
                .add(Blocks.KELP_PLANT)
                .add(Blocks.LARGE_FERN)
                .add(Blocks.LECTERN)
                .add(Blocks.LILY_PAD)
                .add(Blocks.MANGROVE_ROOTS)
                .add(Blocks.MELON)
                .add(Blocks.POTATOES)
                .add(Blocks.PITCHER_CROP)
                .add(Blocks.PITCHER_PLANT)
                .add(Blocks.PUMPKIN)
                .add(Blocks.COMPARATOR)
                .add(Blocks.REPEATER)
                .add(Blocks.REDSTONE_TORCH)
                .add(Blocks.REDSTONE_WALL_TORCH)
                .add(Blocks.REDSTONE_WIRE)
                .add(Blocks.SEA_PICKLE)
                .add(Blocks.SEAGRASS)
                .add(Blocks.SMALL_DRIPLEAF)
                .add(Blocks.SOUL_CAMPFIRE)
                .add(Blocks.SOUL_TORCH)
                .add(Blocks.SOUL_WALL_TORCH)
                .add(Blocks.SPORE_BLOSSOM)
                .add(Blocks.SUGAR_CANE)
                .add(Blocks.TALL_GRASS)
                .add(Blocks.TALL_SEAGRASS)
                .add(Blocks.TORCH)
                .add(Blocks.TORCHFLOWER)
                .add(Blocks.TORCHFLOWER_CROP)
                .add(Blocks.TRAPPED_CHEST)
                .add(Blocks.TRIPWIRE)
                .add(Blocks.TURTLE_EGG)
                .add(Blocks.TWISTING_VINES)
                .add(Blocks.TWISTING_VINES_PLANT)
                .add(Blocks.WALL_TORCH)
                .add(Blocks.WARPED_FUNGUS)
                .add(Blocks.WEEPING_VINES)
                .add(Blocks.WEEPING_VINES_PLANT)
                .add(Blocks.WHEAT)
                .add(BwtBlocks.grateBlock)
                .add(BwtBlocks.slatsBlock)
                .add(BwtBlocks.wickerBlock)
                .add(BwtBlocks.hempCropBlock)
                .add(BwtBlocks.companionSlabBlock);
    }
}
