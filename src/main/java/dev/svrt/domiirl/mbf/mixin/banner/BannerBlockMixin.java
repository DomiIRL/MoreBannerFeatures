package dev.svrt.domiirl.mbf.mixin.banner;

import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0.2
 */
@Mixin(BannerBlock.class)
public abstract class BannerBlockMixin extends AbstractBannerBlock {

	private static final BooleanProperty HANGING;

	protected BannerBlockMixin(DyeColor color, Properties settings) {
		super(color, settings);
	}

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void init(DyeColor dyeColor, Properties settings, CallbackInfo ci) {
		this.registerDefaultState(this.defaultBlockState().setValue(HANGING, false));
	}

	@Inject(method = "getStateForPlacement", at = @At(value = "RETURN"), cancellable = true)
	private void getPlacementState(BlockPlaceContext ctx, CallbackInfoReturnable<BlockState> cir) {
		if (!MBFOptions.HANGING_BANNERS.getBooleanValue()) {
			return;
		}
		BlockState state = cir.getReturnValue();
		if (state == null) return;

		Direction[] var3 = ctx.getNearestLookingDirections();
		int var4 = var3.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			Direction direction = var3[var5];
			if (direction == Direction.UP && ctx.getNearestLookingVerticalDirection() == Direction.UP) {
				if (ctx.getLevel().getBlockState(ctx.getClickedPos().above()).isSolid()) {
					cir.setReturnValue(state.setValue(HANGING, true));
					return;
				}

			}
		}

	}

	@Inject(method = "createBlockStateDefinition", at = @At(value = "TAIL"))
	private void appendProperties(Builder<Block, BlockState> builder, CallbackInfo ci) {
		builder.add(HANGING);
	}

	@Inject(method = "getShape", at = @At(value = "TAIL"), cancellable = true)
	private void getOutlineShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
		if (!MBFOptions.HANGING_BANNERS.getBooleanValue()) {
			return;
		}
		if (state.getValue(HANGING)) {
			cir.setReturnValue(Block.box(1.3D, 14.0D, 1.3D, 14.7D, 16.0D, 14.7D));
		}
	}

	@Inject(method = "canSurvive", at = @At(value = "TAIL"), cancellable = true)
	private void canPlaceAt(BlockState state, LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (!MBFOptions.HANGING_BANNERS.getBooleanValue()) {
			return;
		}
		if (state.getValue(HANGING) || !cir.getReturnValue()) {
			cir.setReturnValue(world.getBlockState(pos.above()).isSolid());
		}
	}

	@Inject(method = "updateShape", at = @At(value = "HEAD"), cancellable = true)
	private void getStateForNeighborUpdate(BlockState state, LevelReader world, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource randomSource, CallbackInfoReturnable<BlockState> cir) {
		if (!MBFOptions.HANGING_BANNERS.getBooleanValue()) {
			return;
		}
		if (state.getValue(HANGING)) {
			cir.setReturnValue(direction == Direction.UP && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, world, scheduledTickAccess, pos, direction, neighborPos, neighborState, randomSource));
		}
	}

	static {
		HANGING = BlockStateProperties.HANGING;
	}

}
