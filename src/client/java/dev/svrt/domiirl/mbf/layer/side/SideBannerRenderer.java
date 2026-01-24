package dev.svrt.domiirl.mbf.layer.side;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.RendererUtils;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.errors.ErrorSystemManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BannerFlagModel;
import net.minecraft.client.model.BannerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

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

    bannerBar.root().rotateBy(Axis.XP.rotationDegrees(180)); // Initial orientation - banner faces downward by default
    bannerFlag.root().rotateBy(Axis.XP.rotationDegrees(180)); // Initial orientation - banner faces downward by default
    bannerBar.root().xScale = position.scale();
    bannerFlag.root().xScale = position.scale();
    bannerBar.root().yScale = position.scale();
    bannerFlag.root().yScale = position.scale();
    bannerBar.root().zScale = position.scale();
    bannerFlag.root().zScale = position.scale();
    bannerBar.root().setPos(position.xOffset(), position.yOffset(), position.zOffset());
    bannerFlag.root().setPos(position.xOffset(), position.yOffset(), position.zOffset());

    // Render left side banner
    stack.pushPose();
    renderBanner(stack, submitNodeCollector, light, state, itemStack, -90F);
    stack.popPose();

    // Render right side banner
    stack.pushPose();
    stack.translate(-position.entityWidth(), 0, 0);
    bannerBar.root().offsetPos(new Vector3f(position.xOffset() * -2, 0, 0));
    bannerFlag.root().offsetPos(new Vector3f(position.xOffset() * -2, 0, 0));
    renderBanner(stack, submitNodeCollector, light, state, itemStack, 90F);
    stack.popPose();
  }

  private void renderBanner(PoseStack matrices, SubmitNodeCollector submitNodeCollector, int light,
                            LivingEntityRenderState entity, ItemStack itemStack, float rotation) {
    matrices.pushPose();

    // Safety try catch to avoid crashes!
    try {
      int overlay = entity.hasRedOverlay ? OverlayTexture.RED_OVERLAY_V : OverlayTexture.NO_OVERLAY;

      RendererUtils.renderBanner(
        this.materials,
        matrices,
        submitNodeCollector,
        light,
        overlay,
        rotation,
        bannerBar,
        bannerFlag,
        RendererUtils.createBannerSwing(entity),
        itemStack
      );
    } catch (Exception exception) {
      ErrorSystemManager.reportException();
      exception.printStackTrace();
    }
    matrices.popPose();
  }
}