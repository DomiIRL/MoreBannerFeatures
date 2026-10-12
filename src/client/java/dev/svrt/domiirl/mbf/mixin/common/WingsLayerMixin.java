package dev.svrt.domiirl.mbf.mixin.common;

import dev.svrt.domiirl.mbf.utils.BannerItems;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21.2 {
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
//?} else {
/*import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.world.entity.LivingEntity;
*///?}

//? if >=1.21.2 {
@Mixin(WingsLayer.class)
public abstract class WingsLayerMixin<S extends HumanoidRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
//?} else {
/*@Mixin(ElytraLayer.class)
public abstract class WingsLayerMixin<S extends LivingEntity, M extends EntityModel<S>> extends RenderLayer<S, M> {
*///?}

  public WingsLayerMixin(RenderLayerParent<S, M> renderLayerParent) {
    super(renderLayerParent);
  }

  //? if >=1.21.2 {
  @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V", at = @At("HEAD"), cancellable = true)
  private void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, S humanoidRenderState, float f, float g, CallbackInfo ci) {
  //?} else {
  /*@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
  private void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, S humanoidRenderState, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
  *///?}
    if (!MBFOptions.ELYTRA_CAPES.getBooleanValue()) {
      return;
    }
    if (humanoidRenderState instanceof Bannerable bannerable) {
      ItemStack itemStack = bannerable.mbf$getBannerItem();
      if (BannerItems.decorationColor(itemStack) != null) {
        ci.cancel();
      }
    }
  }

}
