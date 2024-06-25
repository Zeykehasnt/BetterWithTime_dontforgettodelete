package com.bwt.generation;

import com.bwt.items.BwtItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootingEnchantLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class EntityLootTableGenerator extends SimpleFabricLootTableProvider {
    public EntityLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        this(dataOutput, registryLookup, LootContextTypes.ENTITY);
    }

    public EntityLootTableGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup, LootContextType lootContextType) {
        super(output, registryLookup, lootContextType);
    }

    @Override
    public void accept(RegistryWrapper.WrapperLookup registryLookup, BiConsumer<RegistryKey<LootTable>, LootTable.Builder> consumer) {
        consumer.accept(
                EntityType.SKELETON.getLootTableId(),
                LootTable.builder()
                        .pool(LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0f))
                                .with(ItemEntry.builder(BwtItems.rottedArrowItem)
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 2.0f)))
                                        .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))
                                )
                        )
                        .pool(LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0f))
                                .with(ItemEntry.builder(Items.BONE)
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 2.0f)))
                                        .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f))))
                        )
        );
    }
}
