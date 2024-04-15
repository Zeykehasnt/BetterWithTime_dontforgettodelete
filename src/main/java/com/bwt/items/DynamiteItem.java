package com.bwt.items;

import com.bwt.entities.DynamiteEntity;
import com.bwt.sounds.BwtSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DynamiteItem extends Item {
    public DynamiteItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), BwtSoundEvents.DYNAMITE_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        user.getItemCooldownManager().set(this, 20);
        if (!world.isClient) {
            DynamiteEntity dynamiteEntity = new DynamiteEntity(world, user);
            if (user.getInventory().containsAny(otherStack -> otherStack.isOf(Items.FLINT_AND_STEEL))) {
                dynamiteEntity.ignite();
            }
            dynamiteEntity.setItem(itemStack);
            dynamiteEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.0f, 1.0f);
            world.spawnEntity(dynamiteEntity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }
}
