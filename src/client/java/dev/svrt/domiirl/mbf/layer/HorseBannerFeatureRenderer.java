package dev.svrt.domiirl.mbf.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.layer.side.EquineBannerPositionProvider;
import dev.svrt.domiirl.mbf.layer.side.SideBannerRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.animal.equine.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.client.resources.model.MaterialSet;

@Environment(EnvType.CLIENT)
public class HorseBannerFeatureRenderer extends RenderLayer<EquineRenderState, HorseModel> {

	private final SideBannerRenderer bannerRenderer;

	public HorseBannerFeatureRenderer(RenderLayerParent<EquineRenderState, HorseModel> renderLayerParent, MaterialSet materials, boolean undead) {
		super(renderLayerParent);
		this.bannerRenderer = new SideBannerRenderer(new EquineBannerPositionProvider(undead), materials);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, EquineRenderState entityRenderState, float f, float g) {
		if (entityRenderState instanceof Bannerable bannerable && bannerable.mbf$isEnabled()) {
			poseStack.pushPose();

			// Apply stand animation rotation
			if (entityRenderState.standAnimation > 0) {
				float o = entityRenderState.standAnimation;
				poseStack.mulPose(Axis.XP.rotation(o * -0.7853982F));
				poseStack.translate(0, o * -0.4136991F, o * 0.3926991F);
			}
			bannerRenderer.renderSideBanners(poseStack, submitNodeCollector, light, entityRenderState);
			poseStack.popPose();
		}
	}
}
