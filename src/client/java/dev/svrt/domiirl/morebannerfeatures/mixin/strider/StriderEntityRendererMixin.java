package dev.svrt.domiirl.morebannerfeatures.mixin.strider;

import dev.svrt.domiirl.morebannerfeatures.accessor.BannerRenderState;
import dev.svrt.domiirl.morebannerfeatures.core.accessor.Bannerable;
import dev.svrt.domiirl.morebannerfeatures.renderer.StriderBannerFeatureRenderer;
import net.minecraft.client.model.StriderModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.StriderRenderer;
import net.minecraft.client.renderer.entity.state.StriderRenderState;
import net.minecraft.world.entity.monster.Strider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
@Mixin(StriderRenderer.class)
public abstract class StriderEntityRendererMixin extends MobRenderer<Strider, StriderRenderState, StriderModel> {

	public StriderEntityRendererMixin(Context context, StriderModel entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context context, CallbackInfo ci) {
		addLayer(new StriderBannerFeatureRenderer(this));
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/monster/Strider;Lnet/minecraft/client/renderer/entity/state/StriderRenderState;F)V", at = @At("HEAD"))
	public void extractRenderState(Strider strider, StriderRenderState renderState, float f, CallbackInfo ci) {
		if (renderState instanceof BannerRenderState bannerRenderState && strider instanceof Bannerable bannerable) {
			bannerRenderState.setBannerItem(bannerable.getBannerItem());
		}
	}

}
