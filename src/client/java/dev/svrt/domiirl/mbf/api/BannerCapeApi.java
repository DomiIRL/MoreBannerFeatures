package dev.svrt.domiirl.mbf.api;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.svrt.domiirl.mbf.RendererUtils;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.component.DataComponents;
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
@Environment(EnvType.CLIENT)
public final class BannerCapeApi {

	public static final int TEXTURE_WIDTH = 34;
	public static final int TEXTURE_HEIGHT = 27;

	private static SpriteGetter sprites;

	private BannerCapeApi() {
	}

	// Draw the mesh with renderType() into what wrap() hands back
	public record Pass(RenderType renderType, TextureAtlasSprite sprite, int tint) {

		public VertexConsumer wrap(VertexConsumer consumer) {
			return new BannerVertexConsumer(consumer, this.sprite, this.tint);
		}
	}

	// Called while the player renderer is built
	public static void init(SpriteGetter spriteGetter) {
		sprites = spriteGetter;
	}

	public static boolean hasBannerCape(LivingEntity entity) {
		return entity instanceof Bannerable bannerable
			&& bannerable.mbf$isEnabled()
			&& RendererUtils.isLegitPlayerBannerEquipment(bannerable.mbf$getBannerItem());
	}

	// In draw order, empty without a banner cape
	public static List<Pass> capePasses(LivingEntity entity) {
		if (sprites == null || !hasBannerCape(entity)) {
			return List.of();
		}

		ItemStack itemStack = ((Bannerable) entity).mbf$getBannerItem();
		DyeColor dyeColor = itemStack.getItem() instanceof BannerItem bannerItem
			? bannerItem.getColor()
			: itemStack.getOrDefault(ModDataComponents.BANNER_BASE_COLOR, DyeColor.WHITE);
		BannerPatternLayers patternLayers = itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);

		List<Pass> passes = new ArrayList<>();

		// Solid cloth first, the pattern passes write no depth. No cull, a banner has no back side
		passes.add(pass(Sheets.BANNER_BASE, RenderTypes::entityCutout, -1));

		passes.add(pass(Sheets.BANNER_PATTERN_BASE, RenderTypes::entityTranslucent, tint(dyeColor)));
		for (BannerPatternLayers.Layer layer : patternLayers.layers()) {
			passes.add(pass(Sheets.getBannerSprite(layer.pattern()), RenderTypes::entityTranslucent, tint(layer.color())));
		}

		return passes;
	}

	private static Pass pass(SpriteId spriteId, Function<Identifier, RenderType> renderType, int tint) {
		return new Pass(spriteId.renderType(renderType), sprites.get(spriteId), tint);
	}

	private static int tint(DyeColor dyeColor) {
		return ARGB.opaque(dyeColor.getTextureDiffuseColor());
	}
}
