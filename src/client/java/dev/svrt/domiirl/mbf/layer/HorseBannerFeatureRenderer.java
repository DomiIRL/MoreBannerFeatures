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

//? if >=1.21.2 {
import net.minecraft.client.renderer.entity.state.EquineRenderState;
//?} else {
/*import net.minecraft.world.entity.animal.equine.AbstractHorse;
*///?}

//? if >=1.21.2 {
public class HorseBannerFeatureRenderer extends RenderLayer<EquineRenderState, HorseModel> {
//?} else {
/*public class HorseBannerFeatureRenderer extends RenderLayer<AbstractHorse, HorseModel<AbstractHorse>> {
*///?}

	private final SideBannerRenderer bannerRenderer;
	//? if <1.21.2 {
	/*private final float rendererScale;
	*///?}

	//? if >=1.21.2 {
	public HorseBannerFeatureRenderer(RenderLayerParent<EquineRenderState, HorseModel> renderLayerParent, boolean undead) {
	//?} else {
	/*public HorseBannerFeatureRenderer(RenderLayerParent<AbstractHorse, HorseModel<AbstractHorse>> renderLayerParent, boolean undead, float rendererScale) {
	*///?}
		super(renderLayerParent);
		this.bannerRenderer = new SideBannerRenderer(new EquineBannerPositionProvider(undead));
		//? if <1.21.2 {
		/*this.rendererScale = rendererScale;
		*///?}
	}

	@Override
	//? if >=1.21.2 {
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, EquineRenderState entityRenderState, float f, float g) {
		float standAnimation = entityRenderState.standAnimation;
	//?} else {
	/*public void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, AbstractHorse entityRenderState, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
		float standAnimation = entityRenderState.getStandAnim(partialTick);
	*///?}
		if (entityRenderState instanceof Bannerable bannerable && bannerable.mbf$isEnabled()) {
			poseStack.pushPose();

			//? if <1.21.2 {
			/*// The renderer scales the whole pose until 1.21.2, which scales the model mesh instead.
			// The banner offsets are set for the unscaled pose, so undo it around the feet like the renderer applied it
			poseStack.translate(0.0F, 1.501F, 0.0F);
			poseStack.scale(1.0F / this.rendererScale, 1.0F / this.rendererScale, 1.0F / this.rendererScale);
			poseStack.translate(0.0F, -1.501F, 0.0F);
			*///?}

			// Apply stand animation rotation
			if (standAnimation > 0) {
				Poses.rotate(poseStack, Axis.XP, standAnimation * -0.7853982F);
				poseStack.translate(0, standAnimation * -0.4136991F, standAnimation * 0.3926991F);
			}
			bannerRenderer.renderSideBanners(poseStack, submitNodeCollector, light, entityRenderState);
			poseStack.popPose();
		}
	}
}
