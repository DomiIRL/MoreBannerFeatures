package dev.svrt.domiirl.mbf.mixin.banner;

import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StandingAndWallBlockItem.class)
public abstract class StandingAndWallBlockItemMixin extends BlockItem {

	public StandingAndWallBlockItemMixin(Block block, Properties settings) {
		super(block, settings);
	}

	// Vanilla skips the direction opposite the attachment direction, so a banner aimed at a ceiling
	// never gets the standing state. Pick it here instead; BannerBlock.getStateForPlacement then
	// flags it as hanging.
	@Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
	private void getPlacementState(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
		if (!MBFOptions.HANGING_BANNERS.getBooleanValue()) {
			return;
		}
		if (!(((Object) this) instanceof BannerItem) || context.getNearestLookingVerticalDirection() != Direction.UP) {
			return;
		}

		LevelReader level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState hanging = this.getBlock().getStateForPlacement(context);

		if (hanging != null && hanging.canSurvive(level, pos) && level.isUnobstructed(hanging, pos, CollisionContext.empty())) {
			cir.setReturnValue(hanging);
		}
	}
}
