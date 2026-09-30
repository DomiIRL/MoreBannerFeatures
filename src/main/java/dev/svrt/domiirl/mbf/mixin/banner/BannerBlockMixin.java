package dev.svrt.domiirl.mbf.mixin.banner;

import dev.svrt.domiirl.mbf.accessor.HangingBanner;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BannerBlock.class)
public abstract class BannerBlockMixin extends AbstractBannerBlock {

	protected BannerBlockMixin(DyeColor color, Properties settings) {
		super(color, settings);
	}

	// Kept on the block entity, a blockstate property would shift every id after it
	@Unique
	private static boolean mbf$isHanging(BlockGetter world, BlockPos pos) {
		return MBFOptions.HANGING_BANNERS.getBooleanValue()
			&& world.getBlockEntity(pos) instanceof HangingBanner banner
			&& banner.mbf$isHanging();
	}

	@Inject(method = "getShape", at = @At(value = "TAIL"), cancellable = true)
	private void getOutlineShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
		if (mbf$isHanging(world, pos)) {
			cir.setReturnValue(Block.box(1.3D, 14.0D, 1.3D, 14.7D, 16.0D, 14.7D));
		}
	}

	@Inject(method = "canSurvive", at = @At(value = "TAIL"), cancellable = true)
	private void canPlaceAt(BlockState state, LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (!MBFOptions.HANGING_BANNERS.getBooleanValue()) {
			return;
		}
		// No block entity yet means this is the placement check, so allow a ceiling
		if (mbf$isHanging(world, pos) || (world.getBlockEntity(pos) == null && !cir.getReturnValue())) {
			cir.setReturnValue(world.getBlockState(pos.above()).isSolid());
		}
	}

	// Vanilla only rechecks on a DOWN update, a hanging banner loses its support above
	@Inject(method = "updateShape", at = @At(value = "HEAD"), cancellable = true)
	private void getStateForNeighborUpdate(BlockState state, LevelReader world, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource randomSource, CallbackInfoReturnable<BlockState> cir) {
		if (!MBFOptions.HANGING_BANNERS.getBooleanValue() || !mbf$isHanging(world, pos)) {
			return;
		}
		cir.setReturnValue(direction == Direction.UP && !state.canSurvive(world, pos)
			? Blocks.AIR.defaultBlockState()
			: super.updateShape(state, world, scheduledTickAccess, pos, direction, neighborPos, neighborState, randomSource));
	}
}
