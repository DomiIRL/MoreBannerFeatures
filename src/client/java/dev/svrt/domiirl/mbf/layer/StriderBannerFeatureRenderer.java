package dev.svrt.domiirl.mbf.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.RendererUtils;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.errors.ErrorSystemManager;
import net.minecraft.client.model.monster.strider.StriderModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.StriderRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;

public class StriderBannerFeatureRenderer extends RenderLayer<StriderRenderState, StriderModel> {

	private final SpriteGetter sprites;

	public StriderBannerFeatureRenderer(RenderLayerParent<StriderRenderState, StriderModel> context, SpriteGetter materialSet) {
		super(context);
		this.sprites = materialSet;
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, StriderRenderState entity, float limbAngles, float limbDistance) {
		poseStack.pushPose();

		try {
			if (entity instanceof Bannerable bannerable && bannerable.mbf$isEnabled()) {
				ItemStack itemStack = bannerable.mbf$getBannerItem();
				if (!itemStack.isEmpty() && itemStack.getItem() instanceof BannerItem) {

					// Follow the strider's body part. It bobs (body.y), rolls (body.zRot) and pitches
					// as it walks, and none of that reaches the entity root, so a banner anchored
					// there drifts out of alignment. This also zeroes out correctly when ridden.
					this.getParentModel().root().getChild("body").translateAndRotate(poseStack);

					poseStack.mulPose(Axis.XP.rotationDegrees(180));

					poseStack.translate(-0.5, 0.25, -0.9);

					RendererUtils.renderBanner(
						this.sprites,
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
