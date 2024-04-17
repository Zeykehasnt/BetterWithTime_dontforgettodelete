package com.bwt.blocks.mining_charge;

import com.bwt.entities.MiningChargeEntity;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.EntityExplosionBehavior;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.NotNull;

public class MiningChargeExplosion extends Explosion {
    protected final World world;
    protected final ExplosionBehavior behavior;
    protected final MiningChargeEntity miningChargeEntity;

    public MiningChargeExplosion(World world, @NotNull MiningChargeEntity entity, float power, boolean createFire, Explosion.DestructionType destructionType, ParticleEffect particle, ParticleEffect emitterParticle, SoundEvent soundEvent) {
        super(world, entity, Explosion.createDamageSource(world, entity), null, entity.getX(), entity.getY(), entity.getZ(), power, createFire, destructionType, particle, emitterParticle, soundEvent);
        this.world = world;
        this.behavior = new EntityExplosionBehavior(entity);
        this.miningChargeEntity = entity;
    }

    public void collectBlocksAndDamageEntities() {
        super.collectBlocksAndDamageEntities();
        getAffectedBlocks().clear();

        BlockPos entityBlockPos = BlockPos.ofFloored(getPosition());

        if (!canDestroyBlock(entityBlockPos)) {
            // we are in a block that's too tough to destroy.  Abort.
            return;
        }

        // offset the blast so that it is centered on the block to which we are attached
        BlockPos targetPos = entityBlockPos.offset(miningChargeEntity.getFacing().getOpposite());

        if (canDestroyBlock(targetPos)) {
            // we are attached to a block that's too tough to destroy.  Center the blast on the charge's
            // position
            targetPos = BlockPos.ofFloored(getPosition());
        }


        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    BlockPos tempPos = targetPos.add(i, j, k);
                    if (canDestroyBlock(tempPos)) {
                        getAffectedBlocks().add(tempPos);
                    }
                }
            }
        }

        // resolve the extra block of penetration towards our facing.

        targetPos = targetPos.offset(miningChargeEntity.getFacing().getOpposite());

        if (!canDestroyBlock(targetPos)) {
            // the block between the source and extra block is too tough, abort
            return;
        }

        targetPos = targetPos.offset(miningChargeEntity.getFacing().getOpposite());

        if (canDestroyBlock(targetPos)) {
            // the block between the source and extra block is too tough, abort
            getAffectedBlocks().add(targetPos);
        }
    }

    protected boolean canDestroyBlock(BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        float power = getPower() - behavior.getBlastResistance(this, world, pos, state, state.getFluidState())
                .map(val -> (val + 0.3f) * 0.3f)
                .orElse(0f);
        return power > 0.0f && this.behavior.canDestroyBlock(this, this.world, pos, state, power);
    }
}

