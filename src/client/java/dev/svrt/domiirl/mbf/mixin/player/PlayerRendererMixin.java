package dev.svrt.domiirl.mbf.mixin.player;

import dev.svrt.domiirl.mbf.layer.BannerCapeFeatureRenderer;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21.2 {
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
//?}

@Mixin(AvatarRenderer.class)
//? if >=1.21.2 {
public abstract class PlayerRendererMixin extends LivingEntityRenderer<Avatar, AvatarRenderState, PlayerModel> {
//?} else {
/*public abstract class PlayerRendererMixin extends LivingEntityRenderer<Avatar, PlayerModel<Avatar>> {
*///?}

	//? if >=1.21.2 {
	public PlayerRendererMixin(Context context, PlayerModel entityModel, float f) {
	//?} else {
	/*public PlayerRendererMixin(Context context, PlayerModel<Avatar> entityModel, float f) {
	*///?}
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context ctx, boolean slim, CallbackInfo ci) {
		//? if >=1.21.2 {
		addLayer(new BannerCapeFeatureRenderer(this, ctx.getEquipmentAssets()));
		//?} else {
		/*addLayer(new BannerCapeFeatureRenderer(this));
		*///?}
	}

	//? if >=1.21.2 {
	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("HEAD"))
	private void extractRenderState(Avatar avatar, AvatarRenderState renderState, float f, CallbackInfo ci) {
		if (renderState instanceof Bannerable bannerRenderState && avatar instanceof Bannerable bannerable) {
			bannerRenderState.mbf$setBannerItem(bannerable.mbf$getBannerItem());
		}
	}
	//?}
}
