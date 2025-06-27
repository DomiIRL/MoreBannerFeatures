package dev.svrt.domiirl.morebannerfeatures.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PigRenderState;
import net.minecraft.world.entity.animal.Pig;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class PigBannerFeatureRenderer extends RenderLayer<PigRenderState, PigModel> {

	public PigBannerFeatureRenderer(RenderLayerParent<PigRenderState, PigModel> renderLayerParent) {
		super(renderLayerParent);
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, Pig entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		HorseBaseBannerFeatureRenderer.renderSideBanner(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, PigRenderState entityRenderState, float f, float g) {

	}
}
