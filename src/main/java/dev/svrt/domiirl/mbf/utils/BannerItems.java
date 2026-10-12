package dev.svrt.domiirl.mbf.utils;

import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.Nullable;

//? if <1.21.4 {
/*import net.minecraft.world.level.block.BannerBlock;
*///?}
//? if >=1.20.5 {
import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import net.minecraft.core.component.DataComponents;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
*///?}

// What MBF stores on items: a banner's look, the look an item was decorated with, and the banner layer limit.
// Data components from 1.20.5, NBT in vanilla's own layout before that
public final class BannerItems {

	public static final int VANILLA_MAX_LAYERS = 6;

	//? if <1.20.5 {
	/*private static final String BLOCK_ENTITY_TAG = "BlockEntityTag";
	private static final String BASE_COLOR = "mbf$base_color";
	// The block entity keeps the same key, so the limit survives placing the banner
	private static final String MAX_LAYERS = "mbf$max_layers";
	*///?}

	private BannerItems() {
	}

	// A banner's own colour, or the one an item was decorated with
	public static DyeColor baseColor(ItemStack itemStack) {
		if (itemStack.getItem() instanceof BannerItem bannerItem) {
			return bannerItem.getColor();
		}
		DyeColor decorationColor = decorationColor(itemStack);
		return decorationColor != null ? decorationColor : DyeColor.WHITE;
	}

	public static @Nullable DyeColor decorationColor(ItemStack itemStack) {
		//? if >=1.20.5 {
		return itemStack.get(ModDataComponents.BANNER_BASE_COLOR);
		//?} else {
		/*CompoundTag tag = itemStack.getTag();
		return tag != null && tag.contains(BASE_COLOR) ? DyeColor.byId(tag.getInt(BASE_COLOR)) : null;
		*///?}
	}

	public static BannerPatternLayers patterns(ItemStack itemStack) {
		//? if >=1.20.5 {
		return itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
		//?} else {
		/*ListTag patterns = BannerBlockEntity.getItemPatterns(itemStack);
		return patterns != null ? BannerPatternLayers.fromTag(patterns) : BannerPatternLayers.EMPTY;
		*///?}
	}

	public static boolean hasPatterns(ItemStack itemStack) {
		//? if >=1.20.5 {
		return itemStack.has(DataComponents.BANNER_PATTERNS);
		//?} else {
		/*return BannerBlockEntity.getItemPatterns(itemStack) != null;
		*///?}
	}

	// An armor piece crafted together with a banner
	public static boolean isDecorated(ItemStack itemStack) {
		return decorationColor(itemStack) != null && hasPatterns(itemStack);
	}

	public static void decorate(ItemStack itemStack, DyeColor baseColor, BannerPatternLayers patterns) {
		//? if >=1.20.5 {
		itemStack.set(ModDataComponents.BANNER_BASE_COLOR, baseColor);
		itemStack.set(DataComponents.BANNER_PATTERNS, patterns);
		//?} else {
		/*itemStack.getOrCreateTag().putInt(BASE_COLOR, baseColor.getId());
		itemStack.getOrCreateTagElement(BLOCK_ENTITY_TAG).put(BannerBlockEntity.TAG_PATTERNS, patterns.toTag());
		*///?}
	}

	//? if <1.21.4 {
	/*// A banner item that looks like the given banner or decorated item
	public static ItemStack asBanner(ItemStack itemStack) {
		if (itemStack.getItem() instanceof BannerItem) {
			return itemStack;
		}
		ItemStack banner = new ItemStack(BannerBlock.byColor(baseColor(itemStack)));
		//? if >=1.20.5 {
		banner.set(DataComponents.BANNER_PATTERNS, patterns(itemStack));
		//?} else {
		/^banner.getOrCreateTagElement(BLOCK_ENTITY_TAG).put(BannerBlockEntity.TAG_PATTERNS, patterns(itemStack).toTag());
		^///?}
		return banner;
	}
	*///?}

	public static boolean hasMaxLayers(ItemStack itemStack) {
		//? if >=1.20.5 {
		return itemStack.has(ModDataComponents.MAX_BANNER_LAYERS);
		//?} else {
		/*CompoundTag tag = maxLayersTag(itemStack);
		return tag != null && tag.contains(MAX_LAYERS);
		*///?}
	}

	public static int maxLayers(ItemStack itemStack, int fallback) {
		//? if >=1.20.5 {
		return itemStack.getOrDefault(ModDataComponents.MAX_BANNER_LAYERS, fallback);
		//?} else {
		/*CompoundTag tag = maxLayersTag(itemStack);
		return tag != null && tag.contains(MAX_LAYERS) ? tag.getInt(MAX_LAYERS) : fallback;
		*///?}
	}

	public static void setMaxLayers(ItemStack itemStack, int maxLayers) {
		//? if >=1.20.5 {
		itemStack.set(ModDataComponents.MAX_BANNER_LAYERS, maxLayers);
		//?} else {
		/*(itemStack.getItem() instanceof BannerItem ? itemStack.getOrCreateTagElement(BLOCK_ENTITY_TAG) : itemStack.getOrCreateTag()).putInt(MAX_LAYERS, maxLayers);
		*///?}
	}

	//? if <1.20.5 {
	/*private static @Nullable CompoundTag maxLayersTag(ItemStack itemStack) {
		return itemStack.getItem() instanceof BannerItem ? BlockItem.getBlockEntityData(itemStack) : itemStack.getTag();
	}
	*///?}
}
