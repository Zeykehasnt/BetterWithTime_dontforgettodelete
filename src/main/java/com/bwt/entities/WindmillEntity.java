package com.bwt.entities;

import com.bwt.blocks.AxleBlock;
import com.bwt.blocks.AxlePowerSourceBlock;
import com.bwt.blocks.BwtBlocks;
import com.bwt.items.BwtItems;
import com.bwt.utils.rectangular_entity.RectangularEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;

public class WindmillEntity extends RectangularEntity {
    public static final float height = 12.8f;
    public static final float width = 12.8f;
    public static final float length = 0.8f;

    private static final float rotationPerTick = -0.12F;
    private static final float rotationPerTickInStorm = -2.0F;
    private static final float rotationPerTickInNether = -0.07F;

    private int ticksBeforeNextFullUpdate = 20;
    private float rotation = 0;

    private static final TrackedData<Float> rotationSpeed = DataTracker.registerData(WindmillEntity.class, TrackedDataHandlerRegistry.FLOAT);


    public WindmillEntity(EntityType<? extends Entity> entityType, World world) {
        super(entityType, world);
        this.intersectionChecked = true;
    }

    public WindmillEntity(World world, Vec3d pos, Direction facing) {
        this(BwtEntities.windmillEntity, world);
        setPosition(pos);
        setYaw(facing.asRotation());
    }

    @Override
    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return (dimensions.height / 2) - 1;
    }

    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return Entity.MoveEffect.NONE;
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(rotationSpeed, 0f);
    }

    public float getRotation() {
        return rotation;
    }

    public float getRotationSpeed() {
        return getDataTracker().get(rotationSpeed);
    }

    public void setRotationSpeed(float speed) {
        getDataTracker().set(rotationSpeed, speed);
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    public PistonBehavior getPistonBehavior() {
        return PistonBehavior.DESTROY;
    }

    public boolean tryToSpawn() {
        if (placementBlockedByBlock()) {
            if(MinecraftClient.getInstance().player != null) {
                MinecraftClient.getInstance().player.sendMessage(Text.of("Not enough room to place Wind Mill (They are friggin HUGE!)"));
            }
            return false;
        }
        if (placementBlockedByEntity()) {
            if(MinecraftClient.getInstance().player != null) {
                MinecraftClient.getInstance().player.sendMessage(Text.of("Wind Mill placement is obstructed by something, or by you"));
            }
            return false;
        }
        // This must be the last condition checked,
        // because it has a side effect of converting the axle to powered
        if (placementHasBadAxleState()) {
            return false;
        }

        setRotationSpeed(computeRotation());
        World world = getWorld();
        world.spawnEntity(this);
        return true;
    }

    public boolean placementBlockedByBlock() {
        return BlockPos.stream(getBoundingBox())
                // Ignore the axle we're on
                .filter(blockPos -> !blockPos.equals(this.getBlockPos()))
                .anyMatch(blockPos -> !getWorld().getBlockState(blockPos).isAir());
    }

    @Override
    protected void onBlockCollision(BlockState state) {
        destroyWithDrop();
    }

    public boolean placementBlockedByEntity() {
        ArrayList<Entity> anyEntities = new ArrayList<>();
        getWorld().collectEntitiesByType(TypeFilter.instanceOf(Entity.class), getBoundingBox(), entity -> entity != this, anyEntities, 1);
        return !anyEntities.isEmpty();
    }

    public boolean placementHasBadAxleState() {
        World world = getWorld();

        BlockState axleBlock = world.getBlockState(getBlockPos());

        // Bad block type
        if (!axleBlock.isOf(BwtBlocks.axleBlock) && !axleBlock.isOf(BwtBlocks.axlePowerSourceBlock)) {
            return true;
        }
        Direction.Axis axleAxis = axleBlock.get(AxleBlock.AXIS);
        float yaw = getYaw();

        // Misaligned
        if (Direction.from(axleAxis, Direction.AxisDirection.NEGATIVE).asRotation() != yaw
            && Direction.from(axleAxis, Direction.AxisDirection.POSITIVE).asRotation() != yaw
        ) {
            return true;
        }

        // Not a fail condition, but a state correction to power the axle
        // Especially important on placement
        if (!axleBlock.isOf(BwtBlocks.axlePowerSourceBlock)) {
            world.setBlockState(getBlockPos(), BwtBlocks.axlePowerSourceBlock.getDefaultState().with(AxlePowerSourceBlock.AXIS, axleAxis));
        }

        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (isRemoved()) {
            return;
        }

        if (getWorld().isClient) {
            float prevRotation = rotation;
            updateRotation();
        }
        else {
            ticksBeforeNextFullUpdate--;
            if (ticksBeforeNextFullUpdate <= 0) {
                ticksBeforeNextFullUpdate = 20;
                fullUpdate();
            }
            updateRotation();
        }
    }

    public float computeRotation() {
        World world = getWorld();

        float rotationAmount;
        // Nether
        if (world.getDimension().ultrawarm()) {
            rotationAmount = rotationPerTickInNether;
        }
        // End dimension or modded
        else if (!world.getDimension().natural()) {
            rotationAmount = 0.0f;
        }
        // Overworld, sky blocked
        else if (!world.isSkyVisible(getBlockPos())) {
            rotationAmount = 0.0f;
        }
        // Overworld, raining
        else if (world.isRaining()) {
            rotationAmount = rotationPerTickInStorm;
        }
        // Overworld, not raining
        else {
            rotationAmount = rotationPerTick;
        }
        return rotationAmount;
    }

    protected void updateRotation() {
        rotation += this.getDataTracker().get(rotationSpeed);

        if (rotation > 360f) {
            rotation -= 360f;
        }
        else if (rotation < -360f) {
            rotation += 360f;
        }
    }

    protected void fullUpdate() {
        if (placementBlockedByBlock() || placementBlockedByEntity() || placementHasBadAxleState()) {
            destroyWithDrop();
            return;
        }

        setRotationSpeed(computeRotation());
        float currentSpeed = getRotationSpeed();

        boolean powered = currentSpeed > 0.01f || currentSpeed < -0.01f;

        BlockState blockState = getWorld().getBlockState(getBlockPos());
        getWorld().setBlockState(
                getBlockPos(),
                (powered ? BwtBlocks.axlePowerSourceBlock : BwtBlocks.axleBlock).getDefaultState()
                        .with(AxleBlock.AXIS, blockState.get(AxleBlock.AXIS))
        );
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.getWorld().isClient || this.isRemoved()) {
            return true;
        }
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        boolean bl = source.getAttacker() instanceof PlayerEntity && ((PlayerEntity)source.getAttacker()).getAbilities().creativeMode;
        if (!bl /* && this.getDamageWobbleStrength() > 40.0f || this.shouldAlwaysKill(source) */) {
            destroyWithDrop();
        } else /* if (bl) */{
            discard();
        }
        return true;
    }

    public void destroyWithDrop() {
        if (isRemoved()) return;
        dropStack(BwtItems.windmillItem.getDefaultStack(), 0.5f);
        kill();
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        World world = getWorld();
        BlockPos pos = getBlockPos();
        BlockState hostBlockState = world.getBlockState(pos);
        if (hostBlockState.isOf(BwtBlocks.axlePowerSourceBlock)) {
             world.setBlockState(pos, BwtBlocks.axleBlock.getDefaultState()
                    .with(AxleBlock.AXIS, hostBlockState.get(AxleBlock.AXIS)));
        }
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(BwtItems.windmillItem);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }
}
