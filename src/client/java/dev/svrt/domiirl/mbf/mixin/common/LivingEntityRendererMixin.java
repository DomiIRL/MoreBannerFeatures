package dev.svrt.domiirl.mbf.mixin.common;

import dev.svrt.domiirl.mbf.utils.BannerItems;
//? if >=1.21.4 {
import dev.svrt.domiirl.mbf.utils.MBFUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>>
  extends EntityRenderer<T, S>
  implements RenderLayerParent<S, M> {

  @Shadow @Final protected ItemModelResolver itemModelResolver;

  protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
    super(context);
  }

  @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
  private void extractRenderState(T livingEntity, S state, float f, CallbackInfo ci) {
    ItemStack headItem = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
    if (headItem.getItem() instanceof BannerItem) {
      return; // Banner Items are already handled by Vanilla
    }

    ItemStack itemStack = MBFUtils.getHeadItem(livingEntity);
    if (itemStack.getItem() instanceof BannerItem || BannerItems.isDecorated(itemStack)) {
      //? if >=1.21.5 {
      this.itemModelResolver.updateForLiving(state.headItem, itemStack, ItemDisplayContext.HEAD, livingEntity);
      //?} else {
      /*this.itemModelResolver.updateForLiving(state.headItem, itemStack, ItemDisplayContext.HEAD, false, livingEntity);
      *///?}
    }
  }

}
//?}
