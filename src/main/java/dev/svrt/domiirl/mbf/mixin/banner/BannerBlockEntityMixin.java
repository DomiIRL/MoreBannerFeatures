package dev.svrt.domiirl.mbf.mixin.banner;

import dev.svrt.domiirl.mbf.accessor.HangingBanner;
import dev.svrt.domiirl.mbf.accessor.LayeredBanner;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.20.5 {
import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.DyeColor;
//?}
//? if >=1.21.5 {
import net.minecraft.core.component.DataComponentGetter;
//?}
//? if >=1.21.6 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}
//? if <1.21.6 && >=1.20.5 {
/*import net.minecraft.core.HolderLookup;
*///?}

@Mixin(BannerBlockEntity.class)
public abstract class BannerBlockEntityMixin extends BlockEntity implements HangingBanner, LayeredBanner {

  @Unique
  private static final String VALUE_NAME = "mbf$max_layers";

  @Unique
  private static final String HANGING_NAME = "mbf$hanging";

  @Unique
  private int maxLayers = 6;

  @Unique
  private boolean hanging = false;

  @Unique
  private boolean hangingKnown = false;

  protected BannerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  @Override
  public boolean mbf$isHanging() {
    // Banners hung before 2.3 kept this in the blockstate, so work it out once and keep it
    if (!this.hangingKnown) {
      BlockEntity blockEntity = (BlockEntity) (Object) this;
      Level level = blockEntity.getLevel();
      if (level == null) {
        return false;
      }

      // A standing banner reaches into the block above, so nobody puts a solid one there
      BlockPos pos = blockEntity.getBlockPos();
      this.hanging = level.getBlockState(pos.above()).isSolid();
      this.hangingKnown = true;
      blockEntity.setChanged();
    }

    return this.hanging;
  }

  @Override
  public void mbf$setHanging(boolean hanging) {
    this.hanging = hanging;
    this.hangingKnown = true;
  }

  //? if >=1.21.6 {
  @Inject(method = "saveAdditional", at = @At("TAIL"))
  private void saveAdditional(ValueOutput valueOutput, CallbackInfo ci) {
    valueOutput.putInt(VALUE_NAME, this.maxLayers);
    // An unknown value would reach the client as a known one and stop it deriving
    if (this.hangingKnown) {
      valueOutput.putBoolean(HANGING_NAME, this.hanging);
    }
  }

  @Inject(method = "loadAdditional", at = @At("TAIL"))
  private void loadAdditional(ValueInput valueInput, CallbackInfo ci) {
    valueInput.getInt(VALUE_NAME).ifPresent(value -> {
      this.maxLayers = value;
    });
    // Reading with both defaults tells us whether the key is there at all
    this.hangingKnown = valueInput.getBooleanOr(HANGING_NAME, false) == valueInput.getBooleanOr(HANGING_NAME, true);
    this.hanging = valueInput.getBooleanOr(HANGING_NAME, false);
  }
  //?} else {
  /*@Inject(method = "saveAdditional", at = @At("TAIL"))
  //? if >=1.20.5 {
  private void saveAdditional(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
  //?} else {
  /^private void saveAdditional(CompoundTag tag, CallbackInfo ci) {
  ^///?}
    tag.putInt(VALUE_NAME, this.maxLayers);
    // An unknown value would reach the client as a known one and stop it deriving
    if (this.hangingKnown) {
      tag.putBoolean(HANGING_NAME, this.hanging);
    }
  }

  //? if >=1.20.5 {
  @Inject(method = "loadAdditional", at = @At("TAIL"))
  private void loadAdditional(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
  //?} else {
  /^// Block entities loaded through load(CompoundTag) before 1.20.5
  @Inject(method = "load", at = @At("TAIL"))
  private void loadAdditional(CompoundTag tag, CallbackInfo ci) {
  ^///?}
    //? if >=1.21.5 {
    tag.getInt(VALUE_NAME).ifPresent(value -> {
      this.maxLayers = value;
    });
    this.hanging = tag.getBooleanOr(HANGING_NAME, false);
    //?} else {
    /^if (tag.contains(VALUE_NAME)) {
      this.maxLayers = tag.getInt(VALUE_NAME);
    }
    this.hanging = tag.getBoolean(HANGING_NAME);
    ^///?}
    this.hangingKnown = tag.contains(HANGING_NAME);
  }
  *///?}

  @Override
  public int mbf$getMaxLayers() {
    return this.maxLayers;
  }

  // Before 1.20.5 the limit reaches the item through the block entity tag instead, see BannerItems
  //? if >=1.20.5 {
  @Inject(method = "applyImplicitComponents", at = @At("TAIL"))
  //? if >=1.21.5 {
  private void applyImplicitComponents(DataComponentGetter dataComponentGetter, CallbackInfo ci) {
  //?} else {
  /*private void applyImplicitComponents(DataComponentInput dataComponentGetter, CallbackInfo ci) {
  *///?}
    this.maxLayers = dataComponentGetter.getOrDefault(ModDataComponents.MAX_BANNER_LAYERS, 6);
  }

  @Inject(method = "collectImplicitComponents", at = @At("TAIL"))
  private void collectImplicitComponents(DataComponentMap.Builder builder, CallbackInfo ci) {
    builder.set(ModDataComponents.MAX_BANNER_LAYERS, this.maxLayers);
    builder.set(ModDataComponents.BANNER_BASE_COLOR, DyeColor.WHITE);
  }

  @Inject(method = "removeComponentsFromTag", at = @At("TAIL"))
  //? if >=1.21.6 {
  private void removeComponentsFromTag(ValueOutput valueOutput, CallbackInfo ci) {
    valueOutput.discard(VALUE_NAME);
    valueOutput.discard(HANGING_NAME);
  }
  //?} else {
  /*private void removeComponentsFromTag(CompoundTag tag, CallbackInfo ci) {
    tag.remove(VALUE_NAME);
    tag.remove(HANGING_NAME);
  }
  *///?}
  //?}

}
