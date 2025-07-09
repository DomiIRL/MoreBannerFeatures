package dev.svrt.domiirl.mbf.mixin.boat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.RendererUtils;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.errors.ErrorSystemManager;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.AbstractBoatRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoatRenderer.class)
public abstract class AbstractBoatRendererMixin extends EntityRenderer<AbstractBoat, BoatRenderState> {

	public AbstractBoatRendererMixin(EntityRendererProvider.Context context) {
		super(context);
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/vehicle/AbstractBoat;Lnet/minecraft/client/renderer/entity/state/BoatRenderState;F)V", at = @At("HEAD"), cancellable = true)
	private void extractRenderState(AbstractBoat boat, BoatRenderState state, float f, CallbackInfo ci) {
		if (state instanceof Bannerable bannerRenderState && boat instanceof Bannerable bannerable) {
			bannerRenderState.moreBannerFeatures$setBannerItem(bannerable.moreBannerFeatures$getBannerItem());
		}
	}

	@Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/BoatRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/AbstractBoatRenderer;renderTypeAdditions(Lnet/minecraft/client/renderer/entity/state/BoatRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
	private void render(BoatRenderState entity, PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
		matrices.pushPose();
		try {
			if (entity instanceof Bannerable bannerable && bannerable.moreBannerFeatures$isEnabled()) {
				ItemStack itemStack = bannerable.moreBannerFeatures$getBannerItem();
				if (!itemStack.isEmpty() && itemStack.getItem() instanceof BannerItem) {
					matrices.mulPose(Axis.XP.rotationDegrees(180));
					matrices.mulPose(Axis.YP.rotationDegrees(90));

					matrices.translate(-0.5, 0, -1.44);

					RendererUtils.renderBanner(matrices, vertexConsumers, light, OverlayTexture.NO_OVERLAY, 0.0f, RendererUtils.STANDING_BANNER, RendererUtils.STANDING_FLAG_BANNER, RendererUtils.createBannerSwing(entity), itemStack);
				}
			}
		} catch (Exception exception) {
			ErrorSystemManager.reportException();
			exception.printStackTrace();
		}
		matrices.popPose();
	}

}
