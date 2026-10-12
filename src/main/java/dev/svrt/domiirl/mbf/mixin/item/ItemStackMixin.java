package dev.svrt.domiirl.mbf.mixin.item;

import dev.svrt.domiirl.mbf.utils.BannerItems;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

//? if >=1.21.5 {
import net.minecraft.world.item.component.TooltipDisplay;
//?} else {
/*import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;
*///?}

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

  @Shadow public abstract Item getItem();

  // NeoForge 26.x moves the vanilla component lines out of addDetailsToTooltip into its own method
  //? if >=1.21.5 {
  //? if neoforge && >=26.1 {
  /*@Inject(method = "addDetailsToTooltipComponents", at = @At(value = "FIELD", target = "Lnet/minecraft/core/component/DataComponents;BANNER_PATTERNS:Lnet/minecraft/core/component/DataComponentType;", shift = At.Shift.BEFORE))
  *///?} else {
  @Inject(method = "addDetailsToTooltip", at = @At(value = "FIELD", target = "Lnet/minecraft/core/component/DataComponents;BANNER_PATTERNS:Lnet/minecraft/core/component/DataComponentType;", shift = At.Shift.BEFORE))
  //?}
  private void addDetailsToTooltip(Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Player player, TooltipFlag tooltipFlag, Consumer<Component> consumer, CallbackInfo ci) {
    mbf$addTooltipLines(consumer);
  }
  //?} else {
  /*// The banner patterns are listed by BannerItem.appendHoverText before 1.21.5
  //? if >=1.20.5 {
  @ModifyArg(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;)V"), index = 2)
  //?} else {
  /^@ModifyArg(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;)V"), index = 2)
  ^///?}
  private List<Component> addHoverText(List<Component> tooltip) {
    mbf$addTooltipLines(tooltip::add);
    return tooltip;
  }
  *///?}

  @Unique
  private void mbf$addTooltipLines(Consumer<Component> consumer) {
    ItemStack self = (ItemStack) (Object) this;
    if (BannerItems.hasMaxLayers(self) || getItem() instanceof BannerItem) {
      int maxLayers = BannerItems.maxLayers(self, BannerItems.VANILLA_MAX_LAYERS);
      consumer.accept(Component.translatable("tooltip.mbf.max_banner_layers", maxLayers).withStyle(ChatFormatting.GRAY));
    }

    DyeColor baseColor = BannerItems.decorationColor(self);
    if (baseColor != null) {
      consumer.accept(Component.translatable("tooltip.mbf.banner_base_color", Component.translatable(String.format("color.minecraft.%s", baseColor.getName()))).withStyle(ChatFormatting.GRAY));
    }
  }
}
