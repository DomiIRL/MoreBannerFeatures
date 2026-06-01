package dev.svrt.domiirl.mbf.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.layer.side.GhastBannerPositionProvider;
import dev.svrt.domiirl.mbf.layer.side.SideBannerRenderer;
import net.minecraft.client.model.animal.ghast.HappyGhastModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HappyGhastRenderState;
import net.minecraft.client.resources.model.sprite.SpriteGetter;

public class HappyGhastBannerFeatureRenderer extends RenderLayer<HappyGhastRenderState, HappyGhastModel> {

  private final SideBannerRenderer bannerRenderer;

  public HappyGhastBannerFeatureRenderer(RenderLayerParent<HappyGhastRenderState, HappyGhastModel> renderLayerParent, SpriteGetter materials) {
    super(renderLayerParent);
    this.bannerRenderer = new SideBannerRenderer(new GhastBannerPositionProvider(), materials);
  }

  @Override
  public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, HappyGhastRenderState entityRenderState, float f, float g) {
    if (entityRenderState instanceof Bannerable) {
      bannerRenderer.renderSideBanners(poseStack, submitNodeCollector, i, entityRenderState);
    }
  }
}
