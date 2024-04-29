package com.bwt.generation;

import com.bwt.recipes.DisabledRecipe;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DisabledVanilaRecipeGenerator extends FabricRecipeProvider {
    final FabricDataOutput.PathResolver recipesPathResolver;
    final FabricDataOutput.PathResolver advancementsPathResolver;

    public DisabledVanilaRecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
        this.recipesPathResolver = output.getResolver(FabricDataOutput.OutputType.DATA_PACK, "recipes");
        this.advancementsPathResolver = output.getResolver(FabricDataOutput.OutputType.DATA_PACK, "advancements");
    }

    @Override
    public void generate(RecipeExporter exporter) {/* this is not called */}

    public void disableRecipes(RecipeExporter exporter) {
        disableVanilla(Items.BONE_MEAL, exporter);
        disableVanilla(Items.BREAD, exporter);

        disableVanilla(Items.NETHERITE_INGOT, exporter);
        disableVanilla(Items.NETHERITE_INGOT, "_from_netherite_block", exporter);
        disableVanilla(Items.NETHERITE_BLOCK, exporter);
        disableVanilla(Items.NETHERITE_PICKAXE, "_smithing", exporter);
        disableVanilla(Items.NETHERITE_SHOVEL, "_smithing",exporter);
        disableVanilla(Items.NETHERITE_AXE, "_smithing",exporter);
        disableVanilla(Items.NETHERITE_HOE, "_smithing",exporter);
        disableVanilla(Items.NETHERITE_SWORD, "_smithing",exporter);
        disableVanilla(Items.NETHERITE_HELMET, "_smithing", exporter);
        disableVanilla(Items.NETHERITE_CHESTPLATE, "_smithing", exporter);
        disableVanilla(Items.NETHERITE_LEGGINGS, "_smithing", exporter);
        disableVanilla(Items.NETHERITE_BOOTS, "_smithing", exporter);

        disableVanilla(Items.MOSSY_COBBLESTONE, "_from_moss_block", exporter);
        disableVanilla(Items.MOSSY_COBBLESTONE, "_from_vine", exporter);
    }

    public void disableVanilla(String recipeId, RecipeExporter exporter) {
        exporter.accept(new Identifier(recipeId), new DisabledRecipe(),null);
    }

    public void disableVanilla(ItemConvertible itemConvertible, RecipeExporter exporter) {
        disableVanilla(Registries.ITEM.getId(itemConvertible.asItem()).getPath(), exporter);
    }

    public void disableVanilla(ItemConvertible itemConvertible, String suffix, RecipeExporter exporter) {
        disableVanilla(Registries.ITEM.getId(itemConvertible.asItem()).withSuffixedPath(suffix).getPath(), exporter);
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer, RegistryWrapper.WrapperLookup wrapperLookup) {
        Set<Identifier> generatedRecipes = Sets.newHashSet();
        List<CompletableFuture<?>> list = new ArrayList<>();
        disableRecipes(new RecipeExporter() {
            @Override
            public void accept(Identifier recipeId, @Nullable Recipe<?> recipe, @Nullable AdvancementEntry advancement) {
                Identifier identifier = getRecipeIdentifier(recipeId);

                if (!generatedRecipes.add(identifier)) {
                    throw new IllegalStateException("Duplicate recipe " + identifier);
                }

                RegistryOps<JsonElement> registryOps = wrapperLookup.getOps(JsonOps.INSTANCE);
                JsonObject recipeJson = Recipe.CODEC.encodeStart(registryOps, recipe).getOrThrow(IllegalStateException::new).getAsJsonObject();
                ResourceCondition[] conditions = FabricDataGenHelper.consumeConditions(recipe);
                FabricDataGenHelper.addConditions(recipeJson, conditions);

                list.add(DataProvider.writeToPath(writer, recipeJson, recipesPathResolver.resolveJson(identifier)));

                if (advancement != null) {
                    JsonObject advancementJson = Advancement.CODEC.encodeStart(registryOps, advancement.value()).getOrThrow(IllegalStateException::new).getAsJsonObject();
                    FabricDataGenHelper.addConditions(advancementJson, conditions);
                    list.add(DataProvider.writeToPath(writer, advancementJson, advancementsPathResolver.resolveJson(getRecipeIdentifier(advancement.id()))));
                }
            }

            @Override
            public Advancement.Builder getAdvancementBuilder() {
                return Advancement.Builder.createUntelemetered();
            }
        });
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    @Override
    protected Identifier getRecipeIdentifier(Identifier identifier) {
        return identifier;
    }
}
