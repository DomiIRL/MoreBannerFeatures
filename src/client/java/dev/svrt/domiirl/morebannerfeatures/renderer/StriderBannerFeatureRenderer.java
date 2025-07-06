package dev.svrt.domiirl.morebannerfeatures.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.svrt.domiirl.morebannerfeatures.core.accessor.Bannerable;
import dev.svrt.domiirl.morebannerfeatures.errors.ErrorSystemManager;
import dev.svrt.domiirl.morebannerfeatures.RendererUtils;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BannerFlagModel;
import net.minecraft.client.model.BannerModel;
import net.minecraft.client.model.StriderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.StriderRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
public class StriderBannerFeatureRenderer extends RenderLayer<StriderRenderState, StriderModel> {

	public StriderBannerFeatureRenderer(RenderLayerParent<StriderRenderState, StriderModel> context) {
		super(context);
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, StriderRenderState entity, float limbAngles, float limbDistance) {
		// Safety try catch to avoid crashes!
		try {
			if (entity instanceof Bannerable bannerable) {
				ItemStack itemStack = bannerable.getBannerItem();
				if (itemStack == null || !(itemStack.getItem() instanceof BannerItem)) return;

				matrices.pushPose();

				matrices.mulPose(Axis.YP.rotationDegrees(entity.yRot));
				if (!entity.isRidden) {
					matrices.mulPose(Axis.XP.rotationDegrees(entity.xRot));
				}

				matrices.scale(0.7f, 0.7f, 0.7f);

				matrices.translate(0, -1, 0.6);

				RendererUtils.modifyMatricesFreezing(matrices, entity, entity.isFullyFrozen || entity.isSuffocating);

				RendererUtils.renderBanner(
					matrices, vertexConsumers, light, OverlayTexture.NO_OVERLAY,
					((float) Math.toDegrees(entity.yRot)),
					RendererUtils.STANDING_BANNER, RendererUtils.STANDING_FLAG_BANNER,
					RendererUtils.createBannerSwing(entity),
					itemStack
				);

				matrices.popPose();

			}
		} catch (Exception exception) {
			ErrorSystemManager.reportException();
			exception.printStackTrace();
		}
	}

}
