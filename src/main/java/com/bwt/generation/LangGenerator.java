package com.bwt.generation;

import com.bwt.blocks.BwtBlocks;
import com.bwt.blocks.MaterialInheritedBlock;
import com.bwt.gamerules.BwtGameRules;
import com.bwt.items.BwtItems;
import com.bwt.sounds.BwtSoundEvents;
import com.bwt.tags.BwtItemTags;
import com.bwt.utils.DyeUtils;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LangGenerator extends FabricLanguageProvider {
    public LangGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        addSubtitles(translationBuilder);
        addTagNames(translationBuilder);

        translationBuilder.add(BwtGameRules.VANILLA_HOPPERS_DISABLED.getTranslationKey(), "Disable Vanilla Hopper Transfer");
        BwtBlocks.sidingBlocks.forEach(block -> addMaterialBlockName(translationBuilder, block, "siding"));
        BwtBlocks.mouldingBlocks.forEach(block -> addMaterialBlockName(translationBuilder, block, "moulding"));
        BwtBlocks.cornerBlocks.forEach(block -> addMaterialBlockName(translationBuilder, block, "corner"));
        BwtBlocks.columnBlocks.forEach(block -> addMaterialBlockName(translationBuilder, block, "column"));
        BwtBlocks.pedestalBlocks.forEach(block -> addMaterialBlockName(translationBuilder, block, "pedestal"));
        BwtBlocks.tableBlocks.forEach(block -> addMaterialBlockName(translationBuilder, block, "table"));
        translationBuilder.add(BwtBlocks.bloodWoodBlocks.logBlock, "Blood Wood Log");
        translationBuilder.add(BwtBlocks.bloodWoodBlocks.strippedLogBlock, "Stripped Blood Wood Log");
        translationBuilder.add(BwtBlocks.bloodWoodBlocks.woodBlock, "Blood Wood");
        translationBuilder.add(BwtBlocks.bloodWoodBlocks.strippedWoodBlock, "Stripped Blood Wood");
        translationBuilder.add(BwtBlocks.bloodWoodBlocks.leavesBlock, "Blood Wood Leaves");
        translationBuilder.add(BwtBlocks.bloodWoodBlocks.saplingBlock, "Blood Wood Sapling");

        translationBuilder.add(BwtBlocks.bloodWoodBlocks.planksBlock, "Blood Wood Planks");
        translationBuilder.add(BwtBlocks.bloodWoodBlocks.buttonBlock, "Blood Wood Button");
        translationBuilder.add(BwtBlocks.bloodWoodBlocks.fenceBlock, "Blood Wood Fence");
        translationBuilder.add(BwtBlocks.bloodWoodBlocks.fenceGateBlock, "Blood Wood Fence Gate");
        translationBuilder.add(BwtBlocks.bloodWoodBlocks.pressurePlateBlock, "Blood Wood Pressure Plate");
        translationBuilder.add(BwtBlocks.bloodWoodBlocks.signBlock, "Blood Wood Sign");
        translationBuilder.add(BwtBlocks.bloodWoodBlocks.hangingSignBlock, "Blood Wood Hanging Sign");
        translationBuilder.add(BwtBlocks.bloodWoodBlocks.slabBlock, "Blood Wood Slab");
        translationBuilder.add(BwtBlocks.bloodWoodBlocks.stairsBlock, "Blood Wood Stairs");
        translationBuilder.add(BwtBlocks.bloodWoodBlocks.doorBlock, "Blood Wood Door");
        translationBuilder.add(BwtBlocks.bloodWoodBlocks.trapdoorBlock, "Blood Wood Trapdoor");
        translationBuilder.add(BwtBlocks.unfiredCrucibleBlock, "Unfired Crucible");
        translationBuilder.add(BwtBlocks.unfiredPlanterBlock, "Unfired Planter");
        translationBuilder.add(BwtBlocks.unfiredVaseBlock, "Unfired Vase");
        translationBuilder.add(BwtBlocks.unfiredUrnBlock, "Unfired Urn");
        translationBuilder.add(BwtBlocks.unfiredMouldBlock, "Unfired Mould");
        translationBuilder.add(BwtBlocks.wickerBlock, "Wicker Block");
        translationBuilder.add(BwtBlocks.wickerSlabBlock    , "Wicker Slab");
        translationBuilder.add(BwtBlocks.ropeCoilBlock, "Rope Block");
        translationBuilder.add(BwtBlocks.paddingBlock, "Padding Block");
        translationBuilder.add(BwtBlocks.dungBlock, "Dung Block");
        translationBuilder.add(BwtBlocks.concentratedHellfireBlock, "Concentrated Hellfire Block");
        translationBuilder.add(BwtBlocks.soapBlock, "Soap Block");
        BwtBlocks.vaseBlocks.entrySet().stream().sorted(DyeUtils.COMPARE_DYE_COLOR_ENTRY).forEach(entry -> translationBuilder.add(entry.getValue(), nameKeyToTitleCase(entry.getKey().getName() + "_vase")));
        BwtBlocks.woolSlabBlocks.entrySet().stream().sorted(DyeUtils.COMPARE_DYE_COLOR_ENTRY).forEach(entry -> translationBuilder.add(entry.getValue(), nameKeyToTitleCase(entry.getKey().getName() + "_wool_slab")));
        translationBuilder.add(BwtBlocks.soilPlanterBlock, "Soil Planter");
        translationBuilder.add(BwtBlocks.soulSandPlanterBlock, "Soul Sand Planter");
        translationBuilder.add(BwtBlocks.grassPlanterBlock, "Grass Planter");
        translationBuilder.add(BwtBlocks.stoneDetectorRailBlock, "Stone Detector Rail");
        translationBuilder.add(BwtBlocks.obsidianDetectorRailBlock, "Obsidian Detector Rail");
        translationBuilder.add(BwtBlocks.soulForgeBlock, "Soul Forge");
        translationBuilder.add(BwtBlocks.vineTrapBlock, "Vine Trap");
        translationBuilder.add(BwtItems.rawEggItem, "Raw Egg");
        translationBuilder.add(BwtItems.screwItem, "Screw");
        translationBuilder.add(BwtItems.breedingHarnessItem, "Breeding Harness");
        translationBuilder.add(BwtItems.netheriteMattockItem, "Netherite Mattock");
        translationBuilder.add(BwtItems.netheriteBattleAxeItem, "Netherite Battle Axe");

        translationBuilder.add("emi.category.bwt.crucible", "Crucible");
        translationBuilder.add("emi.category.bwt.stoked_crucible", "Stoked Crucible");
        translationBuilder.add("emi.category.bwt.cauldron", "Cauldron");
        translationBuilder.add("emi.category.bwt.stoked_cauldron", "Stoked Cauldron");
        translationBuilder.add("emi.category.bwt.mill_stone", "Mill Stone");
        translationBuilder.add("emi.category.bwt.saw", "Saw");
        translationBuilder.add("emi.category.bwt.turntable", "Turntable");
        translationBuilder.add("emi.category.bwt.kiln", "Kiln");

        // Load an existing language file.
        try {
            Path existingFilePath = dataOutput.getModContainer().findPath("assets/bwt/lang/en_us.existing.json").get();
            translationBuilder.add(existingFilePath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add existing language file!", e);
        }
    }

    protected void addTagNames(TranslationBuilder translationBuilder) {
        addTagName(BwtItemTags.SIDING_BLOCKS, "Siding Blocks", translationBuilder);
        addTagName(BwtItemTags.WOODEN_SIDING_BLOCKS, "Wooden Siding Blocks", translationBuilder);
        addTagName(BwtItemTags.MOULDING_BLOCKS, "Moulding Blocks", translationBuilder);
        addTagName(BwtItemTags.WOODEN_MOULDING_BLOCKS, "Wooden Moulding Blocks", translationBuilder);
        addTagName(BwtItemTags.CORNER_BLOCKS, "Corner Blocks", translationBuilder);
        addTagName(BwtItemTags.VASES, "Vases", translationBuilder);
        addTagName(BwtItemTags.WOOL_SLABS, "Wool Slabs", translationBuilder);
        addTagName(BwtItemTags.WOODEN_CORNER_BLOCKS, "Wooden Corner Blocks", translationBuilder);
        addTagName(BwtItemTags.PASSES_WICKER_FILTER, "Passes Wicker Filter", translationBuilder);
        addTagName(BwtItemTags.PASSES_GRATE_FILTER, "Passes Grate Filter", translationBuilder);
        addTagName(BwtItemTags.PASSES_SLATS_FILTER, "Passes Slats Filter", translationBuilder);
        addTagName(BwtItemTags.PASSES_TRAPDOOR_FILTER, "Passes Trapdoor Filter", translationBuilder);
        addTagName(BwtItemTags.PASSES_IRON_BARS_FILTER, "Passes Iron Bars Filter", translationBuilder);
        addTagName(BwtItemTags.PASSES_LADDER_FILTER, "Passes Ladder Filter", translationBuilder);
        addTagName(BwtItemTags.STOKED_EXPLOSIVES, "Stoked Explosives", translationBuilder);
        addTagName(BwtItemTags.SAW_DUSTS, "Saw Dusts", translationBuilder);
        addTagName(BwtItemTags.MINING_CHARGE_IMMUNE, "Mining Charge Immune", translationBuilder);
        addTagName(BwtItemTags.BLOOD_WOOD_LOGS, "Blood Wood Logs", translationBuilder);
    }

    protected void addSubtitles(TranslationBuilder translationBuilder) {
        addSubtitle(BwtSoundEvents.MECH_BANG, "Mechanical device operates", translationBuilder);
        addSubtitle(BwtSoundEvents.MECH_EXPLODE, "Mechanical device explodes", translationBuilder);
        addSubtitle(BwtSoundEvents.MECH_CREAK, "Mechanical device creaks", translationBuilder);
        addSubtitle(BwtSoundEvents.ANCHOR_RETRACT, "Anchor retracts rope", translationBuilder);
        addSubtitle(BwtSoundEvents.BELLOWS_COMPRESS, "Bellows compresses", translationBuilder);
        addSubtitle(BwtSoundEvents.COMPANION_CUBE_DEATH, "Companion cube dies. You monster.", translationBuilder);
        addSubtitle(BwtSoundEvents.GEAR_BOX_ACTIVATE, "Gear box activates", translationBuilder);
        addSubtitle(BwtSoundEvents.HAND_CRANK_CLICK, "Hand crank activates", translationBuilder);
        addSubtitle(BwtSoundEvents.HIBACHI_IGNITE, "Hibachi ignites", translationBuilder);
        addSubtitle(BwtSoundEvents.DETECTOR_CLICK, "Detector block toggles", translationBuilder);
        addSubtitle(BwtSoundEvents.BUDDY_CLICK, "Buddy block toggles", translationBuilder);
        addSubtitle(BwtSoundEvents.BLOOD_WOOD_MOAN, "Blood wood moans", translationBuilder);
        addSubtitle(BwtSoundEvents.SOUL_CONVERSION, "Souls transmogrify", translationBuilder);
        addSubtitle(BwtSoundEvents.MILL_STONE_GRIND, "Mill stone grinds", translationBuilder);
        addSubtitle(BwtSoundEvents.TURNTABLE_SETTING_CLICK, "Turntable setting changes", translationBuilder);
        addSubtitle(BwtSoundEvents.TURNTABLE_TURNING_CLICK, "Turntable rotates", translationBuilder);
        addSubtitle(BwtSoundEvents.WOLF_DUNG_PRODUCTION, "Wolf produces dung", translationBuilder);
        addSubtitle(BwtSoundEvents.WOLF_DUNG_EFFORT, "Wolf growls", translationBuilder);
        addSubtitle(BwtSoundEvents.DYNAMITE_THROW, "Dynamite thrown", translationBuilder);
        addSubtitle(BwtSoundEvents.DYNAMITE_IGNITE, "Dynamite ignited", translationBuilder);
        addSubtitle(BwtSoundEvents.MINING_CHARGE_PRIME, "Mining charge primed", translationBuilder);
    }

    protected void addSubtitle(SoundEvent soundEvent, String value, TranslationBuilder translationBuilder) {
        translationBuilder.add(soundEvent.getId().withPrefixedPath("subtitles."), value);
    }

    protected void addTagName(TagKey<Item> tagKey, String value, TranslationBuilder translationBuilder) {
        translationBuilder.add(tagKey, value);
    }

    protected void addMaterialBlockName(TranslationBuilder translationBuilder, MaterialInheritedBlock materialInheritedBlock, String suffix) {
        translationBuilder.add(materialInheritedBlock, nameKeyToTitleCase(materialInheritedBlock.fullBlock.getName().getString().replaceFirst("_planks", "") + "_" + suffix));
    }

    public static String nameKeyToTitleCase(String snakeString) {
        String[] split = snakeString.split("\\.");
        snakeString = split[split.length - 1];
        return Arrays.stream(snakeString.split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
    }
}
