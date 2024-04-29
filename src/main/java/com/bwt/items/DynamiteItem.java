package com.bwt.items;

import com.bwt.entities.DynamiteEntity;
import com.bwt.sounds.BwtSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ProjectileItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class DynamiteItem extends Item implements ProjectileItem {
    public DynamiteItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), BwtSoundEvents.DYNAMITE_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        user.getItemCooldownManager().set(this, 20);
        if (!world.isClient && user instanceof ServerPlayerEntity serverUser) {
            DynamiteEntity dynamiteEntity = new DynamiteEntity(world, user);
            dynamiteEntity.setItem(itemStack);
            dynamiteEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.0f, 1.0f);

            for (int i = 0; i < user.getInventory().size(); ++i) {
                ItemStack otherStack = user.getInventory().getStack(i);
                if (!otherStack.isOf(Items.FLINT_AND_STEEL)) {
                    continue;
                }
                if (!serverUser.getAbilities().creativeMode) {
                    otherStack.damage(1, user, PlayerEntity.getSlotForHand(user.getActiveHand()));
                }
                dynamiteEntity.ignite();
                break;
            }

            world.spawnEntity(dynamiteEntity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        DynamiteEntity dynamiteEntity = new DynamiteEntity(pos.getX(), pos.getY(), pos.getZ(), world);
        dynamiteEntity.setItem(stack);
        dynamiteEntity.ignite();
        return dynamiteEntity;
    }

    @Override
    public ProjectileItem.Settings getProjectileSettings() {
        return ProjectileItem.Settings.builder()
                .power(1.0f)
                .uncertainty(1.0f)
                .build();
    }
}
