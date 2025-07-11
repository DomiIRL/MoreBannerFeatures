package dev.svrt.domiirl.mbf.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.layer.side.EquineBannerPositionProvider;
import dev.svrt.domiirl.mbf.layer.side.SideBannerRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EquineRenderState;

@Environment(EnvType.CLIENT)
public class HorseBannerFeatureRenderer extends RenderLayer<EquineRenderState, HorseModel> {

	private final SideBannerRenderer bannerRenderer;

	public HorseBannerFeatureRenderer(RenderLayerParent<EquineRenderState, HorseModel> renderLayerParent, boolean undead) {
		super(renderLayerParent);
		this.bannerRenderer = new SideBannerRenderer(new EquineBannerPositionProvider(undead));
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, EquineRenderState entityRenderState, float f, float g) {
		if (entityRenderState instanceof Bannerable bannerable && bannerable.mbf$isEnabled()) {
			matrices.pushPose();

			// Apply stand animation rotation
			if (entityRenderState.standAnimation > 0) {
				float o = entityRenderState.standAnimation;
				matrices.mulPose(Axis.XP.rotation(o * -0.7853982F));
				matrices.translate(0, o * -0.4136991F, o * 0.3926991F);
			}
			bannerRenderer.renderSideBanners(matrices, vertexConsumers, light, entityRenderState);
			matrices.popPose();
		}
	}
}
