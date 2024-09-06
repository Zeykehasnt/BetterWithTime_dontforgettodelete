package com.bwt.items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;

public class CompositeBowItem extends BowItem {
    public CompositeBowItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        RegistryWrapper.Impl<Enchantment> enchantmentRegistry = world.getRegistryManager().getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
        RegistryEntry.Reference<Enchantment> infinity = enchantmentRegistry.getOrThrow(Enchantments.INFINITY);
        RegistryEntry.Reference<Enchantment> punch = enchantmentRegistry.getOrThrow(Enchantments.PUNCH);
        RegistryEntry.Reference<Enchantment> power = enchantmentRegistry.getOrThrow(Enchantments.POWER);

        boolean bl2;
        if (!(user instanceof PlayerEntity playerEntity)) {
            return;
        }
        boolean infiniteArrows = playerEntity.getAbilities().creativeMode || EnchantmentHelper.getLevel(infinity, stack) > 0;
        ItemStack itemStack = playerEntity.getProjectileType(stack);
        if (itemStack.isEmpty() && !infiniteArrows) {
            return;
        }
        if (itemStack.isEmpty()) {
            itemStack = new ItemStack(Items.ARROW);
        }
        float pullProgress = BowItem.getPullProgress(this.getMaxUseTime(stack, user) - remainingUseTicks);
        if (pullProgress < 0.1f) {
            return;
        }
        bl2 = infiniteArrows && itemStack.isOf(Items.ARROW);
        ArrowItem arrowItem = (ArrowItem)(itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
        if (arrowItem instanceof RottedArrowItem) {
            fireRottedArrow(world, stack, itemStack, playerEntity);
            return;
        }
        if (!world.isClient) {
            int punchLevel = EnchantmentHelper.getLevel(punch, stack);
            int powerLevel = EnchantmentHelper.getLevel(power, stack);
            PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, itemStack, playerEntity);
            persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, pullProgress * 6.0f, 1.0f);
            if (pullProgress == 1.0f) {
                persistentProjectileEntity.setCritical(true);
            }
            if (powerLevel > 0) {
                persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() + (double) powerLevel * 0.5 + 0.5);
            }
            if (punchLevel > 0) {
                persistentProjectileEntity.setPunch(punchLevel);
            }
            if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                persistentProjectileEntity.setOnFireFor(100);
            }
            stack.damage(1, playerEntity, LivingEntity.getSlotForHand(playerEntity.getActiveHand()));
            if (bl2 || playerEntity.getAbilities().creativeMode && (itemStack.isOf(Items.SPECTRAL_ARROW) || itemStack.isOf(Items.TIPPED_ARROW))) {
                persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
            }
            world.spawnEntity(persistentProjectileEntity);
        }
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + pullProgress * 0.5f);
        if (!bl2 && !playerEntity.getAbilities().creativeMode) {
            itemStack.decrement(1);
            if (itemStack.isEmpty()) {
                playerEntity.getInventory().removeOne(itemStack);
            }
        }
        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    private void fireRottedArrow(World world, ItemStack bowStack, ItemStack arrowStack, PlayerEntity playerEntity) {
        if (!world.isClient) {
            EquipmentSlot slot = LivingEntity.getSlotForHand(playerEntity.getActiveHand());
            bowStack.damage(1, playerEntity, slot);
            if (playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                serverPlayerEntity.sendEquipmentBreakStatus(slot);
            }
        }
        arrowStack.decrement(1);
        if (arrowStack.isEmpty()) {
            playerEntity.getInventory().removeOne(arrowStack);
        }
        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    @Override
    public int getRange() {
        return super.getRange() * 2;
    }
}
