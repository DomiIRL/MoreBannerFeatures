package dev.svrt.domiirl.mbf.mixin.common;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WingsLayer.class)
public abstract class WingsLayerMixin<S extends HumanoidRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {

  public WingsLayerMixin(RenderLayerParent<S, M> renderLayerParent) {
    super(renderLayerParent);
  }

  @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V", at = @At("HEAD"), cancellable = true)
  private void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, S humanoidRenderState, float f, float g, CallbackInfo ci) {
    if (!MBFOptions.ELYTRA_CAPES.getBooleanValue()) {
      return;
    }
    if (humanoidRenderState instanceof Bannerable bannerable) {
      ItemStack itemStack = bannerable.mbf$getBannerItem();
      if (itemStack.has(ModDataComponents.BANNER_BASE_COLOR)) {
        ci.cancel();
      }
    }
  }

}
