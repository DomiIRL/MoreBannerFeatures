package dev.svrt.domiirl.mbf.mixin.common;

import dev.svrt.domiirl.mbf.utils.BannerItems;
//? if >=1.21.4 {
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
//? if >=1.21.9 {
import net.minecraft.world.entity.ItemOwner;
//?}
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumMap;
import java.util.Map;

//? if <26.1 {
/*import org.spongepowered.asm.mixin.Final;

import java.util.function.Function;
*///?}

@Mixin(ItemModelResolver.class)
public abstract class ItemModelResolverMixin {

  //? if >=26.1 {
  @Shadow protected abstract ClientItem.Properties getItemProperties(Identifier modelId);
  @Shadow protected abstract ItemModel getItemModel(Identifier modelId);
  //?} else {
  /*@Shadow @Final private Function<Identifier, ClientItem.Properties> clientProperties;
  @Shadow @Final private Function<Identifier, ItemModel> modelGetter;

  @Unique
  private ClientItem.Properties getItemProperties(Identifier modelId) {
    return this.clientProperties.apply(modelId);
  }

  @Unique
  private ItemModel getItemModel(Identifier modelId) {
    return this.modelGetter.apply(modelId);
  }
  *///?}

  @Unique
  private static volatile Map<DyeColor, Identifier> bannerModelByColor;

  // Built on first render, a static initializer throws Components not bound yet
  @Unique
  private static Identifier mbf$bannerModel(DyeColor color) {
    Map<DyeColor, Identifier> byColor = bannerModelByColor;
    if (byColor == null) {
      byColor = new EnumMap<>(DyeColor.class);
      for (Item item : BuiltInRegistries.ITEM) {
        if (item instanceof BannerItem bannerItem) {
          Identifier model = bannerItem.getDefaultInstance().get(DataComponents.ITEM_MODEL);
          if (model != null) {
            byColor.put(bannerItem.getColor(), model);
          }
        }
      }
      bannerModelByColor = byColor;
    }
    return byColor.get(color);
  }

  // Use @Inject because I don't trust that no one else would redirect at this point here
  @Inject(method = "appendItemLayers", at = @At("HEAD"), cancellable = true)
  //? if >=1.21.9 {
  private void appendItemLayers(ItemStackRenderState itemStackRenderState, ItemStack itemStack, ItemDisplayContext itemDisplayContext, Level level, ItemOwner itemOwner, int i, CallbackInfo ci) {
  //?} else {
  /*private void appendItemLayers(ItemStackRenderState itemStackRenderState, ItemStack itemStack, ItemDisplayContext itemDisplayContext, Level level, LivingEntity itemOwner, int i, CallbackInfo ci) {
  *///?}
    if (itemDisplayContext == ItemDisplayContext.HEAD
      && (itemStack.getItem() instanceof BannerItem || BannerItems.isDecorated(itemStack))) {
      DyeColor dyeColor = BannerItems.baseColor(itemStack);
      Identifier resourceLocation = mbf$bannerModel(dyeColor);

      if (resourceLocation != null) {
        //? if >=1.21.6 {
        itemStackRenderState.setOversizedInGui(this.getItemProperties(resourceLocation).oversizedInGui());
        //?}
        this.getItemModel(resourceLocation)
          .update(itemStackRenderState, itemStack, (ItemModelResolver) (Object) this, itemDisplayContext, level instanceof ClientLevel clientLevel ? clientLevel : null, itemOwner, i);
        ci.cancel();
      }
    }
  }
}
//?}
