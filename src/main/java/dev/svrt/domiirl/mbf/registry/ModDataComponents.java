package dev.svrt.domiirl.mbf.registry;

import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.DyeColor;

public class ModDataComponents {

  // Built before registration because Forge fires the item RegisterEvent first and the banner thread item already needs them
  public static final DataComponentType<Integer> MAX_BANNER_LAYERS = DataComponentType.<Integer>builder().persistent(ExtraCodecs.intRange(1, 99)).networkSynchronized(ByteBufCodecs.VAR_INT).build();
  public static final DataComponentType<DyeColor> BANNER_BASE_COLOR = DataComponentType.<DyeColor>builder().persistent(DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC).build();

  public static void init() {
    register("max_banner_layers", MAX_BANNER_LAYERS);
    register("base_color", BANNER_BASE_COLOR);
  }

  private static void register(String id, DataComponentType<?> type) {
    Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(MoreBannerFeatures.MOD_ID, id), type);
  }

}
