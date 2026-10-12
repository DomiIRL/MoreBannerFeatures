package dev.svrt.domiirl.mbf.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.Poses;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.layer.side.EquineBannerPositionProvider;
import dev.svrt.domiirl.mbf.layer.side.SideBannerRenderer;
import net.minecraft.client.model.animal.equine.HorseModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EquineRenderState;

public class HorseBannerFeatureRenderer extends RenderLayer<EquineRenderState, HorseModel> {

	private final SideBannerRenderer bannerRenderer;

	public HorseBannerFeatureRenderer(RenderLayerParent<EquineRenderState, HorseModel> renderLayerParent, boolean undead) {
		super(renderLayerParent);
		this.bannerRenderer = new SideBannerRenderer(new EquineBannerPositionProvider(undead));
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, EquineRenderState entityRenderState, float f, float g) {
		if (entityRenderState instanceof Bannerable bannerable && bannerable.mbf$isEnabled()) {
			poseStack.pushPose();

			// Apply stand animation rotation
			if (entityRenderState.standAnimation > 0) {
				float o = entityRenderState.standAnimation;
				Poses.rotate(poseStack, Axis.XP, o * -0.7853982F);
				poseStack.translate(0, o * -0.4136991F, o * 0.3926991F);
			}
			bannerRenderer.renderSideBanners(poseStack, submitNodeCollector, light, entityRenderState);
			poseStack.popPose();
		}
	}
}
