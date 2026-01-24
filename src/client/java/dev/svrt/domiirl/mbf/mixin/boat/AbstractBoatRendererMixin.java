package dev.svrt.domiirl.mbf.mixin.boat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.RendererUtils;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.errors.ErrorSystemManager;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.AbstractBoatRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoatRenderer.class)
public abstract class AbstractBoatRendererMixin extends EntityRenderer<AbstractBoat, BoatRenderState> {

	@Unique
  private MaterialSet materials;

	protected AbstractBoatRendererMixin(EntityRendererProvider.Context context) {
		super(context);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(EntityRendererProvider.Context context, CallbackInfo ci) {
		this.materials = context.getMaterials();
	}

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/vehicle/AbstractBoat;Lnet/minecraft/client/renderer/entity/state/BoatRenderState;F)V",
		at = @At("TAIL")
	)
	private void onExtractRenderState(AbstractBoat boat, BoatRenderState state, float f, CallbackInfo ci) {
		if (state instanceof Bannerable bannerRenderState && boat instanceof Bannerable bannerable) {
			bannerRenderState.mbf$setBannerItem(bannerable.mbf$getBannerItem());
		}
	}

	@Inject(
		method = "submitTypeAdditions(Lnet/minecraft/client/renderer/entity/state/BoatRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
		at = @At("HEAD")
	)
	private void onSubmitTypeAdditions(BoatRenderState entity, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, CallbackInfo ci) {
		poseStack.pushPose();
		try {
			if (entity instanceof Bannerable bannerable && bannerable.mbf$isEnabled()) {
				ItemStack itemStack = bannerable.mbf$getBannerItem();
				if (!itemStack.isEmpty() && itemStack.getItem() instanceof BannerItem) {
					poseStack.mulPose(Axis.XP.rotationDegrees(180));
					poseStack.mulPose(Axis.YP.rotationDegrees(90));

					poseStack.translate(-0.5, 0, -1.44);

					RendererUtils.renderBanner(
						this.materials,
						poseStack,
						submitNodeCollector,
						light,
						OverlayTexture.NO_OVERLAY,
						0.0f,
						RendererUtils.STANDING_BANNER,
						RendererUtils.STANDING_FLAG_BANNER,
						RendererUtils.createBannerSwing(entity),
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