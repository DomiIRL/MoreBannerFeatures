package dev.svrt.domiirl.mbf.registry;

import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.DyeColor;

import java.util.function.UnaryOperator;

public class ModDataComponents {

  public static DataComponentType<Integer> MAX_BANNER_LAYERS;
  public static DataComponentType<DyeColor> BANNER_BASE_COLOR;

  public static void init() {
    MAX_BANNER_LAYERS = register("max_banner_layers", (builder) -> builder.persistent(ExtraCodecs.intRange(1, 99)).networkSynchronized(ByteBufCodecs.VAR_INT));
    BANNER_BASE_COLOR = register("base_color", (builder) -> builder.persistent(DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
  }

  private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> unaryOperator) {
    return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(MoreBannerFeatures.MOD_ID, id), unaryOperator.apply(DataComponentType.builder()).build());
  }

}
