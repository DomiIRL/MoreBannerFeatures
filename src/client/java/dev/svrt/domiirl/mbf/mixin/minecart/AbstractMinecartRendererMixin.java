package dev.svrt.domiirl.mbf.mixin.minecart;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.Poses;
import dev.svrt.domiirl.mbf.RendererUtils;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.errors.ErrorSystemManager;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21.9 {
import net.minecraft.client.renderer.state.level.CameraRenderState;
//?}

//? if >=1.21.2 {
import net.minecraft.client.renderer.entity.AbstractMinecartRenderer;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import dev.svrt.domiirl.mbf.accessor.BannerableMinecartRenderState;
//?} else {
/*import net.minecraft.client.renderer.entity.MinecartRenderer;
*///?}

//? if >=1.21.2 {
@Mixin(AbstractMinecartRenderer.class)
public abstract class AbstractMinecartRendererMixin<T extends AbstractMinecart, S extends MinecartRenderState> extends EntityRenderer<T, S> {
//?} else {
/*@Mixin(MinecartRenderer.class)
public abstract class AbstractMinecartRendererMixin<T extends AbstractMinecart> extends EntityRenderer<T> {
*///?}

	protected AbstractMinecartRendererMixin(EntityRendererProvider.Context context) {
		super(context);
	}

	//? if >=1.21.2 {
	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;Lnet/minecraft/client/renderer/entity/state/MinecartRenderState;F)V", at = @At("HEAD"), cancellable = true)
	private void extractRenderState(T minecart, S state, float f, CallbackInfo ci) {
		if (state instanceof Bannerable bannerRenderState && minecart instanceof Bannerable bannerable) {
			bannerRenderState.mbf$setBannerItem(bannerable.mbf$getBannerItem());
		}
		if (state instanceof BannerableMinecartRenderState customState) {
			customState.setVelocity(minecart.getDeltaMovement());
		}
	}
	//?}

	//? if >=1.21.2 {
	//? if >=1.21.9 {
	@Inject(
		method = "submit(Lnet/minecraft/client/renderer/entity/state/MinecartRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
		//? if >=26.3 {
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/resources/Identifier;III)V", shift = At.Shift.AFTER)
		//?} else if <26.1 {
		/*at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V", shift = At.Shift.AFTER)
		*///?} else {
		/*at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/resources/Identifier;IIILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V", shift = At.Shift.AFTER)
		*///?}
	)
	private void render(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
		int light = state.lightCoords;
	//?} else {
	/*@Inject(
		method = "render(Lnet/minecraft/client/renderer/entity/state/MinecartRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/MinecartModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V", shift = At.Shift.AFTER)
	)
	private void render(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, CallbackInfo ci) {
	*///?}
	//?} else {
	/*@Inject(
		method = "render(Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V", shift = At.Shift.AFTER)
	)
	private void render(T state, float yaw, float partialTick, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, CallbackInfo ci) {
	*///?}
		poseStack.pushPose();
		try {
			if (state instanceof Bannerable bannerable && bannerable.mbf$isEnabled()) {
				ItemStack itemStack = bannerable.mbf$getBannerItem();
				if (!itemStack.isEmpty() && itemStack.getItem() instanceof BannerItem) {
					Poses.rotateDegrees(poseStack, Axis.XP, 180);
					Poses.rotateDegrees(poseStack, Axis.YP, 90);

					//? if >=1.21.2 {
					Vec3 velocity = state instanceof BannerableMinecartRenderState minecartState ? minecartState.getVelocity() : Vec3.ZERO;
					//?} else {
					/*Vec3 velocity = state.getDeltaMovement();
					*///?}
					boolean inverted = velocity.x < 0 || velocity.z < 0;

					if (!inverted) {
						Poses.rotateDegrees(poseStack, Axis.YP, 180);
					}

					poseStack.translate(-0.5, 0.3, -1.06);
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
