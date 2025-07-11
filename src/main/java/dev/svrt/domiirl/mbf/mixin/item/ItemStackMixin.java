package dev.svrt.domiirl.mbf.mixin.item;

import dev.svrt.domiirl.mbf.config.MBFOptions;
import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder, FabricItemStack {

  @Shadow public abstract Item getItem();

  @Inject(method = "addDetailsToTooltip", at = @At(value = "FIELD", target = "Lnet/minecraft/core/component/DataComponents;BANNER_PATTERNS:Lnet/minecraft/core/component/DataComponentType;", shift = At.Shift.BEFORE))
  private void addDetailsToTooltip(Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Player player, TooltipFlag tooltipFlag, Consumer<Component> consumer, CallbackInfo ci) {

    if (has(ModDataComponents.MAX_BANNER_LAYERS) || getItem() instanceof BannerItem) {
      int maxLayers = getOrDefault(ModDataComponents.MAX_BANNER_LAYERS, 6);
      consumer.accept(Component.translatable("tooltip.mbf.max_banner_layers", maxLayers).withStyle(ChatFormatting.GRAY));
    }

    if (has(ModDataComponents.BANNER_BASE_COLOR)) {
      var baseColor = get(ModDataComponents.BANNER_BASE_COLOR);
      if (baseColor != null) {
        consumer.accept(Component.translatable("tooltip.mbf.banner_base_color", Component.translatable(String.format("color.minecraft.%s", baseColor.getName()))).withStyle(ChatFormatting.GRAY));
      }
    }
  }
}
