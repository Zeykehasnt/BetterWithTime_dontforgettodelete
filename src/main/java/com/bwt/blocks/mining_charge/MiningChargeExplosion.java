package com.bwt.blocks.mining_charge;

import com.bwt.entities.BwtEntities;
import com.bwt.entities.MiningChargeEntity;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.EntitySubPredicateTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.EntityExplosionBehavior;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MiningChargeExplosion extends Explosion {
    public static final LootCondition LOOT_CONDITION = EntityPropertiesLootCondition.builder(
            LootContext.EntityTarget.THIS,
            EntityPredicate.Builder.create().type(BwtEntities.miningChargeEntity)
    ).build();

    protected final World world;
    protected final ExplosionBehavior behavior;
    protected final MiningChargeEntity miningChargeEntity;
    protected final DamageSource damageSource;

    public MiningChargeExplosion(World world, @NotNull MiningChargeEntity entity, Vec3d pos, DamageSource damageSource, float power, boolean createFire, Explosion.DestructionType destructionType, ParticleEffect particle, ParticleEffect emitterParticle, RegistryEntry<SoundEvent> soundEvent) {
        super(world, entity, damageSource, null, pos.x, pos.y, pos.z, power, createFire, destructionType, particle, emitterParticle, soundEvent);
        this.world = world;
        this.behavior = new EntityExplosionBehavior(entity);
        this.miningChargeEntity = entity;
        this.damageSource = damageSource;
    }

    public void collectBlocksAndDamageEntities() {
        super.collectBlocksAndDamageEntities();
        getAffectedBlocks().clear();
        damageBlocks();
    }

    protected void damageBlocks() {
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

    protected void damageEntities() {
        float damagePower = this.getPower() * 2.0f;
        int k = MathHelper.floor(getPosition().x - (double) damagePower - 1.0);
        int l = MathHelper.floor(getPosition().x + (double) damagePower + 1.0);
        int r = MathHelper.floor(getPosition().y - (double) damagePower - 1.0);
        int s = MathHelper.floor(getPosition().y + (double) damagePower + 1.0);
        int t = MathHelper.floor(getPosition().z - (double) damagePower - 1.0);
        int u = MathHelper.floor(getPosition().z + (double) damagePower + 1.0);
        List<Entity> list = this.world.getOtherEntities(
                miningChargeEntity, new Box(
                        -damagePower - 1, -damagePower - 1, -damagePower - 1,
                         damagePower + 1, damagePower +  1, damagePower +  1
                ).offset(getPosition())
        );
        for (Entity entity : list) {
            PlayerEntity playerEntity;
            double distX = entity.getX() - getPosition().x;
            double distY = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - getPosition().y;
            double distZ = entity.getZ() - getPosition().z;
            double distanceToEntity = Math.sqrt(distX * distX + distY * distY + distZ * distZ);
            double v = Math.sqrt(entity.squaredDistanceTo(getPosition())) / (double) damagePower;
            if (
                    entity.isImmuneToExplosion(this)
                    || v > 1.0
                    || distanceToEntity == 0.0
            )
                continue;
            distX /= distanceToEntity;
            distY /= distanceToEntity;
            distZ /= distanceToEntity;
            if (this.behavior.shouldDamage(this, entity)) {
                entity.damage(damageSource, this.behavior.calculateDamage(this, entity));
            }
            double rawKnockback = (1.0 - v) * Explosion.getExposure(getPosition(), entity);
            double knockback = entity instanceof LivingEntity livingEntity ? ProtectionEnchantment.transformExplosionKnockback(livingEntity, rawKnockback) : rawKnockback;
            Vec3d vec3d2 = new Vec3d(distX * knockback, distY * knockback, distZ * knockback);
            entity.setVelocity(entity.getVelocity().add(vec3d2));
            if (!(entity instanceof PlayerEntity) || (playerEntity = (PlayerEntity)entity).isSpectator() || playerEntity.isCreative() && playerEntity.getAbilities().flying) continue;
            this.getAffectedPlayers().put(playerEntity, vec3d2);
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

