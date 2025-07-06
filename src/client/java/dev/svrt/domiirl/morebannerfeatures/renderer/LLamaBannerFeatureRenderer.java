package dev.svrt.domiirl.morebannerfeatures.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LlamaRenderState;
import net.minecraft.world.entity.animal.horse.Llama;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class LLamaBannerFeatureRenderer extends RenderLayer<LlamaRenderState, LlamaModel> {

	public LLamaBannerFeatureRenderer(RenderLayerParent<LlamaRenderState, LlamaModel> renderLayerParent) {
		super(renderLayerParent);
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, LlamaRenderState entity, float limbAngle, float limbDistance) {
		HorseBaseBannerFeatureRenderer.renderSideBanner(matrices, vertexConsumers, light, entity);
	}

}
