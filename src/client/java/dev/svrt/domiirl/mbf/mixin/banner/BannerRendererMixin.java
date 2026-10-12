package dev.svrt.domiirl.mbf.mixin.banner;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.svrt.domiirl.mbf.RendererUtils;
import dev.svrt.domiirl.mbf.accessor.HangingBanner;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.client.model.object.banner.BannerFlagModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21.9 {
import dev.svrt.domiirl.mbf.accessor.BannerRenderStateAccessor;
import net.minecraft.client.renderer.blockentity.state.BannerRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
//?} else {
/*import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.properties.RotationSegment;
*///?}

@Mixin(value = BannerRenderer.class, priority = 10000)
//? if >=1.21.9 {
public abstract class BannerRendererMixin implements BlockEntityRenderer<BannerBlockEntity, BannerRenderState> {
//?} else {
/*public abstract class BannerRendererMixin implements BlockEntityRenderer<BannerBlockEntity> {
*///?}

	//? if >=1.21.4 {
	@Shadow @Final private BannerFlagModel standingFlagModel;
	//?}

	//? if >=1.21.9 {
	@Inject(
		method = "submit(Lnet/minecraft/client/renderer/blockentity/state/BannerRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
		at = @At(
			value = "INVOKE",
			//? if >=26.1 {
			target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;submitBanner(Lnet/minecraft/client/resources/model/sprite/SpriteGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IILnet/minecraft/client/model/object/banner/BannerModel;Lnet/minecraft/client/model/object/banner/BannerFlagModel;FLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;I)V"
			//?} else {
			/*target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;submitBanner(Lnet/minecraft/client/resources/model/sprite/SpriteGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IIFLnet/minecraft/client/model/object/banner/BannerModel;Lnet/minecraft/client/model/object/banner/BannerFlagModel;FLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;I)V"
			*///?}
		),
		cancellable = true
	)
	private void onSubmit(BannerRenderState bannerRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
		if (!MBFOptions.HANGING_BANNERS.getBooleanValue()) {
			return;
		}

		if (bannerRenderState instanceof BannerRenderStateAccessor accessor && accessor.mbf$isHanging()) {
			//? if >=26.1 {
			// submit() already applied the Transformation, so 0.85 blocks becomes 0.85/0.6666667 here
			poseStack.translate(0.0D, 1.275D, 0.0D);
			RendererUtils.renderBannerDirect(
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
			// submit() pops the pose after this call, cancelling would skip it
			poseStack.popPose();
			//?} else {
			/*poseStack.translate(0.0D, -0.85D, 0.0D);
			RendererUtils.renderBanner(
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
			*///?}
			ci.cancel();
		}
	}

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/level/block/entity/BannerBlockEntity;Lnet/minecraft/client/renderer/blockentity/state/BannerRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
		at = @At("TAIL")
	)
	private void onExtractRenderState(BannerBlockEntity bannerBlockEntity, BannerRenderState bannerRenderState, float f, Vec3 vec3, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, CallbackInfo ci) {
		boolean hanging = bannerBlockEntity.getBlockState().getBlock() instanceof BannerBlock
			&& bannerBlockEntity instanceof HangingBanner banner
			&& banner.mbf$isHanging();

		((BannerRenderStateAccessor) bannerRenderState).mbf$setHanging(hanging);
	}
	//?} else {
	/*@Inject(
		//? if >=1.21.5 {
		method = "render(Lnet/minecraft/world/level/block/entity/BannerBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IILnet/minecraft/world/phys/Vec3;)V",
		//?} else {
		/^method = "render(Lnet/minecraft/world/level/block/entity/BannerBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;II)V",
		^///?}
		//? if >=1.21.4 {
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;renderBanner(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IIFLnet/minecraft/client/model/object/banner/BannerModel;Lnet/minecraft/client/model/object/banner/BannerFlagModel;FLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;)V"
		),
		//?} else {
		/^// 1.21.1's render draws the banner inline, so take over from its start
		at = @At("HEAD"),
		^///?}
		cancellable = true
	)
	//? if >=1.21.5 {
	private void onRender(BannerBlockEntity bannerBlockEntity, float partialTick, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, Vec3 cameraPos, CallbackInfo ci) {
	//?} else {
	/^private void onRender(BannerBlockEntity bannerBlockEntity, float partialTick, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, CallbackInfo ci) {
	^///?}
		if (!MBFOptions.HANGING_BANNERS.getBooleanValue()) {
			return;
		}

		if (bannerBlockEntity.getBlockState().getBlock() instanceof BannerBlock && bannerBlockEntity instanceof HangingBanner banner && banner.mbf$isHanging()) {
			float angle = -RotationSegment.convertToDegrees(bannerBlockEntity.getBlockState().getValue(BannerBlock.ROTATION));
			BlockPos blockPos = bannerBlockEntity.getBlockPos();
			float phase = ((float) Math.floorMod(blockPos.getX() * 7L + blockPos.getY() * 9L + blockPos.getZ() * 13L + bannerBlockEntity.getLevel().getGameTime(), 100L) + partialTick) / 100.0F;
			poseStack.translate(0.0D, -0.85D, 0.0D);
			RendererUtils.renderBanner(
				poseStack,
				submitNodeCollector,
				light,
				overlay,
				angle,
				RendererUtils.BANNER_BAR,
				//? if >=1.21.4 {
				this.standingFlagModel,
				//?} else {
				/^RendererUtils.STANDING_FLAG_BANNER,
				^///?}
				phase,
				bannerBlockEntity.getBaseColor(),
				//? if >=1.20.5 {
				bannerBlockEntity.getPatterns()
				//?} else {
				/^BannerPatternLayers.fromRenderList(bannerBlockEntity.getPatterns())
				^///?}
			);
			ci.cancel();
		}
	}
	*///?}
}
