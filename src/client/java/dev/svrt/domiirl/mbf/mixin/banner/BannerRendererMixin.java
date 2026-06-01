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
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.MaterialSet;
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
	@Shadow @Final private MaterialSet materials;

	@Inject(
		method = "submit(Lnet/minecraft/client/renderer/blockentity/state/BannerRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;submitBanner(Lnet/minecraft/client/resources/model/MaterialSet;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IIFLnet/minecraft/client/model/BannerModel;Lnet/minecraft/client/model/BannerFlagModel;FLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;I)V"
		),
		cancellable = true
	)
	private void onSubmit(BannerRenderState bannerRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
		if (!MBFOptions.HANGING_BANNERS.getBooleanValue()) {
			return;
		}

		// For now, check if it's standing (hanging banners are only on standing banners)
		if (bannerRenderState instanceof BannerRenderStateAccessor accessor && accessor.mbf$isHanging()) {
			poseStack.translate(0.0D, -0.85D, 0.0D);
			RendererUtils.renderBanner(
				this.materials,
				poseStack,
				submitNodeCollector,
				bannerRenderState.lightCoords,
				OverlayTexture.NO_OVERLAY,
				bannerRenderState.angle,
				RendererUtils.BANNER_BAR,
				this.standingFlagModel,
				bannerRenderState.phase,
				bannerRenderState.baseColor,
				bannerRenderState.patterns
			);
			ci.cancel();
		}
	}

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/level/block/entity/BannerBlockEntity;Lnet/minecraft/client/renderer/blockentity/state/BannerRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
		at = @At("TAIL")
	)
	private void onExtractRenderState(BannerBlockEntity bannerBlockEntity, BannerRenderState bannerRenderState, float f, Vec3 vec3, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, CallbackInfo ci) {
		BlockState state = bannerBlockEntity.getBlockState();

		// Check if this is a standing banner with the HANGING property
		if (state.getBlock() instanceof BannerBlock && state.hasProperty(BlockStateProperties.HANGING)) {
			boolean isHanging = state.getValue(BlockStateProperties.HANGING);
			((BannerRenderStateAccessor) bannerRenderState).mbf$setHanging(isHanging);
		} else {
			// Not a hanging banner
			((BannerRenderStateAccessor) bannerRenderState).mbf$setHanging(false);
		}
	}
}