package dev.svrt.domiirl.mbf.api;

import dev.svrt.domiirl.mbf.utils.BannerItems;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.svrt.domiirl.mbf.BannerSprites;
import dev.svrt.domiirl.mbf.RendererUtils;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import net.minecraft.client.renderer.rendertype.RenderType;
//? if >=1.21.11 {
import net.minecraft.client.renderer.rendertype.RenderTypes;
//?}
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

// Lets another mod draw the banner cape on its own mesh
public final class BannerCapeApi {

	public static final int TEXTURE_WIDTH = 34;
	public static final int TEXTURE_HEIGHT = 27;

	private BannerCapeApi() {
	}

	// Draw the mesh with renderType() into what wrap() hands back
	public record Pass(RenderType renderType, TextureAtlasSprite sprite, int tint) {

		public VertexConsumer wrap(VertexConsumer consumer) {
			return new BannerVertexConsumer(consumer, this.sprite, this.tint);
		}
	}

	public static boolean hasBannerCape(LivingEntity entity) {
		return entity instanceof Bannerable bannerable
			&& bannerable.mbf$isEnabled()
			&& RendererUtils.isLegitPlayerBannerEquipment(bannerable.mbf$getBannerItem());
	}

	// In draw order, empty without a banner cape
	public static List<Pass> capePasses(LivingEntity entity) {
		if (!hasBannerCape(entity)) {
			return List.of();
		}

		ItemStack itemStack = ((Bannerable) entity).mbf$getBannerItem();
		DyeColor dyeColor = BannerItems.baseColor(itemStack);
		BannerPatternLayers patternLayers = BannerItems.patterns(itemStack);

		List<Pass> passes = new ArrayList<>();

		// Solid cloth first, the pattern passes write no depth. No cull, a banner has no back side. Not an armor
		// render type: those sit slightly nearer the camera and the patterns then fail the depth test behind it
		//? if >=26.1 {
		passes.add(pass(BannerSprites.cloth(), RenderTypes::entityCutout, -1));
		//?} else {
		/*passes.add(pass(BannerSprites.cloth(), RenderTypes::entityCutoutNoCull, -1));
		*///?}

		passes.add(pass(BannerSprites.patternBase(), RenderTypes::entityTranslucent, tint(dyeColor)));
		for (BannerPatternLayers.Layer layer : patternLayers.layers()) {
			passes.add(pass(BannerSprites.pattern(layer.pattern()), RenderTypes::entityTranslucent, tint(layer.color())));
		}

		return passes;
	}

	private static Pass pass(SpriteId spriteId, Function<Identifier, RenderType> renderType, int tint) {
		return new Pass(spriteId.renderType(renderType), BannerSprites.sprite(spriteId), tint);
	}

	private static int tint(DyeColor dyeColor) {
		//? if >=1.21 {
		return ARGB.opaque(dyeColor.getTextureDiffuseColor());
		//?} else {
		/*float[] color = dyeColor.getTextureDiffuseColors();
		return ARGB.color(255, (int) (color[0] * 255.0F), (int) (color[1] * 255.0F), (int) (color[2] * 255.0F));
		*///?}
	}
}
