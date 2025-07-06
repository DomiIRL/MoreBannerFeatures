package dev.svrt.domiirl.morebannerfeatures.mixin.player;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.svrt.domiirl.morebannerfeatures.core.accessor.Bannerable;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0.4
 */
@Mixin(CapeLayer.class)
public abstract class CapeLayerMixin extends RenderLayer<PlayerRenderState, PlayerModel> {

	public CapeLayerMixin(RenderLayerParent<PlayerRenderState, PlayerModel> context) {
		super(context);
	}

	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/PlayerRenderState;FF)V", at = @At(value = "HEAD"), cancellable = true)
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, PlayerRenderState playerRenderState, float f, float g, CallbackInfo ci) {
		if (hasCustomBanner(playerRenderState)) {
			ci.cancel();
		}
	}

	public boolean hasCustomBanner(PlayerRenderState playerEntity) {
		return playerEntity instanceof Bannerable && !((Bannerable) playerEntity).getBannerItem().isEmpty();
	}

}
