package dev.svrt.domiirl.morebannerfeatures.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.CamelModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.CamelRenderState;
import net.minecraft.world.entity.animal.camel.Camel;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class CamelBannerFeatureRenderer extends RenderLayer<CamelRenderState, CamelModel> {

	public CamelBannerFeatureRenderer(RenderLayerParent<CamelRenderState, CamelModel> renderLayerParent) {
		super(renderLayerParent);
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, CamelRenderState entityRenderState, float limbAngle, float limbDistance) {
		HorseBaseBannerFeatureRenderer.renderSideBanner(matrices, vertexConsumers, light, entityRenderState);
	}
}
