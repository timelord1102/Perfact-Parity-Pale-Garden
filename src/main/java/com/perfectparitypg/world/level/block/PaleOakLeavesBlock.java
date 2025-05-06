package com.perfectparitypg.world.level.block;

import com.mojang.serialization.MapCodec;
import com.perfectparitypg.particle.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CherryLeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PaleOakLeavesBlock extends CherryLeavesBlock {
    public static final MapCodec<CherryLeavesBlock> CODEC = simpleCodec(PaleOakLeavesBlock::new);

    public @NotNull MapCodec<CherryLeavesBlock> codec() {
        return CODEC;
    }

    public PaleOakLeavesBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        super.animateTick(blockState, level, blockPos, randomSource);
        if (randomSource.nextInt(10) == 0) {
            BlockPos blockPos2 = blockPos.below();
            BlockState blockState2 = level.getBlockState(blockPos2);
            if (!isFaceFull(blockState2.getCollisionShape(level, blockPos2), Direction.UP)) {
                ParticleUtils.spawnParticleBelow(level, blockPos, randomSource, ModParticles.PALE_OAK_LEAVES);
            }
        }
    }
}
