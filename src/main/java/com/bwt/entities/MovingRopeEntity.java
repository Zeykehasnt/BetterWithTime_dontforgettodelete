package com.bwt.entities;

import com.bwt.blocks.BwtBlocks;
import com.bwt.blocks.pulley.PulleyBlockEntity;
import com.bwt.mixin.MovableBlockEntityMixin;
import com.bwt.utils.TrackedDataHandlers;
import com.bwt.utils.VoxelShapedEntity;
import com.bwt.utils.rectangular_entity.EntityRectDimensions;
import com.google.common.collect.Maps;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

import java.util.*;

public class MovingRopeEntity extends Entity implements VoxelShapedEntity {
    protected static final TrackedData<BlockPos> pulleyPos = DataTracker.registerData(MovingRopeEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final TrackedData<Integer> targetY = DataTracker.registerData(MovingRopeEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> up = DataTracker.registerData(MovingRopeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Map<Vec3i, BlockState>> blockMap = DataTracker.registerData(MovingRopeEntity.class, TrackedDataHandlers.blockStateMapHandler);
    private static final TrackedData<Map<Vec3i, NbtCompound>> blockEntityNbtMap = DataTracker.registerData(MovingRopeEntity.class, TrackedDataHandlers.blockEntityMapHandler);
//    private final HashMap<Vec3i, MovingPlatformComponentEntity> children;
    private VoxelShape voxelShape = VoxelShapes.fullCube();
    private final float speed = 1f / 20f;

    public MovingRopeEntity(EntityType<? extends MovingRopeEntity> entityType, World world) {
        super(entityType, world);
        this.intersectionChecked = true;
//        this.children = Maps.newHashMap();
    }


    public MovingRopeEntity(World world, BlockPos pulley, BlockPos source, int targetY) {
        this(BwtEntities.movingRopeEntity, world);
        setTargetY(targetY);
        if (source != null) {
            setIsMovingUp(source.getY() < targetY);
            setPosition(source.getX() + 0.5, source.getY(), source.getZ() + 0.5);
        }
        this.ignoreCameraFrustum = true;
        setPulleyPos(pulley);
    }


    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(pulleyPos, BlockPos.ORIGIN);
        this.dataTracker.startTracking(up, false);
        this.dataTracker.startTracking(targetY, 0);
        this.dataTracker.startTracking(blockMap, Maps.newHashMap());
        this.dataTracker.startTracking(blockEntityNbtMap, Maps.newHashMap());
    }

    public void setPulleyPos(BlockPos pos) {
        this.dataTracker.set(pulleyPos, pos);
    }

    public BlockPos getPulleyPos() {
        return this.dataTracker.get(pulleyPos);
    }

    public boolean isMovingUp() {
        return this.dataTracker.get(up);
    }

    public void setIsMovingUp(boolean value) {
        this.dataTracker.set(up, value);
    }

    public int getTargetY() {
        return this.dataTracker.get(targetY);
    }

    public void setTargetY(int value) {
        this.dataTracker.set(targetY, value);
    }

    public void setBlockMap(Map<Vec3i, BlockState> map) {
        this.dataTracker.set(blockMap, map);
//        map.forEach((offset, blockState) -> {
//            children.putIfAbsent(offset, new MovingPlatformComponentEntity(this, offset, blockState, null));
//        });
//        children.entrySet().removeIf(entry -> {
//            if (!map.containsKey(entry.getKey())) {
//                entry.getValue().discard();
//                return true;
//            }
//            return false;
//        });
    }

    public HashMap<Vec3i, BlockState> getBlockMap() {
        Map<Vec3i, BlockState> blockMap = this.dataTracker.get(MovingRopeEntity.blockMap);
        return new HashMap<>(blockMap);
    }

    public void setBlockEntityNbtMap(Map<Vec3i, NbtCompound> map) {
        this.dataTracker.set(blockEntityNbtMap, map);
    }

    public HashMap<Vec3i, NbtCompound> getBlockEntityNbtMap() {
        Map<Vec3i, NbtCompound> nbtMap = this.dataTracker.get(blockEntityNbtMap);
        return new HashMap<>(nbtMap);
    }

//    public Map<Vec3i, MovingPlatformComponentEntity> getChildren() {
//        return children;
//    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        super.setPosition(x, y, z);
//        if (children != null && !children.isEmpty()) {
//            children.forEach((offset, child) -> child.setPosition(x + offset.getX(), y + offset.getY(), z + offset.getZ()));
//        }
    }

    @Override
    public VoxelShape getVoxelShape() {
        return voxelShape;
    }

    public void setVoxelShape(VoxelShape voxelShape) {
        this.voxelShape = voxelShape;
        setBoundingBox(voxelShape.getBoundingBox());
        calculateDimensions();
        calculateBoundingBox();
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        Box boundingBox = getBoundingBox();
        return EntityRectDimensions.changing((float) boundingBox.getLengthX(), (float) boundingBox.getLengthY(), (float) boundingBox.getLengthZ());
    }

    @Override
    protected Box calculateBoundingBox() {
        return Box.enclosing(
                new BlockPos(
                    getBlockMap().keySet().stream().mapToInt(Vec3i::getX).min().orElse(0),
                    getBlockMap().keySet().stream().mapToInt(Vec3i::getY).min().orElse(0),
                    getBlockMap().keySet().stream().mapToInt(Vec3i::getZ).min().orElse(0)
                ),
                new BlockPos(
                    getBlockMap().keySet().stream().mapToInt(Vec3i::getX).max().orElse(0),
                    getBlockMap().keySet().stream().mapToInt(Vec3i::getY).max().orElse(0),
                    getBlockMap().keySet().stream().mapToInt(Vec3i::getZ).max().orElse(0)
                )
        ).offset(getPos()).offset(-0.5, 0, -0.5);
    }


    @Override
    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return -1;
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound compound) {
        setPulleyPos(new BlockPos(compound.getInt("PulleyX"), compound.getInt("PulleyY"), compound.getInt("PulleyZ")));
        setTargetY(compound.getInt("TargetY"));
        setIsMovingUp(compound.getBoolean("Up"));
        if (compound.contains("blocks", NbtElement.LIST_TYPE)) {
            Map<Vec3i, BlockState> blocks = deserializeBlockMap(compound.getList("blocks", NbtElement.COMPOUND_TYPE));
            setBlockMap(blocks);
            rebuildBlockBoundingBox();
        }
        if (compound.contains("blockEntities", NbtElement.LIST_TYPE)) {
            setBlockEntityNbtMap(deserializeBlockEntities(compound.getList("blockEntities", NbtElement.COMPOUND_TYPE)));
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound compound) {
        compound.putInt("PulleyX", getPulleyPos().getX());
        compound.putInt("PulleyY", getPulleyPos().getY());
        compound.putInt("PulleyZ", getPulleyPos().getZ());
        compound.putInt("TargetY", getTargetY());
        compound.putBoolean("Up", isMovingUp());
        compound.put("blocks", serializeBlockMap());
        compound.put("blockEntities", serializeBlockEntities());
    }

    private NbtList serializeBlockEntities() {
        NbtList entries = new NbtList();
        getBlockEntityNbtMap().forEach((offset, blockEntity) -> {
            NbtCompound entry = new NbtCompound();
            entry.putLong("offset", new BlockPos(offset).asLong());
            entry.put("blockEntity", blockEntity);
            entries.add(entry);
        });
        return entries;
    }

    private Map<Vec3i, NbtCompound> deserializeBlockEntities(NbtList blockEntities) {
        Map<Vec3i, NbtCompound> map = new HashMap<>();
        for (NbtElement entry : blockEntities) {
            NbtCompound compound = (NbtCompound) entry;
            map.put(
                    BlockPos.fromLong(compound.getLong("offset")),
                    compound.getCompound("blockEntity")
            );
        }
        return map;
    }

    private NbtList serializeBlockMap() {
        NbtList entries = new NbtList();
        for (Map.Entry<Vec3i, BlockState> entry : getBlockMap().entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            NbtCompound listEntry = new NbtCompound();
            listEntry.putLong("pos", new BlockPos(entry.getKey()).asLong());
            listEntry.put("state", NbtHelper.fromBlockState(entry.getValue()));
            entries.add(listEntry);
        }
        return entries;
    }

    private Map<Vec3i, BlockState> deserializeBlockMap(NbtList blocks) {
        Map<Vec3i, BlockState> map = new HashMap<>();
        for (NbtElement entry : blocks) {
            NbtCompound entryCompound = ((NbtCompound) entry);
            Vec3i pos = BlockPos.fromLong(entryCompound.getLong("pos"));
            BlockState state = NbtHelper.toBlockState(this.getWorld().createCommandRegistryWrapper(RegistryKeys.BLOCK), entryCompound.getCompound("state"));
            map.put(pos, state);
        }
        return map;
    }

    public boolean collidesWithStateAtPos(BlockPos pos, BlockState state) {
        VoxelShape voxelShape2 = state.getCollisionShape(this.getWorld(), pos, ShapeContext.of(this)).offset(pos.getX(), pos.getY(), pos.getZ());
        return VoxelShapes.matchesAnywhere(voxelShape2, VoxelShapes.cuboid(this.getBoundingBox()), BooleanBiFunction.AND);
    }

    public void rebuildBlockBoundingBox() {
        HashMap<Vec3i, BlockState> blocks = getBlockMap();
        if (blocks == null || blocks.isEmpty()) {
            setVoxelShape(VoxelShapes.fullCube());
            return;
        }
        VoxelShape newShape = VoxelShapes.empty();
        for (Map.Entry<Vec3i, BlockState> entry : blocks.entrySet()) {
            newShape = VoxelShapes.union(newShape, entry.getValue().getCollisionShape(getWorld(), getBlockPos().add(entry.getKey())).offset(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getZ()));
        }
        setVoxelShape(newShape);
    }

    private double getSpeed() {
        return isMovingUp() ? speed : -speed;
    }

    @Override
    public void tick() {
        if (getPulleyPos() == null) {
            return;
        }
        rebuildBlockBoundingBox();
        if (isMovingUp()) {
            if (getY() > getTargetY()) {
                if (done())
                    return;
            }
        } else {
            if (getY() < getTargetY()) {
                if (done())
                    return;
            }
        }

        HashMap<Entity, Box> riders = getRiders();
        moveRiders(riders);
        setPosition(
                getPulleyPos().getX() + 0.5,
                this.getY() + getSpeed(),
                getPulleyPos().getZ() + 0.5
        );
        riders.putAll(getRiders());
        moveRiders(riders);
    }

    public HashMap<Entity, Box> getRiders() {
        HashMap<Entity, Box> entities = new HashMap<>();
        for (Box box : this.getVoxelShape().offset(getX() - 0.5, getY(), getZ() - 0.5).getBoundingBoxes()) {
            for (Entity entity1 : getWorld().getOtherEntities(this, box.expand(1.0E-7, 0.15, 1.0E-7), EntityPredicates.EXCEPT_SPECTATOR.and(entity -> !(entity instanceof AbstractDecorationEntity)))) {
                entities.put(entity1, box);
            }
        }
        return entities;
    }

    public void moveRiders(HashMap<Entity, Box> riders) {
        riders.forEach((entity, box) -> {
            if (entity.getY() < box.minY) {
                return;
            }
            double thisFrameIntersectingY = entity.getY() - box.maxY;
            if (thisFrameIntersectingY < 0) {
                entity.setPosition(entity.getX(), box.maxY + 1.0e-7, entity.getZ());
            }
            // If entity is moving downwards, next frame it will intersect the platform by this much
            double nextFrameIntersectingY = entity.getVelocity().y;
            if (nextFrameIntersectingY < 0) {
                entity.addVelocity(0, -nextFrameIntersectingY, 0);
            }
            entity.addVelocity(0, getSpeed() - entity.getVelocity().y, 0);
            entity.setOnGround(true);
        });
    }


    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    private void reconstruct() {
        BlockPos pos = getPulleyPos().down(getPulleyPos().getY() - getTargetY());

        int retries = 0;
        Map<Vec3i, BlockState> blocks = getBlockMap();
        Map<Vec3i, NbtCompound> blockEntities = getBlockEntityNbtMap();
        while (!blocks.isEmpty() && retries < 10) {
            retries++;
            int skipped = 0;
            for (Map.Entry<Vec3i, BlockState> entry : blocks.entrySet()) {
                BlockPos blockPos = pos.add(entry.getKey());
                BlockState state = entry.getValue();
                if (state.canPlaceAt(getWorld(), blockPos)) {
                    if (getWorld().setBlockState(blockPos, state, Block.NOTIFY_ALL)) {
                        ((ServerWorld) this.getWorld()).getChunkManager().threadedAnvilChunkStorage.sendToOtherNearbyPlayers(this, new BlockUpdateS2CPacket(blockPos, this.getWorld().getBlockState(blockPos)));
                    }
                    if (blockEntities.containsKey(entry.getKey())) {
                        BlockEntity blockEntity = getWorld().getBlockEntity(blockPos);
                        if (blockEntity != null) {
                            NbtCompound tag = blockEntities.get(entry.getKey());
                            blockEntity.readNbt(tag);
                            ((MovableBlockEntityMixin) blockEntity).setPos(blockPos);
                        }
                    }
                    blocks.remove(entry.getKey());
                    blockEntities.remove(entry.getKey());
                    skipped = 0;
                    break;
                }
                skipped++;
            }
            if (skipped == 0) {
                retries = 0;
            }
        }

        if (retries > 0) {
            blocks.forEach((blockPos, state) -> this.dropItem(state.getBlock()));
        }
        setBlockMap(blocks);
    }

    private boolean done() {
        if (getWorld().isClient) {
            return false;
        }
        BlockEntity blockEntity = getWorld().getBlockEntity(getPulleyPos());
        if (blockEntity instanceof PulleyBlockEntity pulleyBlockEntity) {
            if (!pulleyBlockEntity.onJobCompleted(getWorld(), getPulleyPos(), getWorld().getBlockState(getPulleyPos()), isMovingUp(), getTargetY(), this)) {
                reconstruct();
                return true;
            }
            return false;
        }
        // The block entity has been lost, abort
        reconstruct();
        this.discard();
        return true;
    }

    public Map<Vec3i, BlockState> addBlock(Vec3i offset, World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        Map<Vec3i, BlockState> blocks = getBlockMap();
        Map<Vec3i, NbtCompound> blockEntities = getBlockEntityNbtMap();
        blocks.put(offset, state);
        if (blockEntity != null) {
            NbtCompound tag = blockEntity.createNbtWithIdentifyingData();
            blockEntities.put(offset, tag);
            world.removeBlockEntity(pos);
            setBlockEntityNbtMap(blockEntities);
        }
        if (!world.isClient) {
            setBlockMap(blocks);
        }
        return blocks;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return false;
    }

    public boolean isPathBlocked() {
        HashSet<BlockPos> blocked = new HashSet<>();
        getBlockMap().forEach((offset, state) -> {
            if (state.isOf(BwtBlocks.anchorBlock) && (!blocked.isEmpty() || isMovingUp())) {
                return;
            }
            BlockPos pos = this.getPulleyPos().down(this.getPulleyPos().getY() - getTargetY()).add(offset);
            pos = isMovingUp() ? pos.up() : pos.down();

            BlockState placementState = getEntityWorld().getBlockState(pos);

            if (!(placementState.isAir() || placementState.isReplaceable())) {
                blocked.add(pos);
            }
        });
        return !blocked.isEmpty();
    }
}
