package dev.svrt.domiirl.mbf.mixin.boat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.Poses;
import dev.svrt.domiirl.mbf.RendererUtils;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.errors.ErrorSystemManager;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.AbstractBoatRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21.9 {
import net.minecraft.client.renderer.state.level.CameraRenderState;
//?}

@Mixin(AbstractBoatRenderer.class)
public abstract class AbstractBoatRendererMixin extends EntityRenderer<AbstractBoat, BoatRenderState> {

	protected AbstractBoatRendererMixin(EntityRendererProvider.Context context) {
		super(context);
	}

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/vehicle/boat/AbstractBoat;Lnet/minecraft/client/renderer/entity/state/BoatRenderState;F)V",
		at = @At("TAIL")
	)
	private void onExtractRenderState(AbstractBoat boat, BoatRenderState state, float f, CallbackInfo ci) {
		if (state instanceof Bannerable bannerRenderState && boat instanceof Bannerable bannerable) {
			bannerRenderState.mbf$setBannerItem(bannerable.mbf$getBannerItem());
		}
	}

	//? if >=1.21.9 {
	@Inject(
		method = "submit(Lnet/minecraft/client/renderer/entity/state/BoatRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/entity/AbstractBoatRenderer;submitTypeAdditions(Lnet/minecraft/client/renderer/entity/state/BoatRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
			shift = At.Shift.AFTER
		)
	)
	private void onSubmitTypeAdditions(BoatRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
		int light = state.lightCoords;
	//?} else {
	/*@Inject(
		method = "render(Lnet/minecraft/client/renderer/entity/state/BoatRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/entity/AbstractBoatRenderer;renderTypeAdditions(Lnet/minecraft/client/renderer/entity/state/BoatRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
			shift = At.Shift.AFTER
		)
	)
	private void onSubmitTypeAdditions(BoatRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, CallbackInfo ci) {
	*///?}
		poseStack.pushPose();
		try {
			if (state instanceof Bannerable bannerable && bannerable.mbf$isEnabled()) {
				ItemStack itemStack = bannerable.mbf$getBannerItem();
				if (!itemStack.isEmpty() && itemStack.getItem() instanceof BannerItem) {
					Poses.rotateDegrees(poseStack, Axis.XP, 180);
					Poses.rotateDegrees(poseStack, Axis.YP, 90);

					poseStack.translate(-0.5, 0, -1.44);

					RendererUtils.renderBanner(
						poseStack,
						submitNodeCollector,
						light,
						OverlayTexture.NO_OVERLAY,
						0.0f,
						RendererUtils.STANDING_BANNER,
						RendererUtils.STANDING_FLAG_BANNER,
						RendererUtils.createBannerSwing(state),
						itemStack
					);
				}
			}
		} catch (Exception exception) {
			ErrorSystemManager.reportException();
			exception.printStackTrace();
		}
		poseStack.popPose();
	}
}