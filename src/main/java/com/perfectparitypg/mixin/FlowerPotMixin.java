package com.perfectparitypg.mixin;

import com.perfectparitypg.world.level.block.CreakingHeartBlock;
import com.perfectparitypg.world.level.block.EyeblossomBlock;
import com.perfectparitypg.world.level.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.class)
public class FlowerPotMixin {

	@Unique
	protected boolean isRandomlyTicking(BlockState blockState) {
		return blockState.is(ModBlocks.POTTED_OPEN_EYEBLOSSOM) || blockState.is(ModBlocks.POTTED_CLOSED_EYEBLOSSOM);
	}

	@Inject(method = "randomTick", at = @At("HEAD"))
	protected void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
		if (blockState.getBlock() instanceof FlowerPotBlock && this.isRandomlyTicking(blockState) && serverLevel.dimensionType().natural()) {
			if (blockState.getBlock() instanceof FlowerPotBlock) {
				boolean bl = ((FlowerPotBlock) blockState.getBlock()).getPotted() == ModBlocks.OPEN_EYEBLOSSOM;
				boolean bl2 = CreakingHeartBlock.isNaturalNight(serverLevel);
				if (bl != bl2) {
					serverLevel.setBlock(blockPos, this.opposite(blockState), 3);
					EyeblossomBlock.Type type = EyeblossomBlock.Type.fromBoolean(bl).transform();
					type.spawnTransformParticle(serverLevel, blockPos, randomSource);
					serverLevel.playSound((Entity) null, blockPos, type.longSwitchSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
				}
			}
		}
	}
	@Unique
	public BlockState opposite(BlockState blockState) {
		if (blockState.is(ModBlocks.POTTED_OPEN_EYEBLOSSOM)) {
			return ModBlocks.POTTED_CLOSED_EYEBLOSSOM.defaultBlockState();
		} else {
			return blockState.is(ModBlocks.POTTED_CLOSED_EYEBLOSSOM) ? ModBlocks.POTTED_OPEN_EYEBLOSSOM.defaultBlockState() : blockState;
		}
	}
}