package dev.svrt.domiirl.mbf.mixin.ghast;

import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.layer.HappyGhastBannerFeatureRenderer;
import net.minecraft.client.model.animal.ghast.HappyGhastModel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HappyGhastRenderer;
import net.minecraft.client.renderer.entity.state.HappyGhastRenderState;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HappyGhastRenderer.class)
public abstract class HappyGhastRendererMixin extends AgeableMobRenderer<HappyGhast, HappyGhastRenderState, HappyGhastModel> {

  public HappyGhastRendererMixin(EntityRendererProvider.Context context, HappyGhastModel entityModel, HappyGhastModel entityModel2, float f) {
    super(context, entityModel, entityModel2, f);
  }

  @Inject(method = "<init>", at = @At("TAIL"))
  private void init(EntityRendererProvider.Context context, CallbackInfo ci) {
    this.addLayer(new HappyGhastBannerFeatureRenderer(this, context.getMaterials()));
  }

  @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/HappyGhast;Lnet/minecraft/client/renderer/entity/state/HappyGhastRenderState;F)V", at = @At("HEAD"))
  private void extractRenderState(HappyGhast happyGhast, HappyGhastRenderState renderState, float f, CallbackInfo ci) {
    if (renderState instanceof Bannerable bannerRenderState && happyGhast instanceof Bannerable bannerable) {
      bannerRenderState.mbf$setBannerItem(bannerable.mbf$getBannerItem());
    }
  }

}
