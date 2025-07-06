package dev.svrt.domiirl.morebannerfeatures.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.CamelRenderState;
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
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, PigRenderState entityRenderState, float limbAngle, float limbDistance) {
		HorseBaseBannerFeatureRenderer.renderSideBanner(matrices, vertexConsumers, light, entityRenderState);
	}
}
