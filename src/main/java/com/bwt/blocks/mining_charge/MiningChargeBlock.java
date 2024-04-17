package com.bwt.blocks.mining_charge;

import com.bwt.blocks.SidingBlock;
import com.bwt.entities.MiningChargeEntity;
import com.bwt.sounds.BwtSoundEvents;
import com.bwt.utils.BlockUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class MiningChargeBlock extends WallMountedBlock {
    protected static final Box BOTTOM_SHAPE = new Box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

    protected static final List<VoxelShape> COLLISION_SHAPES = Arrays.stream(Direction.values())
            .map(direction -> BlockUtils.rotateCuboidFromUp(direction, BOTTOM_SHAPE))
            .toList();
    public static final MapCodec<MiningChargeBlock> CODEC = SidingBlock.createCodec(MiningChargeBlock::new);

    public MiningChargeBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(FACE, BlockFace.WALL));
    }

    public MapCodec<? extends MiningChargeBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, FACE);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPES.get(getSurfaceOrientation(state).getId());
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return true;
    }

    public static boolean isHorizontal(BlockState state) {
        return state.get(FACE) == BlockFace.WALL;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (oldState.isOf(state.getBlock())) {
            return;
        }
        if (world.isReceivingRedstonePower(pos)) {
            prime(world, pos, state);
            world.removeBlock(pos, false);
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return state;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isReceivingRedstonePower(pos)) {
            prime(world, pos, state);
            world.removeBlock(pos, false);
        }
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        if (world.isClient) {
            return;
        }
        TntEntity tntEntity = new TntEntity(world, (double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, explosion.getCausingEntity());
        int i = tntEntity.getFuse();
        tntEntity.setFuse((short)(world.random.nextInt(i / 4) + i / 8));
        world.spawnEntity(tntEntity);
    }

    public static void prime(World world, BlockPos pos, BlockState state) {
        prime(world, pos, state, null);
    }

    private static void prime(World world, BlockPos pos, BlockState state, @Nullable LivingEntity igniter) {
        if (world.isClient) {
            return;
        }
        MiningChargeEntity miningChargeEntity = new MiningChargeEntity(world, pos.toCenterPos().subtract(0, 0.5, 0), state, igniter);
        world.spawnEntity(miningChargeEntity);
        world.playSound(null, miningChargeEntity.getX(), miningChargeEntity.getY(), miningChargeEntity.getZ(), BwtSoundEvents.MINING_CHARGE_PRIME, SoundCategory.BLOCKS, 1.0f, 1.0f);
        world.emitGameEvent(igniter, GameEvent.PRIME_FUSE, pos);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!itemStack.isOf(Items.FLINT_AND_STEEL) && !itemStack.isOf(Items.FIRE_CHARGE)) {
            return super.onUse(state, world, pos, player, hand, hit);
        }
        prime(world, pos, state, player);
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL_AND_REDRAW);
        Item item = itemStack.getItem();
        if (!player.isCreative()) {
            if (itemStack.isOf(Items.FLINT_AND_STEEL)) {
                itemStack.damage(1, player, playerx -> playerx.sendToolBreakStatus(hand));
            } else {
                itemStack.decrement(1);
            }
        }
        player.incrementStat(Stats.USED.getOrCreateStat(item));
        return ActionResult.success(world.isClient);
    }

    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (!world.isClient) {
            BlockPos blockPos = hit.getBlockPos();
            Entity entity = projectile.getOwner();
            if (projectile.isOnFire() && projectile.canModifyAt(world, blockPos)) {
                prime(world, blockPos, state, entity instanceof LivingEntity ? (LivingEntity)entity : null);
                world.removeBlock(blockPos, false);
            }
        }
    }

    @Override
    public boolean shouldDropItemsOnExplosion(Explosion explosion) {
        return false;
    }

    public static Direction getSurfaceOrientation(BlockState state) {
        return switch (state.get(FACE)) {
            case WALL -> state.get(FACING);
            case FLOOR -> Direction.UP;
            case CEILING -> Direction.DOWN;
        };
    }

    public static BlockState withSurfaceOrientation(BlockState state, Direction direction) {
        return switch (direction) {
            case UP -> state.with(FACE, BlockFace.FLOOR);
            case DOWN -> state.with(FACE, BlockFace.CEILING);
            case NORTH -> state.with(FACE, BlockFace.WALL).with(FACING, Direction.SOUTH);
            case EAST -> state.with(FACE, BlockFace.WALL).with(FACING, Direction.WEST);
            case SOUTH -> state.with(FACE, BlockFace.WALL).with(FACING, Direction.NORTH);
            case WEST -> state.with(FACE, BlockFace.WALL).with(FACING, Direction.EAST);
        };
    }
}
