package dev.svrt.domiirl.mbf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.object.banner.BannerFlagModel;
import net.minecraft.client.model.object.banner.BannerModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public class RendererUtils {

	public static final BannerModel STANDING_BANNER = new BannerModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER));
	public static final BannerModel BANNER_BAR = new BannerModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER));
	public static final BannerFlagModel STANDING_FLAG_BANNER = new BannerFlagModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER_FLAG));

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

	public static void renderBanner(SpriteGetter sprites, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, float angle, BannerModel bannerModel, BannerFlagModel bannerFlagModel, float phase, ItemStack itemStack) {
		DyeColor dyeColor = itemStack.getItem() instanceof BannerItem bannerItem ? bannerItem.getColor() : itemStack.getOrDefault(ModDataComponents.BANNER_BASE_COLOR, DyeColor.WHITE);
		BannerPatternLayers bannerPatternLayers = itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
		renderBanner(sprites, poseStack, submitNodeCollector, light, overlay, angle, bannerModel, bannerFlagModel, phase, dyeColor, bannerPatternLayers);
	}

	public static void renderBanner(SpriteGetter sprites, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int j, float angle, BannerModel bannerModel, BannerFlagModel bannerFlagModel, float phase, DyeColor dyeColor, BannerPatternLayers bannerPatternLayers) {
		poseStack.pushPose();
		poseStack.translate(0.5F, 0.0F, 0.5F);
		poseStack.mulPose(Axis.YP.rotationDegrees(angle));
		poseStack.scale(0.6666667F, -0.6666667F, -0.6666667F);

		renderBannerDirect(sprites, poseStack, submitNodeCollector, i, j, bannerModel, bannerFlagModel, phase, dyeColor, bannerPatternLayers);

		poseStack.popPose();
	}

	public static void renderBannerDirect(SpriteGetter sprites, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int j, BannerModel bannerModel, BannerFlagModel bannerFlagModel, float phase, ItemStack itemStack) {
		DyeColor dyeColor = itemStack.getItem() instanceof BannerItem bannerItem ? bannerItem.getColor() : itemStack.getOrDefault(ModDataComponents.BANNER_BASE_COLOR, DyeColor.WHITE);
		BannerPatternLayers bannerPatternLayers = itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
		renderBannerDirect(sprites, poseStack, submitNodeCollector, i, j, bannerModel, bannerFlagModel, phase, dyeColor, bannerPatternLayers);
	}

	public static void renderBannerDirect(SpriteGetter sprites, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int j, BannerModel bannerModel, BannerFlagModel bannerFlagModel, float phase, DyeColor dyeColor, BannerPatternLayers bannerPatternLayers) {
		if (bannerModel != null) {
			submitNodeCollector.submitModel(bannerModel, Unit.INSTANCE, poseStack, i, j, -1, Sheets.BANNER_BASE, sprites, 0, null);
		}

		if (bannerFlagModel != null) {
			BannerRenderer.submitPatterns(sprites, poseStack, submitNodeCollector, i, j, bannerFlagModel, phase, false, dyeColor, bannerPatternLayers, null);
		}
	}

	public static <S> void renderCanvasFromItem(SpriteGetter sprites, ItemStack itemStack, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, Model<S> model, S object) {
		DyeColor dyeColor = itemStack.getItem() instanceof BannerItem bannerItem ? bannerItem.getColor() : itemStack.getOrDefault(ModDataComponents.BANNER_BASE_COLOR, DyeColor.WHITE);
		BannerPatternLayers patternLayers = itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);

		BannerRenderer.submitPatterns(sprites, poseStack, submitNodeCollector, light, overlay, model, object, false, dyeColor, patternLayers, null);
	}

	public static boolean isLegitPlayerBannerEquipment(ItemStack itemStack) {
		return !itemStack.isEmpty() && (itemStack.getItem() instanceof BannerItem || (itemStack.has(DataComponents.BANNER_PATTERNS) && itemStack.has(ModDataComponents.BANNER_BASE_COLOR)));
	}
}
