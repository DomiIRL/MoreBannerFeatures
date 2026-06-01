package dev.svrt.domiirl.mbf.layer.side;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.RendererUtils;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.errors.ErrorSystemManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.object.banner.BannerFlagModel;
import net.minecraft.client.model.object.banner.BannerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;

/**
 * Utility class for rendering side banners on entities.
 */
@Environment(EnvType.CLIENT)
public class SideBannerRenderer {

  private final BannerModel bannerBar;
  private final BannerFlagModel bannerFlag;
  private final BannerPositionProvider positionProvider;
  private final MaterialSet materials;

  public SideBannerRenderer(BannerPositionProvider positionProvider, MaterialSet materialSet) {
    this.positionProvider = positionProvider;
    this.materials = materialSet;

    this.bannerBar = new BannerModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER));
    this.bannerFlag = new BannerFlagModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER_FLAG));

    // Hide the pole part of the banner
    ModelPart part = bannerBar.root();
    if (part.hasChild("pole")) {
      part.getChild("pole").visible = false;
    }
  }

  /**
   * Renders side banners for an entity.
   */
  public void renderSideBanners(PoseStack stack, SubmitNodeCollector submitNodeCollector, int light,
                                LivingEntityRenderState state) {
    if (!(state instanceof Bannerable bannerable) || !bannerable.mbf$isEnabled()) {
      return;
    }

    ItemStack itemStack = bannerable.mbf$getBannerItem();
    if (itemStack.isEmpty() || !(itemStack.getItem() instanceof BannerItem)) {
      return;
    }

    BannerPositionProvider.BannerPosition position = positionProvider.getPosition(state);

    bannerBar.resetPose();
    bannerFlag.resetPose();

    // Render left side banner
    stack.pushPose();
    renderBanner(stack, submitNodeCollector, light, state, itemStack, -90F, position.scale(), position.xOffset(), position.yOffset(), position.zOffset());
    stack.popPose();

    // Render right side banner
    stack.pushPose();
    stack.translate(-position.entityWidth(), 0, 0);
    renderBanner(stack, submitNodeCollector, light, state, itemStack, 90F, position.scale(), -position.xOffset(), position.yOffset(), position.zOffset());
    stack.popPose();
  }

  private void renderBanner(PoseStack matrices, SubmitNodeCollector submitNodeCollector, int light,
                            LivingEntityRenderState entity, ItemStack itemStack, float rotationY, float scale, float x, float y, float z) {
    matrices.pushPose();

    // Safety try catch to avoid crashes!
    try {
      int overlay = entity.hasRedOverlay ? OverlayTexture.RED_OVERLAY_V : OverlayTexture.NO_OVERLAY;

      matrices.translate(0.5F, 0.0F, 0.5F);
      matrices.mulPose(Axis.YP.rotationDegrees(rotationY));
      matrices.scale(0.6666667F, -0.6666667F, -0.6666667F);

      matrices.translate(x / 16.0F, y / 16.0F, z / 16.0F);
      matrices.mulPose(Axis.XP.rotationDegrees(180));
      matrices.scale(scale, scale, scale);

      RendererUtils.renderBannerDirect(
        this.materials,
        matrices,
        submitNodeCollector,
        light,
        overlay,
        bannerBar,
        bannerFlag,
        RendererUtils.createBannerSwing(entity),
        itemStack
      );
    } catch (Exception exception) {
      ErrorSystemManager.reportException();
    }
    matrices.popPose();
  }
}