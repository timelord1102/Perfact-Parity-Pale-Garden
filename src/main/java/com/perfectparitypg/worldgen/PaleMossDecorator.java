package com.perfectparitypg.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.perfectparitypg.world.level.block.HangingMossBlock;
import com.perfectparitypg.world.level.block.ModBlocks;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.List;

public class PaleMossDecorator extends TreeDecorator {
    public static final MapCodec<PaleMossDecorator> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Codec.floatRange(0.0F, 1.0F).fieldOf("leaves_probability").forGetter((paleMossDecorator) -> paleMossDecorator.leavesProbability), Codec.floatRange(0.0F, 1.0F).fieldOf("trunk_probability").forGetter((paleMossDecorator) -> paleMossDecorator.trunkProbability), Codec.floatRange(0.0F, 1.0F).fieldOf("ground_probability").forGetter((paleMossDecorator) -> paleMossDecorator.groundProbability)).apply(instance, PaleMossDecorator::new));
    private final float leavesProbability;
    private final float trunkProbability;
    private final float groundProbability;

    protected TreeDecoratorType<?> type() {
        return ModTreeDecoratorType.PALE_MOSS;
    }

    public PaleMossDecorator(float f, float g, float h) {
        this.leavesProbability = f;
        this.trunkProbability = g;
        this.groundProbability = h;
    }

    public void place(TreeDecorator.Context context) {
        RandomSource randomSource = context.random();
        WorldGenLevel worldGenLevel = (WorldGenLevel)context.level();
        List<BlockPos> list = Util.shuffledCopy(context.logs(), randomSource);
        if (!list.isEmpty()) {
            Mutable<BlockPos> mutable = new MutableObject((BlockPos)list.getFirst());
            list.forEach((blockPosx) -> {
                if (blockPosx.getY() < ((BlockPos)mutable.getValue()).getY()) {
                    mutable.setValue(blockPosx);
                }

            });
            BlockPos blockPos = (BlockPos)mutable.getValue();
            if (randomSource.nextFloat() < this.groundProbability) {
                worldGenLevel.registryAccess().lookup(Registries.CONFIGURED_FEATURE).flatMap((registry) -> registry.get(ModVegetationFeatures.PALE_MOSS_PATCH)).ifPresent((reference) -> ((ConfiguredFeature)reference.value()).place(worldGenLevel, worldGenLevel.getLevel().getChunkSource().getGenerator(), randomSource, blockPos.above()));
            }

            context.logs().forEach((blockPosx) -> {
                if (randomSource.nextFloat() < this.trunkProbability) {
                    BlockPos blockPos2 = blockPosx.below();
                    if (context.isAir(blockPos2)) {
                        addMossHanger(blockPos2, context);
                    }
                }

            });
            context.leaves().forEach((blockPosx) -> {
                if (randomSource.nextFloat() < this.leavesProbability) {
                    BlockPos blockPos2 = blockPosx.below();
                    if (context.isAir(blockPos2)) {
                        addMossHanger(blockPos2, context);
                    }
                }

            });
        }
    }

    private static void addMossHanger(BlockPos blockPos, TreeDecorator.Context context) {
        while(context.isAir(blockPos.below()) && !((double)context.random().nextFloat() < (double)0.5F)) {
            context.setBlock(blockPos, (BlockState) ModBlocks.PALE_HANGING_MOSS.defaultBlockState().setValue(HangingMossBlock.TIP, false));
            blockPos = blockPos.below();
        }

        context.setBlock(blockPos, (BlockState)ModBlocks.PALE_HANGING_MOSS.defaultBlockState().setValue(HangingMossBlock.TIP, true));
    }
}

