package dev.svrt.domiirl.mbf.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.errors.ErrorSystemManager;
import dev.svrt.domiirl.mbf.RendererUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.DonkeyRenderState;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class HorseBaseBannerFeatureRenderer extends RenderLayer<EquineRenderState, HorseModel> {

	public HorseBaseBannerFeatureRenderer(RenderLayerParent<EquineRenderState, HorseModel> renderLayerParent) {
		super(renderLayerParent);
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, EquineRenderState entityRenderState, float f, float g) {
		renderSideBanner(matrices, vertexConsumers, light, entityRenderState);
	}

	public static void renderSideBanner(PoseStack matrices, MultiBufferSource vertexConsumers, int light, EquineRenderState state) {
		if (state instanceof Bannerable bannerable) {
			matrices.pushPose();

			// ENTITY AND ITEM PREPARATIONS
			ItemStack itemStack = bannerable.getBannerItem();
			if (itemStack.isEmpty() || !(itemStack.getItem() instanceof BannerItem)) {
				matrices.popPose();
				return;
			}

			if (state instanceof EquineRenderState abstractHorse) {
				float o = abstractHorse.standAnimation;
				matrices.mulPose(Axis.XP.rotation(o * -0.7853982F));
				matrices.translate(0, o * -0.4136991F, o * 0.3926991F);
			}

			// RENDERING PREPARATIONS
			matrices.scale(0.6f, 0.6f, 0.6f);
			matrices.mulPose(Axis.XP.rotationDegrees(180));

			// FIRST BANNER
			matrices.pushPose();

			// START MODIFYING
			matrices.mulPose(Axis.YP.rotationDegrees(90));
//			scaleMatricesForEntity(matrices, state);
			modifyMatricesDefault(state, matrices, true);
//			modifyMatricesForEntity(matrices, state, true);

			// FINISHED MODIFYING
			renderBanner(matrices, vertexConsumers, light, state, itemStack);
			matrices.popPose();

			// SECOND BANNER

			// START MODIFYING
			matrices.mulPose(Axis.YN.rotationDegrees(90));
//			scaleMatricesForEntity(matrices, state);
			modifyMatricesDefault(state, matrices, false);
//			modifyMatricesForEntity(matrices, state, false);

			// FINISHED MODIFYING
			renderBanner(matrices, vertexConsumers, light, state, itemStack);
			matrices.popPose();
		}
	}

	private static void modifyMatricesDefault(EquineRenderState state, PoseStack matrices, boolean first) {
		float y = 0;
		float x = 0;
		float zOff = 0;
		boolean hasChest = state instanceof DonkeyRenderState donkeyRenderState && donkeyRenderState.hasChest;

		if (state instanceof DonkeyRenderState donkeyRenderState) {
			zOff -= hasChest ? 0.3F : -0.25F;
			x = 0F;
			y = -2.43F;
		} else {
			x = 0.125F;
			y = -1.92F;
			zOff = 0.3F;
		}

		if (!state.saddle.isEmpty()) {
			if (!hasChest) {
				x += 0.04F;
			}
			y += 0.05F;
		}

		if (first) {
			matrices.translate(-0.6982422 + zOff, y, x);
		} else {
			matrices.translate(-0.3491211 - zOff, y, x);
		}
	}

//	private static Vec3 modifyMatricesForEntity(PoseStack matrices, EntityRenderState entity, boolean first) {
//
//		if (entity instanceof Bannerable bannerable) {
//
//			Vec3 offset = new Vec3(bannerable.getXOffset(), bannerable.getYOffset(), bannerable.getZOffset());
//
//			if (first) {
//				matrices.translate(-offset.z(), -offset.y(), offset.x());
//			} else {
//				matrices.translate(offset.z(), -offset.y(), offset.x());
//			}
//
//			return offset;
//		}
//
//		return Vec3.ZERO;
//	}
//
//	private static void scaleMatricesForEntity(PoseStack matrices, EntityRenderState entity) {
//
//		if (entity instanceof SideBannerable bannerable) {
//			Vec3 scaleOffset = bannerable.getScaleOffset();
//			if (scaleOffset != null) matrices.scale((float) scaleOffset.x(), (float) scaleOffset.y(), (float) scaleOffset.z());
//		}
//
//	}

	private static void renderBanner(PoseStack matrices, MultiBufferSource vertexConsumers, int light, LivingEntityRenderState entity, ItemStack itemStack) {
//		RendererUtils.modifyMatricesBannerSwing(flagPart, state, false, aFloat -> Float.valueOf(-aFloat));
		matrices.pushPose();

		// Safety try catch to avoid crashes!
		try {
			RendererUtils.renderBanner(
				matrices, vertexConsumers, light,
				LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 0.0F,
				RendererUtils.BANNER_BAR, RendererUtils.STANDING_FLAG_BANNER,
				RendererUtils.createBannerSwing(entity),
				itemStack
			);

//			matrices.pushPose();
//			if (bar) {
//				matrices.translate(0, 1.99, -0.07);
//
//				int overlay = LivingEntityRenderer.getOverlayCoords(state, 0.0F);
//				VertexConsumer vertexConsumer = ModelBakery.BANNER_BASE.buffer(vertexConsumers, RenderType::entitySolid);
////				crossbarPart.render(matrices, vertexConsumer, light, overlay);
//			}
//			matrices.popPose();

		} catch (Exception exception) {
			ErrorSystemManager.reportException();
			exception.printStackTrace();
		}
		matrices.popPose();

	}
}
