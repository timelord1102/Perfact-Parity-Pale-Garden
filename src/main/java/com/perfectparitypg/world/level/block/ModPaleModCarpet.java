package com.perfectparitypg.world.level.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ModPaleModCarpet extends Block implements BonemealableBlock {
    public static final MapCodec<ModPaleModCarpet> CODEC = simpleCodec(ModPaleModCarpet::new);
    public static final BooleanProperty BASE;
    private static final EnumProperty<WallSide> NORTH;
    private static final EnumProperty<WallSide> EAST;
    private static final EnumProperty<WallSide> SOUTH;
    private static final EnumProperty<WallSide> WEST;
    private static final Map<Direction, EnumProperty<WallSide>> PROPERTY_BY_DIRECTION;
    private static final float AABB_OFFSET = 1.0F;
    private static final VoxelShape DOWN_AABB;
    private static final VoxelShape WEST_AABB;
    private static final VoxelShape EAST_AABB;
    private static final VoxelShape NORTH_AABB;
    private static final VoxelShape SOUTH_AABB;
    private static final int SHORT_HEIGHT = 10;
    private static final VoxelShape WEST_SHORT_AABB;
    private static final VoxelShape EAST_SHORT_AABB;
    private static final VoxelShape NORTH_SHORT_AABB;
    private static final VoxelShape SOUTH_SHORT_AABB;
    private final Map<BlockState, VoxelShape> shapesCache;

    public MapCodec<ModPaleModCarpet> codec() {
        return CODEC;
    }

    public ModPaleModCarpet(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(BASE, true)).setValue(NORTH, WallSide.NONE)).setValue(EAST, WallSide.NONE)).setValue(SOUTH, WallSide.NONE)).setValue(WEST, WallSide.NONE));
        this.shapesCache = ImmutableMap.copyOf((Map)this.stateDefinition.getPossibleStates().stream().collect(Collectors.toMap(Function.identity(), ModPaleModCarpet::calculateShape)));
    }

    protected VoxelShape getOcclusionShape(BlockState blockState) {
        return Shapes.empty();
    }

    private static VoxelShape calculateShape(BlockState blockState) {
        VoxelShape voxelShape = Shapes.empty();
        if ((Boolean)blockState.getValue(BASE)) {
            voxelShape = DOWN_AABB;
        }

        VoxelShape var10000;
        switch ((WallSide)blockState.getValue(NORTH)) {
            case NONE -> var10000 = voxelShape;
            case LOW -> var10000 = Shapes.or(voxelShape, NORTH_SHORT_AABB);
            case TALL -> var10000 = Shapes.or(voxelShape, NORTH_AABB);
            default -> throw new MatchException((String)null, (Throwable)null);
        }

        voxelShape = var10000;
        switch ((WallSide)blockState.getValue(SOUTH)) {
            case NONE -> var10000 = voxelShape;
            case LOW -> var10000 = Shapes.or(voxelShape, SOUTH_SHORT_AABB);
            case TALL -> var10000 = Shapes.or(voxelShape, SOUTH_AABB);
            default -> throw new MatchException((String)null, (Throwable)null);
        }

        voxelShape = var10000;
        switch ((WallSide)blockState.getValue(EAST)) {
            case NONE -> var10000 = voxelShape;
            case LOW -> var10000 = Shapes.or(voxelShape, EAST_SHORT_AABB);
            case TALL -> var10000 = Shapes.or(voxelShape, EAST_AABB);
            default -> throw new MatchException((String)null, (Throwable)null);
        }

        voxelShape = var10000;
        switch ((WallSide)blockState.getValue(WEST)) {
            case NONE -> var10000 = voxelShape;
            case LOW -> var10000 = Shapes.or(voxelShape, WEST_SHORT_AABB);
            case TALL -> var10000 = Shapes.or(voxelShape, WEST_AABB);
            default -> throw new MatchException((String)null, (Throwable)null);
        }

        voxelShape = var10000;
        return voxelShape.isEmpty() ? Shapes.block() : voxelShape;
    }

    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return (VoxelShape)this.shapesCache.get(blockState);
    }

    protected VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return (Boolean)blockState.getValue(BASE) ? DOWN_AABB : Shapes.empty();
    }

    protected boolean propagatesSkylightDown(BlockState blockState) {
        return true;
    }

    protected boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockState blockState2 = levelReader.getBlockState(blockPos.below());
        if ((Boolean)blockState.getValue(BASE)) {
            return !blockState2.isAir();
        } else {
            return blockState2.is(this) && (Boolean)blockState2.getValue(BASE);
        }
    }

    private static boolean hasFaces(BlockState blockState) {
        if ((Boolean)blockState.getValue(BASE)) {
            return true;
        } else {
            for(EnumProperty<WallSide> enumProperty : PROPERTY_BY_DIRECTION.values()) {
                if (blockState.getValue(enumProperty) != WallSide.NONE) {
                    return true;
                }
            }

            return false;
        }
    }

    // fix
    private static boolean canSupportAtFace(BlockGetter level,
                                            BlockPos    mossPos,
                                            Direction   face)
    {
        BlockPos    neighbourPos   = mossPos.relative(face);
        BlockState  neighbourState = level.getBlockState(neighbourPos);
        // Is that neighbour’s face sturdy enough for us?
        return MultifaceBlock.canAttachTo(level, face, neighbourPos, neighbourState);
    }


    private static BlockState getUpdatedState(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, boolean bl) {
        BlockState blockState2 = null;
        BlockState blockState3 = null;
        bl |= (Boolean)blockState.getValue(BASE);

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            EnumProperty<WallSide> enumProperty = getPropertyForFace(direction);
            WallSide wallSide = canSupportAtFace(blockGetter, blockPos, direction) ? (bl ? WallSide.LOW : (WallSide)blockState.getValue(enumProperty)) : WallSide.NONE;
            if (wallSide == WallSide.LOW) {
                if (blockState2 == null) {
                    blockState2 = blockGetter.getBlockState(blockPos.above());
                }

                if (blockState2.is(ModBlocks.PALE_MOSS_CARPET) && blockState2.getValue(enumProperty) != WallSide.NONE && !(Boolean)blockState2.getValue(BASE)) {
                    wallSide = WallSide.TALL;
                }

                if (!(Boolean)blockState.getValue(BASE)) {
                    if (blockState3 == null) {
                        blockState3 = blockGetter.getBlockState(blockPos.below());
                    }

                    if (blockState3.is(ModBlocks.PALE_MOSS_CARPET) && blockState3.getValue(enumProperty) == WallSide.NONE) {
                        wallSide = WallSide.NONE;
                    }
                }
            }

            blockState = (BlockState)blockState.setValue(enumProperty, wallSide);
        }

        return blockState;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return getUpdatedState(this.defaultBlockState(), blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos(), true);
    }

    public static void placeAt(LevelAccessor levelAccessor, BlockPos blockPos, RandomSource randomSource, int i) {
        BlockState blockState = ModBlocks.PALE_MOSS_CARPET.defaultBlockState();
        BlockState blockState2 = getUpdatedState(blockState, levelAccessor, blockPos, true);
        levelAccessor.setBlock(blockPos, blockState2, 3);
        Objects.requireNonNull(randomSource);
        BlockState blockState3 = createTopperWithSideChance(levelAccessor, blockPos, randomSource::nextBoolean);
        if (!blockState3.isAir()) {
            levelAccessor.setBlock(blockPos.above(), blockState3, i);
        }

    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        if (!level.isClientSide) {
            RandomSource randomSource = level.getRandom();
            Objects.requireNonNull(randomSource);
            BlockState blockState2 = createTopperWithSideChance(level, blockPos, randomSource::nextBoolean);
            if (!blockState2.isAir()) {
                level.setBlock(blockPos.above(), blockState2, 3);
            }

        }
    }


    private static BlockState createTopperWithSideChance(BlockGetter blockGetter, BlockPos blockPos, BooleanSupplier booleanSupplier) {
        BlockPos blockPos2 = blockPos.above();
        BlockState blockState = blockGetter.getBlockState(blockPos2);
        boolean bl = blockState.is(ModBlocks.PALE_MOSS_CARPET);
        if ((!bl || !(Boolean)blockState.getValue(BASE)) && (bl || blockState.canBeReplaced())) {
            BlockState blockState2 = (BlockState)ModBlocks.PALE_MOSS_CARPET.defaultBlockState().setValue(BASE, false);
            BlockState blockState3 = getUpdatedState(blockState2, blockGetter, blockPos.above(), true);

            for(Direction direction : Direction.Plane.HORIZONTAL) {
                EnumProperty<WallSide> enumProperty = getPropertyForFace(direction);
                if (blockState3.getValue(enumProperty) != WallSide.NONE && !booleanSupplier.getAsBoolean()) {
                    blockState3 = (BlockState)blockState3.setValue(enumProperty, WallSide.NONE);
                }
            }

            if (hasFaces(blockState3) && blockState3 != blockState) {
                return blockState3;
            } else {
                return Blocks.AIR.defaultBlockState();
            }
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (!blockState.canSurvive(levelAccessor, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            BlockState blockState3 = getUpdatedState(blockState, levelAccessor, blockPos, false);
            return !hasFaces(blockState3) ? Blocks.AIR.defaultBlockState() : blockState3;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{BASE, NORTH, EAST, SOUTH, WEST});
    }

    @Override
    protected BlockState rotate(BlockState blockState, Rotation rotation) {
        BlockState var10000;
        switch (rotation) {
            case CLOCKWISE_180 -> var10000 = (BlockState)((BlockState)((BlockState)((BlockState)blockState.setValue(NORTH, (WallSide)blockState.getValue(SOUTH))).setValue(EAST, (WallSide)blockState.getValue(WEST))).setValue(SOUTH, (WallSide)blockState.getValue(NORTH))).setValue(WEST, (WallSide)blockState.getValue(EAST));
            case COUNTERCLOCKWISE_90 -> var10000 = (BlockState)((BlockState)((BlockState)((BlockState)blockState.setValue(NORTH, (WallSide)blockState.getValue(EAST))).setValue(EAST, (WallSide)blockState.getValue(SOUTH))).setValue(SOUTH, (WallSide)blockState.getValue(WEST))).setValue(WEST, (WallSide)blockState.getValue(NORTH));
            case CLOCKWISE_90 -> var10000 = (BlockState)((BlockState)((BlockState)((BlockState)blockState.setValue(NORTH, (WallSide)blockState.getValue(WEST))).setValue(EAST, (WallSide)blockState.getValue(NORTH))).setValue(SOUTH, (WallSide)blockState.getValue(EAST))).setValue(WEST, (WallSide)blockState.getValue(SOUTH));
            default -> var10000 = blockState;
        }

        return var10000;
    }

    @Override
    protected BlockState mirror(BlockState blockState, Mirror mirror) {
        BlockState var10000;
        switch (mirror) {
            case LEFT_RIGHT -> var10000 = (BlockState)((BlockState)blockState.setValue(NORTH, (WallSide)blockState.getValue(SOUTH))).setValue(SOUTH, (WallSide)blockState.getValue(NORTH));
            case FRONT_BACK -> var10000 = (BlockState)((BlockState)blockState.setValue(EAST, (WallSide)blockState.getValue(WEST))).setValue(WEST, (WallSide)blockState.getValue(EAST));
            default -> var10000 = super.mirror(blockState, mirror);
        }

        return var10000;
    }


    @Nullable
    public static EnumProperty<WallSide> getPropertyForFace(Direction direction) {
        return (EnumProperty)PROPERTY_BY_DIRECTION.get(direction);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return (Boolean)blockState.getValue(BASE) && !createTopperWithSideChance(levelReader, blockPos, () -> true).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        BlockState blockState2 = createTopperWithSideChance(serverLevel, blockPos, () -> true);
        if (!blockState2.isAir()) {
            serverLevel.setBlock(blockPos.above(), blockState2, 3);
        }

    }

    static {
        BASE = BlockStateProperties.BOTTOM;
        NORTH = BlockStateProperties.NORTH_WALL;
        EAST = BlockStateProperties.EAST_WALL;
        SOUTH = BlockStateProperties.SOUTH_WALL;
        WEST = BlockStateProperties.WEST_WALL;
        PROPERTY_BY_DIRECTION = ImmutableMap.copyOf((Map) Util.make(Maps.newEnumMap(Direction.class), (enumMap) -> {
            enumMap.put(Direction.NORTH, NORTH);
            enumMap.put(Direction.EAST, EAST);
            enumMap.put(Direction.SOUTH, SOUTH);
            enumMap.put(Direction.WEST, WEST);
        }));
        DOWN_AABB = Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)1.0F, (double)16.0F);
        WEST_AABB = Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)1.0F, (double)16.0F, (double)16.0F);
        EAST_AABB = Block.box((double)15.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)16.0F, (double)16.0F);
        NORTH_AABB = Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)16.0F, (double)1.0F);
        SOUTH_AABB = Block.box((double)0.0F, (double)0.0F, (double)15.0F, (double)16.0F, (double)16.0F, (double)16.0F);
        WEST_SHORT_AABB = Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)1.0F, (double)10.0F, (double)16.0F);
        EAST_SHORT_AABB = Block.box((double)15.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)10.0F, (double)16.0F);
        NORTH_SHORT_AABB = Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)10.0F, (double)1.0F);
        SOUTH_SHORT_AABB = Block.box((double)0.0F, (double)0.0F, (double)15.0F, (double)16.0F, (double)10.0F, (double)16.0F);
    }
}
