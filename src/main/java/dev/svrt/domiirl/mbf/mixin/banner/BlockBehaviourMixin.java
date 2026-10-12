package dev.svrt.domiirl.mbf.mixin.banner;

import dev.svrt.domiirl.mbf.utils.BannerItems;
import dev.svrt.domiirl.mbf.accessor.LayeredBanner;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

// The vanilla banner loot tables copy only an explicit component list, so the layer limit is carried over here
@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {

  @Inject(method = "getDrops", at = @At("RETURN"))
  private void keepMaxBannerLayers(BlockState state, LootParams.Builder params, CallbackInfoReturnable<List<ItemStack>> cir) {
    if (!(params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof BannerBlockEntity banner)) {
      return;
    }

    int maxLayers = ((LayeredBanner) banner).mbf$getMaxLayers();
    for (ItemStack drop : cir.getReturnValue()) {
      if (drop.getItem() instanceof BannerItem) {
        BannerItems.setMaxLayers(drop, maxLayers);
      }
    }
  }

}
