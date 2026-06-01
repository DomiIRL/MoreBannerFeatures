package dev.svrt.domiirl.mbf.mixin.strider;

import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.layer.StriderBannerFeatureRenderer;
import net.minecraft.client.model.monster.strider.StriderModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.StriderRenderer;
import net.minecraft.client.renderer.entity.state.StriderRenderState;
import net.minecraft.world.entity.monster.Strider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StriderRenderer.class)
public abstract class StriderEntityRendererMixin extends MobRenderer<Strider, StriderRenderState, StriderModel> {

	public StriderEntityRendererMixin(Context context, StriderModel entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context context, CallbackInfo ci) {
		addLayer(new StriderBannerFeatureRenderer(this, context.getMaterials()));
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/monster/Strider;Lnet/minecraft/client/renderer/entity/state/StriderRenderState;F)V", at = @At("HEAD"))
	private void extractRenderState(Strider strider, StriderRenderState renderState, float f, CallbackInfo ci) {
		if (renderState instanceof Bannerable bannerRenderState && strider instanceof Bannerable bannerable) {
			bannerRenderState.mbf$setBannerItem(bannerable.mbf$getBannerItem());
		}
	}

}
