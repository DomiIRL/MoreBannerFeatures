package dev.svrt.domiirl.mbf.mixin.banner;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.svrt.domiirl.mbf.RendererUtils;
import dev.svrt.domiirl.mbf.accessor.BannerRenderStateAccessor;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.client.model.object.banner.BannerFlagModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BannerRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BannerRenderer.class, priority = 10000)
public abstract class BannerRendererMixin implements BlockEntityRenderer<BannerBlockEntity, BannerRenderState> {

	@Shadow @Final private BannerFlagModel standingFlagModel;
	@Shadow @Final private SpriteGetter sprites;

	@Inject(
		method = "submit(Lnet/minecraft/client/renderer/blockentity/state/BannerRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;submitBanner(Lnet/minecraft/client/resources/model/sprite/SpriteGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IILnet/minecraft/client/model/object/banner/BannerModel;Lnet/minecraft/client/model/object/banner/BannerFlagModel;FLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;I)V"
		),
		cancellable = true
	)
	private void onSubmit(BannerRenderState bannerRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
		if (!MBFOptions.HANGING_BANNERS.getBooleanValue()) {
			return;
		}

		if (bannerRenderState instanceof BannerRenderStateAccessor accessor && accessor.mbf$isHanging()) {
			// submit() has already applied the block's Transformation, so the banner only needs
			// lifting to the ceiling - translate is in that flipped, 0.6666667-scaled space.
			poseStack.translate(0.0D, -0.85D, 0.0D);
			RendererUtils.renderBannerDirect(
				this.sprites,
				poseStack,
				submitNodeCollector,
				bannerRenderState.lightCoords,
				OverlayTexture.NO_OVERLAY,
				RendererUtils.BANNER_BAR,
				this.standingFlagModel,
				bannerRenderState.phase,
				bannerRenderState.baseColor,
				bannerRenderState.patterns
			);
			// submit() pushed the pose before this call site and pops it after; cancelling skips that pop.
			poseStack.popPose();
			ci.cancel();
		}
	}

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/level/block/entity/BannerBlockEntity;Lnet/minecraft/client/renderer/blockentity/state/BannerRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
		at = @At("TAIL")
	)
	private void onExtractRenderState(BannerBlockEntity bannerBlockEntity, BannerRenderState bannerRenderState, float f, Vec3 vec3, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, CallbackInfo ci) {
		BlockState state = bannerBlockEntity.getBlockState();

		if (state.getBlock() instanceof BannerBlock && state.hasProperty(BlockStateProperties.HANGING)) {
			boolean isHanging = state.getValue(BlockStateProperties.HANGING);
			((BannerRenderStateAccessor) bannerRenderState).mbf$setHanging(isHanging);
		} else {
			((BannerRenderStateAccessor) bannerRenderState).mbf$setHanging(false);
		}
	}
}
