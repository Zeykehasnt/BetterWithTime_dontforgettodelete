package com.bwt.blocks;

import com.bwt.sounds.BwtSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class CompanionCubeBlock extends SimpleFacingBlock {

    public CompanionCubeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        addHearts(world, pos);
    }

    public void addHearts(World world, BlockPos pos) {
        for( int tempCount = 0; tempCount < 7; tempCount++)
        {
            double d = world.random.nextGaussian() * 0.02D;
            double d1 = world.random.nextGaussian() * 0.02D;
            double d2 = world.random.nextGaussian() * 0.02D;

            world.addParticle(ParticleTypes.HEART,
                    (double) pos.getX() + world.random.nextDouble(),
                    (double)(pos.getY() + 1 ) + world.random.nextDouble(),
                    ((double)pos.getZ()) + world.random.nextDouble(),
                    d, d1, d2);
        }
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        world.playSound(null, pos, BwtSoundEvents.COMPANION_CUBE_WHINE, SoundCategory.BLOCKS, 0.5f, 2.6F + (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.8F);
    }
}
