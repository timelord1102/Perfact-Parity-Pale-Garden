package com.perfectparitypg.mixin;

import com.perfectparitypg.world.level.block.CreakingHeartBlock;
import com.perfectparitypg.world.level.block.EyeblossomBlock;
import com.perfectparitypg.world.level.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(FlowerPotBlock.class)
public class FlowerPotMixin extends Block {

	@Final
	@Shadow
	private Block potted;

	public FlowerPotMixin(Properties properties) {
		super(properties);
	}

	@Unique
	protected boolean isRandomlyTicking(BlockState blockState) {
		return blockState.is(ModBlocks.POTTED_OPEN_EYEBLOSSOM) || blockState.is(ModBlocks.POTTED_CLOSED_EYEBLOSSOM);
	}

	@Override
	protected void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
		if (this.isRandomlyTicking(blockState) && serverLevel.dimensionType().natural()) {
			boolean bl = this.potted == ModBlocks.OPEN_EYEBLOSSOM;
			boolean bl2 = CreakingHeartBlock.isNaturalNight(serverLevel);
			if (bl != bl2) {
				serverLevel.setBlock(blockPos, this.opposite(blockState), 3);
				EyeblossomBlock.Type type = EyeblossomBlock.Type.fromBoolean(bl).transform();
				type.spawnTransformParticle(serverLevel, blockPos, randomSource);
				serverLevel.playSound((Entity)null, blockPos, type.longSwitchSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
			}
		}

		super.randomTick(blockState, serverLevel, blockPos, randomSource);	}

	@Unique
	public BlockState opposite(BlockState blockState) {
		if (blockState.is(ModBlocks.POTTED_OPEN_EYEBLOSSOM)) {
			return ModBlocks.POTTED_CLOSED_EYEBLOSSOM.defaultBlockState();
		} else {
			return blockState.is(ModBlocks.POTTED_CLOSED_EYEBLOSSOM) ? ModBlocks.POTTED_OPEN_EYEBLOSSOM.defaultBlockState() : blockState;
		}
	}
}