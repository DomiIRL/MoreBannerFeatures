package dev.svrt.domiirl.mbf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.object.banner.BannerFlagModel;
import net.minecraft.client.model.object.banner.BannerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

//? if >=1.21.11 && <26.1 {
/*import net.minecraft.client.renderer.rendertype.RenderTypes;
*///?} else if <1.21.11 {
/*import net.minecraft.client.renderer.rendertype.RenderType;
*///?}

//? if >=1.21.9 {
import net.minecraft.client.model.Model;
import net.minecraft.util.Unit;
//?}

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

	public static void renderBanner(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, float angle, BannerModel bannerModel, BannerFlagModel bannerFlagModel, float phase, ItemStack itemStack) {
		DyeColor dyeColor = itemStack.getItem() instanceof BannerItem bannerItem ? bannerItem.getColor() : itemStack.getOrDefault(ModDataComponents.BANNER_BASE_COLOR, DyeColor.WHITE);
		BannerPatternLayers bannerPatternLayers = itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
		renderBanner(poseStack, submitNodeCollector, light, overlay, angle, bannerModel, bannerFlagModel, phase, dyeColor, bannerPatternLayers);
	}

	public static void renderBanner(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int j, float angle, BannerModel bannerModel, BannerFlagModel bannerFlagModel, float phase, DyeColor dyeColor, BannerPatternLayers bannerPatternLayers) {
		poseStack.pushPose();
		poseStack.translate(0.5F, 0.0F, 0.5F);
		Poses.rotateDegrees(poseStack, Axis.YP, angle);
		poseStack.scale(0.6666667F, -0.6666667F, -0.6666667F);

		renderBannerDirect(poseStack, submitNodeCollector, i, j, bannerModel, bannerFlagModel, phase, dyeColor, bannerPatternLayers);

		poseStack.popPose();
	}

	public static void renderBannerDirect(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int j, BannerModel bannerModel, BannerFlagModel bannerFlagModel, float phase, ItemStack itemStack) {
		DyeColor dyeColor = itemStack.getItem() instanceof BannerItem bannerItem ? bannerItem.getColor() : itemStack.getOrDefault(ModDataComponents.BANNER_BASE_COLOR, DyeColor.WHITE);
		BannerPatternLayers bannerPatternLayers = itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
		renderBannerDirect(poseStack, submitNodeCollector, i, j, bannerModel, bannerFlagModel, phase, dyeColor, bannerPatternLayers);
	}

	public static void renderBannerDirect(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int j, BannerModel bannerModel, BannerFlagModel bannerFlagModel, float phase, DyeColor dyeColor, BannerPatternLayers bannerPatternLayers) {
		//? if >=1.21.9 {
		if (bannerModel != null) {
			submitBase(poseStack, submitNodeCollector, i, j, bannerModel, Unit.INSTANCE);
		}

		if (bannerFlagModel != null) {
			submitCanvas(poseStack, submitNodeCollector, i, j, bannerFlagModel, phase, dyeColor, bannerPatternLayers);
		}
		//?} else {
		/*if (bannerModel != null) {
			bannerModel.renderToBuffer(poseStack, BannerSprites.cloth().buffer(submitNodeCollector, RenderTypes::entitySolid), i, j);
		}

		if (bannerFlagModel != null) {
			bannerFlagModel.setupAnim(phase);
			BannerRenderer.renderPatterns(poseStack, submitNodeCollector, i, j, bannerFlagModel.root(), BannerSprites.cloth(), true, dyeColor, bannerPatternLayers);
		}
		*///?}
	}

	//? if >=1.21.9 {
	public static <S> void renderCanvasFromItem(ItemStack itemStack, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, Model<S> model, S object) {
	//?} else {
	/*public static void renderCanvasFromItem(ItemStack itemStack, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, ModelPart canvas) {
	*///?}
		DyeColor dyeColor = itemStack.getItem() instanceof BannerItem bannerItem ? bannerItem.getColor() : itemStack.getOrDefault(ModDataComponents.BANNER_BASE_COLOR, DyeColor.WHITE);
		BannerPatternLayers patternLayers = itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);

		//? if >=1.21.9 {
		submitCanvas(poseStack, submitNodeCollector, light, overlay, model, object, dyeColor, patternLayers);
		//?} else {
		/*BannerRenderer.renderPatterns(poseStack, submitNodeCollector, light, overlay, canvas, BannerSprites.cloth(), true, dyeColor, patternLayers);
		*///?}
	}

	//? if >=1.21.9 {

	private static <S> void submitCanvas(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, Model<S> model, S state, DyeColor dyeColor, BannerPatternLayers patternLayers) {
		//? if >=26.1 {
		// Solid base pass first, the pattern layers write no depth
		submitBase(poseStack, submitNodeCollector, light, overlay, model, state);

		// Banner sprites, not shield ones, the shield atlas draws nothing here
		//? if >=26.3 {
		BannerRenderer.submitPatterns(BannerSprites.atlas(), poseStack, submitNodeCollector, light, overlay, model, state, true, dyeColor, patternLayers);
		//?} else {
		/*BannerRenderer.submitPatterns(BannerSprites.atlas(), poseStack, submitNodeCollector, light, overlay, model, state, true, dyeColor, patternLayers, null);
		*///?}
		//?} else {
		/*// The cloth sprite passed here is drawn as the base, so no separate base pass
		BannerRenderer.submitPatterns(BannerSprites.atlas(), poseStack, submitNodeCollector, light, overlay, model, state, BannerSprites.cloth(), true, dyeColor, patternLayers, false, null, 0);
		*///?}
	}

	private static <S> void submitBase(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, Model<S> model, S state) {
		//? if >=26.3 {
		submitNodeCollector.submitModel(model, state, poseStack, light, overlay, -1, BannerSprites.cloth(), BannerSprites.atlas(), 0);
		//?} else if >=26.1 {
		/*submitNodeCollector.submitModel(model, state, poseStack, light, overlay, -1, BannerSprites.cloth(), BannerSprites.atlas(), 0, null);
		*///?} else {
		/*submitNodeCollector.submitModel(model, state, poseStack, BannerSprites.cloth().renderType(RenderTypes::entitySolid), light, overlay, -1, BannerSprites.sprite(BannerSprites.cloth()), 0, null);
		*///?}
	}

	//?}

	public static boolean isLegitPlayerBannerEquipment(ItemStack itemStack) {
		return !itemStack.isEmpty() && (itemStack.getItem() instanceof BannerItem || (itemStack.has(DataComponents.BANNER_PATTERNS) && itemStack.has(ModDataComponents.BANNER_BASE_COLOR)));
	}
}
