package dev.svrt.domiirl.mbf.mixin.minecart;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.RendererUtils;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.accessor.BannerableMinecartRenderState;
import dev.svrt.domiirl.mbf.errors.ErrorSystemManager;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.AbstractMinecartRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartRenderer.class)
public abstract class AbstractMinecartRendererMixin<T extends AbstractMinecart, S extends MinecartRenderState> extends EntityRenderer<T, S> {

	@Unique
  private MaterialSet materials;

	protected AbstractMinecartRendererMixin(EntityRendererProvider.Context context) {
		super(context);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation, CallbackInfo ci) {
		this.materials = context.getMaterials();
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/vehicle/AbstractMinecart;Lnet/minecraft/client/renderer/entity/state/MinecartRenderState;F)V", at = @At("HEAD"), cancellable = true)
	private void extractRenderState(T minecart, S state, float f, CallbackInfo ci) {
		if (state instanceof Bannerable bannerRenderState && minecart instanceof Bannerable bannerable) {
			bannerRenderState.mbf$setBannerItem(bannerable.mbf$getBannerItem());
		}
		if (state instanceof BannerableMinecartRenderState customState) {
			customState.setVelocity(minecart.getDeltaMovement());
		}
	}

	@Inject(
		method = "submit(Lnet/minecraft/client/renderer/entity/state/MinecartRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/RenderType;IIILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V", shift = At.Shift.AFTER)
	)
	private void render(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
		poseStack.pushPose();
		try {
			if (state instanceof Bannerable bannerable && bannerable.mbf$isEnabled()) {
				ItemStack itemStack = bannerable.mbf$getBannerItem();
				if (!itemStack.isEmpty() && itemStack.getItem() instanceof BannerItem) {
					poseStack.mulPose(Axis.XP.rotationDegrees(180));
					poseStack.mulPose(Axis.YP.rotationDegrees(90));

					boolean inverted = false;

					if (state instanceof BannerableMinecartRenderState minecartState) {
						Vec3 velocity = minecartState.getVelocity();
						inverted = velocity.x < 0 || velocity.z < 0;
					}

					if (!inverted) {
						poseStack.mulPose(Axis.YP.rotationDegrees(180));
					}

					poseStack.translate(-0.5, 0.3, -1.06);
					RendererUtils.renderBanner(
						this.materials,
						poseStack,
						submitNodeCollector,
						state.lightCoords,
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
