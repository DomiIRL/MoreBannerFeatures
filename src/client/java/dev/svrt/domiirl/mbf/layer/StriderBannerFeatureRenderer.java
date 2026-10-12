package dev.svrt.domiirl.mbf.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.Poses;
import dev.svrt.domiirl.mbf.RendererUtils;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.errors.ErrorSystemManager;
import net.minecraft.client.model.monster.strider.StriderModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;

//? if >=1.21.2 {
import net.minecraft.client.renderer.entity.state.StriderRenderState;
//?} else {
/*import net.minecraft.world.entity.monster.Strider;
*///?}

//? if >=1.21.2 {
public class StriderBannerFeatureRenderer extends RenderLayer<StriderRenderState, StriderModel> {
//?} else {
/*public class StriderBannerFeatureRenderer extends RenderLayer<Strider, StriderModel<Strider>> {
*///?}

	//? if >=1.21.2 {
	public StriderBannerFeatureRenderer(RenderLayerParent<StriderRenderState, StriderModel> context) {
	//?} else {
	/*public StriderBannerFeatureRenderer(RenderLayerParent<Strider, StriderModel<Strider>> context) {
	*///?}
		super(context);
	}

	@Override
	//? if >=1.21.2 {
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, StriderRenderState entity, float limbAngles, float limbDistance) {
	//?} else {
	/*public void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, Strider entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
	*///?}
		poseStack.pushPose();

		try {
			if (entity instanceof Bannerable bannerable && bannerable.mbf$isEnabled()) {
				ItemStack itemStack = bannerable.mbf$getBannerItem();
				if (!itemStack.isEmpty() && itemStack.getItem() instanceof BannerItem) {

					// Follow the body part, its bob and roll never reach the entity root
					this.getParentModel().root().getChild("body").translateAndRotate(poseStack);

					Poses.rotateDegrees(poseStack, Axis.XP, 180);

					poseStack.translate(-0.5, 0.25, -0.9);

					RendererUtils.renderBanner(
						poseStack,
						submitNodeCollector,
						light,
						OverlayTexture.NO_OVERLAY,
						0.0F,
						RendererUtils.STANDING_BANNER,
						RendererUtils.STANDING_FLAG_BANNER,
						RendererUtils.createBannerSwing(entity),
						itemStack
					);
				}
			}
		} catch (Exception exception) {
			ErrorSystemManager.reportException();
			exception.printStackTrace();
		}
		poseStack.popPose();
	}
}
