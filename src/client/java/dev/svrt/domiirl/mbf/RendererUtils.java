package dev.svrt.domiirl.mbf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BannerFlagModel;
import net.minecraft.client.model.BannerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public class RendererUtils {

	public static final BannerModel STANDING_BANNER = new BannerModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER));
	public static final BannerModel BANNER_BAR = new BannerModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER));
	public static final BannerModel WALL_BANNER = new BannerModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.WALL_BANNER));
	public static final BannerFlagModel STANDING_FLAG_BANNER = new BannerFlagModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER_FLAG));
	public static final BannerFlagModel WALL_FLAG_BANNER = new BannerFlagModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.WALL_BANNER_FLAG));

	public static boolean nextBannerGlint = false;

	private static final DeltaTracker deltaTracker = Minecraft.getInstance().getDeltaTracker();

	static {
		ModelPart part = BANNER_BAR.root();
		if (part.hasChild("pole")) {
			part.getChild("pole").visible = false;
		}
	}

	public static float createBannerSwing(EntityRenderState entityRenderState) {
		return ((float)Math.floorMod((long)(entityRenderState.x * 7 + entityRenderState.y * 9 + entityRenderState.z * 13) + Minecraft.getInstance().level.getGameTime(), 100L) + deltaTracker.getGameTimeDeltaPartialTick(false)) / 100.0F;
	}

	public static void renderBanner(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int j, float f, BannerModel bannerModel, BannerFlagModel bannerFlagModel, float g, ItemStack itemStack) {
		DyeColor dyeColor = itemStack.getItem() instanceof BannerItem bannerItem ? bannerItem.getColor() : itemStack.getOrDefault(ModDataComponents.BANNER_BASE_COLOR, DyeColor.WHITE);
		BannerPatternLayers bannerPatternLayers = itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
		renderBanner(poseStack, multiBufferSource, light, j, f, bannerModel, bannerFlagModel, g, dyeColor, bannerPatternLayers);
	}

	public static void renderBanner(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, BannerModel bannerModel, BannerFlagModel bannerFlagModel, float g, DyeColor dyeColor, BannerPatternLayers bannerPatternLayers) {
		poseStack.pushPose();
		poseStack.translate(0.5F, 0.0F, 0.5F);
		poseStack.mulPose(Axis.YP.rotationDegrees(f));
		poseStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
		if (bannerModel != null) {
			bannerModel.renderToBuffer(poseStack, ModelBakery.BANNER_BASE.buffer(multiBufferSource, RenderType::entitySolid), i, j);
		}
		if (bannerFlagModel != null) {
			bannerFlagModel.setupAnim(g);
			BannerRenderer.renderPatterns(poseStack, multiBufferSource, i, j, bannerFlagModel.root(), ModelBakery.BANNER_BASE, true, dyeColor, bannerPatternLayers);
		}
		poseStack.popPose();
	}

	public static void renderCanvasFromItem(ItemStack itemStack, PoseStack matrixStack, MultiBufferSource vertexConsumers, int light, int overlay, ModelPart canvas) {
		DyeColor dyeColor = itemStack.getItem() instanceof BannerItem bannerItem ? bannerItem.getColor() : itemStack.getOrDefault(ModDataComponents.BANNER_BASE_COLOR, DyeColor.WHITE);
		BannerPatternLayers patternLayers = itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
		BannerRenderer.renderPatterns(matrixStack, vertexConsumers, light, overlay, canvas, ModelBakery.BANNER_BASE, true, dyeColor, patternLayers, itemStack.hasFoil(), itemStack.hasFoil());
	}

	public static boolean isLegitPlayerBannerEquipment(ItemStack itemStack) {
		return !itemStack.isEmpty() && (itemStack.getItem() instanceof BannerItem || (itemStack.has(DataComponents.BANNER_PATTERNS) && itemStack.has(ModDataComponents.BANNER_BASE_COLOR)));
	}

}
