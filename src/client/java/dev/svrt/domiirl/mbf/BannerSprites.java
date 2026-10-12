package dev.svrt.domiirl.mbf;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BannerPattern;

//? if >=1.21.9 {
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
//?}
//? if <26.1 {
/*import net.minecraft.client.resources.model.ModelBakery;
*///?}

// Below 26.1 the cloth sprite lives in ModelBakery and Sheets.BANNER_BASE is the dyed pattern base instead
public final class BannerSprites {

	private BannerSprites() {
	}

	//? if >=1.21.9 {
	// The same atlas EntityRendererProvider.Context.getSprites() hands out
	public static SpriteGetter atlas() {
		return Minecraft.getInstance().getAtlasManager();
	}
	//?}

	public static TextureAtlasSprite sprite(SpriteId spriteId) {
		//? if >=1.21.9 {
		return atlas().get(spriteId);
		//?} else {
		/*return spriteId.sprite();
		*///?}
	}

	public static SpriteId cloth() {
		//? if >=26.1 {
		return Sheets.BANNER_BASE;
		//?} else {
		/*return ModelBakery.BANNER_BASE;
		*///?}
	}

	public static SpriteId patternBase() {
		//? if >=26.1 {
		return Sheets.BANNER_PATTERN_BASE;
		//?} else {
		/*return Sheets.BANNER_BASE;
		*///?}
	}

	public static SpriteId pattern(Holder<BannerPattern> pattern) {
		//? if >=26.1 {
		return Sheets.getBannerSprite(pattern);
		//?} else {
		/*return Sheets.getBannerMaterial(pattern);
		*///?}
	}

}
