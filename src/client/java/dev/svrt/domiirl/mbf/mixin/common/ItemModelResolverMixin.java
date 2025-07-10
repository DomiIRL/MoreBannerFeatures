package dev.svrt.domiirl.mbf.mixin.common;

import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Mixin(ItemModelResolver.class)
public class ItemModelResolverMixin {

  @Shadow @Final private Function<ResourceLocation, ClientItem.Properties> clientProperties;
  @Shadow @Final private Function<ResourceLocation, ItemModel> modelGetter;
  private static final Map<DyeColor, ResourceLocation> bannerModelByColor = new HashMap<>();

  static  {
    BuiltInRegistries.ITEM.stream().filter(item -> item instanceof BannerItem).map(item -> ((BannerItem) item))
      .forEach(bannerItem -> {
        DyeColor color = bannerItem.getColor();
        ResourceLocation resourceLocation = bannerItem.getDefaultInstance().get(DataComponents.ITEM_MODEL);
        bannerModelByColor.put(color, resourceLocation);
      });
  }

  // Use @Inject because I don't trust that no one else would redirect at this point here
  @Inject(method = "appendItemLayers", at = @At("HEAD"), cancellable = true)
  private void appendItemLayers(ItemStackRenderState itemStackRenderState, ItemStack itemStack, ItemDisplayContext itemDisplayContext, @Nullable Level level, @Nullable LivingEntity livingEntity, int i, CallbackInfo ci) {
    if (itemDisplayContext == ItemDisplayContext.HEAD
      && itemStack.has(DataComponents.BANNER_PATTERNS)
      && (itemStack.getItem() instanceof BannerItem || itemStack.has(ModDataComponents.BANNER_BASE_COLOR))) {
      DyeColor dyeColor = itemStack.getItem() instanceof BannerItem bannerItem ? bannerItem.getColor() : itemStack.get(ModDataComponents.BANNER_BASE_COLOR);
      ResourceLocation resourceLocation = bannerModelByColor.get(dyeColor);

      if (resourceLocation != null) {
        itemStackRenderState.setOversizedInGui(this.clientProperties.apply(resourceLocation).oversizedInGui());
        this.modelGetter.apply(resourceLocation)
          .update(itemStackRenderState, itemStack, (ItemModelResolver) (Object) this, itemDisplayContext, level instanceof ClientLevel clientLevel ? clientLevel : null, livingEntity, i);
        ci.cancel();
      }
    }
  }
}
