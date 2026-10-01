package dev.svrt.domiirl.mbf.mixin.banner;

import dev.svrt.domiirl.mbf.accessor.HangingBanner;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {

	// The block entity exists by now and can remember the intent
	@Inject(method = "place", at = @At("TAIL"))
	private void place(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir) {
		if (!(((Object) this) instanceof BannerItem)) {
			return;
		}

		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);

		if (!(state.getBlock() instanceof BannerBlock) || !(level.getBlockEntity(pos) instanceof HangingBanner banner)) {
			return;
		}

		boolean hanging = MBFOptions.HANGING_BANNERS.getBooleanValue()
			&& context.getNearestLookingVerticalDirection() == Direction.UP
			&& level.getBlockState(pos.above()).isSolid();

		banner.mbf$setHanging(hanging);
		((BlockEntity) banner).setChanged();
		level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
	}
}
