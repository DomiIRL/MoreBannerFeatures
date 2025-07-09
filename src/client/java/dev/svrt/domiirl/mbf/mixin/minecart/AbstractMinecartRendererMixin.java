package dev.svrt.domiirl.mbf.mixin.minecart;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.RendererUtils;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.accessor.BannerableMinecartRenderState;
import dev.svrt.domiirl.mbf.errors.ErrorSystemManager;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.AbstractMinecartRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartRenderer.class)
public abstract class AbstractMinecartRendererMixin<T extends AbstractMinecart, S extends MinecartRenderState> extends EntityRenderer<T, S> {

	protected AbstractMinecartRendererMixin(EntityRendererProvider.Context context) {
		super(context);
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/vehicle/AbstractMinecart;Lnet/minecraft/client/renderer/entity/state/MinecartRenderState;F)V", at = @At("HEAD"), cancellable = true)
	private void extractRenderState(T minecart, S state, float f, CallbackInfo ci) {
		if (state instanceof Bannerable bannerRenderState && minecart instanceof Bannerable bannerable) {
			bannerRenderState.moreBannerFeatures$setBannerItem(bannerable.moreBannerFeatures$getBannerItem());
		}
		if (state instanceof BannerableMinecartRenderState customState) {
			customState.setVelocity(minecart.getDeltaMovement());
		}
	}

	@Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/MinecartRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/MinecartModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V", shift = At.Shift.AFTER))
	private void render(S state, PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
		matrices.pushPose();
		try {
			if (state instanceof Bannerable bannerable && bannerable.moreBannerFeatures$isEnabled()) {
				ItemStack itemStack = bannerable.moreBannerFeatures$getBannerItem();
				if (!itemStack.isEmpty() && itemStack.getItem() instanceof BannerItem) {
					matrices.mulPose(Axis.XP.rotationDegrees(180));
					matrices.mulPose(Axis.YP.rotationDegrees(90));

					boolean inverted = false;

					if (state instanceof BannerableMinecartRenderState minecartState) {
						Vec3 velocity = minecartState.getVelocity();
						inverted = velocity.x < 0 || velocity.z < 0;
					}

					if (!inverted) {
						matrices.mulPose(Axis.YP.rotationDegrees(180));
					}

					matrices.translate(-0.5, 0.3, -1.06);

					RendererUtils.renderBanner(matrices, vertexConsumers, light, OverlayTexture.NO_OVERLAY, 0.0f, RendererUtils.STANDING_BANNER, RendererUtils.STANDING_FLAG_BANNER, RendererUtils.createBannerSwing(state), itemStack);
				}
			}
		} catch (Exception exception) {
			ErrorSystemManager.reportException();
			exception.printStackTrace();
		}
		matrices.popPose();
	}

}
