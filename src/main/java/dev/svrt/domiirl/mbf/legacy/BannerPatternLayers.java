package dev.svrt.domiirl.mbf.legacy;

//? if <1.20.5 {
/*import com.mojang.datafixers.util.Pair;
//? if >=1.19 {
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BannerPatterns;
//?}
//? if >=1.19.3 {
import net.minecraft.core.registries.BuiltInRegistries;
//?} else {
/^import net.minecraft.core.Registry;
^///?}
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.ArrayList;
import java.util.List;

// 1.20.5's BannerPatternLayers, for the versions that kept patterns as an NBT list and rendered them as pairs
public record BannerPatternLayers(List<Layer> layers) {

	public static final BannerPatternLayers EMPTY = new BannerPatternLayers(List.of());

	//? if >=1.19 {
	public record Layer(Holder<BannerPattern> pattern, DyeColor color) {
	//?} else {
	/^// Banner patterns were an enum before 1.19
	public record Layer(BannerPattern pattern, DyeColor color) {
	^///?}
	}

	public static BannerPatternLayers fromTag(ListTag tag) {
		List<Layer> layers = new ArrayList<>();
		for (Tag entry : tag) {
			if (entry instanceof CompoundTag layer) {
				//? if >=1.19 {
				Holder<BannerPattern> pattern = BannerPattern.byHash(layer.getString("Pattern"));
				//?} else {
				/^BannerPattern pattern = BannerPattern.byHash(layer.getString("Pattern"));
				^///?}
				if (pattern != null) {
					layers.add(new Layer(pattern, DyeColor.byId(layer.getInt("Color"))));
				}
			}
		}
		return new BannerPatternLayers(layers);
	}

	// What BannerBlockEntity.getPatterns() hands out: the base colour first, then the layers
	//? if >=1.19 {
	public static BannerPatternLayers fromRenderList(List<Pair<Holder<BannerPattern>, DyeColor>> patterns) {
	//?} else {
	/^public static BannerPatternLayers fromRenderList(List<Pair<BannerPattern, DyeColor>> patterns) {
	^///?}
		List<Layer> layers = new ArrayList<>();
		for (int i = 1; i < patterns.size(); i++) {
			layers.add(new Layer(patterns.get(i).getFirst(), patterns.get(i).getSecond()));
		}
		return new BannerPatternLayers(layers);
	}

	public ListTag toTag() {
		ListTag tag = new ListTag();
		for (Layer layer : this.layers) {
			CompoundTag entry = new CompoundTag();
			//? if >=1.19 {
			entry.putString("Pattern", layer.pattern().value().getHashname());
			//?} else {
			/^entry.putString("Pattern", layer.pattern().getHashname());
			^///?}
			entry.putInt("Color", layer.color().getId());
			tag.add(entry);
		}
		return tag;
	}

	//? if >=1.19 {
	public List<Pair<Holder<BannerPattern>, DyeColor>> toRenderList(DyeColor baseColor) {
		List<Pair<Holder<BannerPattern>, DyeColor>> patterns = new ArrayList<>();
		patterns.add(Pair.of(BuiltInRegistries.BANNER_PATTERN.getHolderOrThrow(BannerPatterns.BASE), baseColor));
	//?} else {
	/^public List<Pair<BannerPattern, DyeColor>> toRenderList(DyeColor baseColor) {
		List<Pair<BannerPattern, DyeColor>> patterns = new ArrayList<>();
		patterns.add(Pair.of(BannerPattern.BASE, baseColor));
	^///?}
		for (Layer layer : this.layers) {
			patterns.add(Pair.of(layer.pattern(), layer.color()));
		}
		return patterns;
	}
}
*///?}
