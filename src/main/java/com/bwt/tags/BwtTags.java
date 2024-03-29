package com.bwt.tags;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class BwtTags implements ModInitializer {
    public static final TagKey<Block> BLOCK_DISPENSER_INHALE_NOOP = TagKey.of(RegistryKeys.BLOCK, new Identifier("bwt", "block_dispenser_inhale_noop"));

    @Override
    public void onInitialize() {

    }
}
