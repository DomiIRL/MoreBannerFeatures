package dev.svrt.domiirl.mbf.mixin.fox;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.FoxHeldItemLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.FoxRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoxHeldItemLayer.class)
public abstract class FoxHeldItemLayerMixin extends RenderLayer<FoxRenderState, FoxModel> {

	public FoxHeldItemLayerMixin(RenderLayerParent<FoxRenderState, FoxModel> renderLayerParent) {
		super(renderLayerParent);
	}

	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/FoxRenderState;FF)V", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V"))
	private void renderItem(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, FoxRenderState foxRenderState, float f, float g, CallbackInfo ci) {
		if (MBFOptions.FOX_CORRECTION.getBooleanValue()) {
			poseStack.mulPose(Axis.ZP.rotationDegrees(180));
		}
	}

}
