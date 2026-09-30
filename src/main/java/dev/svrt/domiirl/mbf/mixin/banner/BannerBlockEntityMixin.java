package dev.svrt.domiirl.mbf.mixin.banner;

import dev.svrt.domiirl.mbf.accessor.HangingBanner;
import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BannerBlockEntity.class)
public class BannerBlockEntityMixin implements HangingBanner {

  @Unique
  private static final String VALUE_NAME = "mbf$max_layers";

  @Unique
  private static final String HANGING_NAME = "mbf$hanging";

  @Unique
  private int maxLayers = 6;

  @Unique
  private boolean hanging = false;

  @Override
  public boolean mbf$isHanging() {
    return this.hanging;
  }

  @Override
  public void mbf$setHanging(boolean hanging) {
    this.hanging = hanging;
  }

  @Inject(method = "saveAdditional", at = @At("TAIL"))
  private void saveAdditional(ValueOutput valueOutput, CallbackInfo ci) {
    valueOutput.putInt(VALUE_NAME, this.maxLayers);
    valueOutput.putBoolean(HANGING_NAME, this.hanging);
  }

  @Inject(method = "loadAdditional", at = @At("TAIL"))
  private void loadAdditional(ValueInput valueInput, CallbackInfo ci) {
    valueInput.getInt(VALUE_NAME).ifPresent(value -> {
      this.maxLayers = value;
    });
    this.hanging = valueInput.getBooleanOr(HANGING_NAME, false);
  }

  @Inject(method = "applyImplicitComponents", at = @At("TAIL"))
  private void applyImplicitComponents(DataComponentGetter dataComponentGetter, CallbackInfo ci) {
    this.maxLayers = dataComponentGetter.getOrDefault(ModDataComponents.MAX_BANNER_LAYERS, 6);
  }

  @Inject(method = "collectImplicitComponents", at = @At("TAIL"))
  private void collectImplicitComponents(DataComponentMap.Builder builder, CallbackInfo ci) {
    builder.set(ModDataComponents.MAX_BANNER_LAYERS, this.maxLayers);
    builder.set(ModDataComponents.BANNER_BASE_COLOR, DyeColor.WHITE);
  }

  @Inject(method = "removeComponentsFromTag", at = @At("TAIL"))
  private void removeComponentsFromTag(ValueOutput valueOutput, CallbackInfo ci) {
    valueOutput.discard(VALUE_NAME);
    valueOutput.discard(HANGING_NAME);
  }

}
