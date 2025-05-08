package com.perfectparitypg.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.perfectparitypg.world.level.block.CreakingHeartBlock;
import com.perfectparitypg.world.level.block.ModBlocks;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CreakingHeartDecorator extends TreeDecorator {
    public static final MapCodec<CreakingHeartDecorator> CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(CreakingHeartDecorator::new, (creakingHeartDecorator) -> creakingHeartDecorator.probability);
    private final float probability;

    public CreakingHeartDecorator(float f) {
        this.probability = f;
    }

    protected TreeDecoratorType<?> type() {
        return ModTreeDecoratorType.CREAKING_HEART;
    }

    public void place(TreeDecorator.Context context) {
        RandomSource randomSource = context.random();
        List<BlockPos> list = context.logs();
        if (!list.isEmpty()) {
            if (!(randomSource.nextFloat() >= this.probability)) {
                List<BlockPos> list2 = new ArrayList(list);
                Util.shuffle(list2, randomSource);
                Optional<BlockPos> optional = list2.stream().filter((blockPos) -> {
                    for(Direction direction : Direction.values()) {
                        if (!checkBlock(context, blockPos.relative(direction), (blockState) -> blockState.is(BlockTags.LOGS))) {
                            return false;
                        }
                    }

                    return true;
                }).findFirst();
                if (!optional.isEmpty()) {
                    context.setBlock((BlockPos)optional.get(), (BlockState)((BlockState) ModBlocks.CREAKING_HEART.defaultBlockState().setValue(CreakingHeartBlock.ACTIVE, true)).setValue(CreakingHeartBlock.NATURAL, true));
                }
            }
        }
    }

    public boolean checkBlock(TreeDecorator.Context context, BlockPos blockPos, Predicate<BlockState> predicate) {
        return context.level().isStateAtPosition(blockPos, predicate);
    }
}
