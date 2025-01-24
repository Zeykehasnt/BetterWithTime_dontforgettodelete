package com.bwt.mixin;

import com.bwt.recipes.BwtRecipes;
import com.bwt.recipes.mob_spawner_conversion.MobSpawnerConversionRecipe;
import com.bwt.recipes.mob_spawner_conversion.MobSpawnerConversionRecipeInput;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.spawner.MobSpawnerLogic;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Optional;

@Mixin(MobSpawnerLogic.class)
public class SpawnerMossyCobbleMixin {
    @Unique
    private static final Map<BlockState, BlockState> REGULAR_TO_MOSSY_STATE_MAP = Maps.newIdentityHashMap();

    @Unique
    private static BlockState copyProperties(BlockState fromState, Block toBlock) {
        return REGULAR_TO_MOSSY_STATE_MAP.computeIfAbsent(fromState, infestedState -> {
            BlockState blockState = toBlock.getDefaultState();

            for (Property property : infestedState.getProperties()) {
                blockState = blockState.contains(property) ? blockState.with(property, infestedState.get(property)) : blockState;
            }

            return blockState;
        });
    }

    @Inject(method = "serverTick", at = @At("HEAD"))
    protected void bwt$createMossyCobblestone(ServerWorld world, BlockPos pos, CallbackInfo ci) {
        if (world.random.nextInt(1200) != 0) {
            return;
        }
        BlockPos randomPos = new BlockPos(
                pos.getX() + world.random.nextBetween(-4, 4),
                pos.getY() + world.random.nextBetween(-1, 4),
                pos.getZ() + world.random.nextBetween(-4, 4)
        );
        BlockState blockState = world.getBlockState(randomPos);
        if (blockState.isAir()) {
            return;
        }
        MobSpawnerConversionRecipeInput recipeInput = new MobSpawnerConversionRecipeInput(blockState.getBlock());
        Optional<MobSpawnerConversionRecipe> recipe = world.getRecipeManager().getFirstMatch(
                BwtRecipes.MOB_SPAWNER_CONVERSION_RECIPE_TYPE,
                recipeInput,
                world
        ).map(RecipeEntry::value);
        if (recipe.isEmpty()) {
            return;
        }

        Block blockToPlace = recipe.get().getResult();
        BlockState newState = copyProperties(blockState, blockToPlace);
        world.setBlockState(randomPos, newState);
    }
}
