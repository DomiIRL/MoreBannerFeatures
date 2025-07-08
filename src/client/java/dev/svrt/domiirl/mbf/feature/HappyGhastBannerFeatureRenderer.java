package dev.svrt.domiirl.mbf.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.feature.side.GhastBannerPositionProvider;
import dev.svrt.domiirl.mbf.feature.side.SideBannerRenderer;
import net.minecraft.client.model.HappyGhastModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HappyGhastRenderState;

public class HappyGhastBannerFeatureRenderer extends RenderLayer<HappyGhastRenderState, HappyGhastModel> {

  private final SideBannerRenderer bannerRenderer;

  public HappyGhastBannerFeatureRenderer(RenderLayerParent<HappyGhastRenderState, HappyGhastModel> renderLayerParent) {
    super(renderLayerParent);
    this.bannerRenderer = new SideBannerRenderer(new GhastBannerPositionProvider());
  }

  @Override
  public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, HappyGhastRenderState entityRenderState, float f, float g) {
    if (entityRenderState instanceof Bannerable) {
      bannerRenderer.renderSideBanners(poseStack, multiBufferSource, i, entityRenderState);
    }
  }
}
