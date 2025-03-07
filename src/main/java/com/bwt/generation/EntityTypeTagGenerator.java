package com.bwt.generation;

import com.bwt.tags.BwtEntityTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagGenerator extends FabricTagProvider.EntityTypeTagProvider {
    public EntityTypeTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BwtEntityTags.BLOCK_DISPENSER_INHALE_ENTITIES)
                .add(EntityType.WOLF)
                .add(EntityType.CHICKEN)
                .add(EntityType.SHEEP)
                .add(EntityType.MINECART)
                .add(EntityType.FURNACE_MINECART)
                .add(EntityType.CHEST_MINECART)
                .add(EntityType.HOPPER_MINECART)
                .add(EntityType.SPAWNER_MINECART)
                .add(EntityType.TNT_MINECART)
                .add(EntityType.BOAT)
                .add(EntityType.CHEST_BOAT)
                .add(EntityType.ARMOR_STAND)
                .add(EntityType.ITEM_FRAME)
                .add(EntityType.GLOW_ITEM_FRAME);
    }
}
