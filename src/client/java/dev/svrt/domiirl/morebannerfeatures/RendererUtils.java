package dev.svrt.domiirl.morebannerfeatures;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import dev.svrt.domiirl.morebannerfeatures.core.accessor.SideBannerable;
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
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Function;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
public class RendererUtils {

	public static final BannerModel STANDING_BANNER = new BannerModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER));
	public static final BannerModel WALL_BANNER = new BannerModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.WALL_BANNER));
	public static final BannerFlagModel STANDING_FLAG_BANNER = new BannerFlagModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER_FLAG));
	public static final BannerFlagModel WALL_FLAG_BANNER = new BannerFlagModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.WALL_BANNER_FLAG));

	public static boolean nextBannerGlint = false;

	private static final DeltaTracker deltaTracker = Minecraft.getInstance().getDeltaTracker();

	public static float createBannerSwing(EntityRenderState entityRenderState) {
		return ((float)Math.floorMod((long)(entityRenderState.x * 7 + entityRenderState.y * 9 + entityRenderState.z * 13) + Minecraft.getInstance().level.getGameTime(), 100L) + deltaTracker.getGameTimeDeltaPartialTick(false)) / 100.0F;
	}

	public static void renderBanner(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, BannerModel bannerModel, BannerFlagModel bannerFlagModel, float g, ItemStack itemStack) {
		if (itemStack.getItem() instanceof BannerItem bannerItem) {
			DyeColor dyeColor = bannerItem.getColor();
			BannerPatternLayers bannerPatternLayers = itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
			renderBanner(poseStack, multiBufferSource, i, j, f, bannerModel, bannerFlagModel, g, dyeColor, bannerPatternLayers);
		} else {
			System.err.println("Tried to render banner without BannerItem! ItemStack: " + itemStack);
		}
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

	public static void modifyMatricesFreezing(PoseStack matrices, EntityRenderState entity, boolean freezing) {
		if (freezing) {
			float yaw = (float) (Math.cos((double) entity.ageInTicks * 3.25D) * 3.141592653589793D * 0.4000000059604645D);
			matrices.mulPose(Axis.YP.rotationDegrees(yaw));
		}
	}

	public static void renderCanvasFromItem(ItemStack itemStack, PoseStack matrixStack, MultiBufferSource vertexConsumers, int light, int overlay, ModelPart canvas) {
		if (itemStack.getItem() instanceof BannerItem) {
			DyeColor dyeColor = ((BannerItem) itemStack.getItem()).getColor();
			BannerPatternLayers patternLayers = itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
			BannerRenderer.renderPatterns(matrixStack, vertexConsumers, light, overlay, canvas, ModelBakery.BANNER_BASE, true, dyeColor, patternLayers, itemStack.hasFoil(), itemStack.hasFoil());
		}
	}
}
