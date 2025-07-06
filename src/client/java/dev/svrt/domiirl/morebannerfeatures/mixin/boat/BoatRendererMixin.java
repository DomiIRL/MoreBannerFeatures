package dev.svrt.domiirl.morebannerfeatures.mixin.boat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.svrt.domiirl.morebannerfeatures.core.accessor.Bannerable;
import dev.svrt.domiirl.morebannerfeatures.errors.ErrorSystemManager;
import dev.svrt.domiirl.morebannerfeatures.RendererUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BannerFlagModel;
import net.minecraft.client.model.BannerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.AbstractBoatRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
@Mixin(AbstractBoatRenderer.class)
public abstract class BoatRendererMixin extends EntityRenderer<AbstractBoat, BoatRenderState> {

	private final BannerModel standingModel = new BannerModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER));
	private final BannerFlagModel standingFlagModel = new BannerFlagModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER_FLAG));

	public BoatRendererMixin(EntityRendererProvider.Context context) {
		super(context);
	}

	@Inject(method = "renderTypeAdditions", at = @At(value = "TAIL"))
	private void render(BoatRenderState entity, PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
		matrices.pushPose();
		try {
			if (entity instanceof Bannerable bannerable) {
				ItemStack itemStack = bannerable.getBannerItem();
				if (itemStack == null || !(itemStack.getItem() instanceof BannerItem)) return;
				matrices.mulPose(Axis.YP.rotationDegrees(180));

				matrices.translate(0, 1.05, -0.937);

				matrices.scale(0.6666667F, -0.6666667F, -0.6666667F);

				VertexConsumer vertexConsumer = ModelBakery.BANNER_BASE.buffer(vertexConsumers, RenderType::entityNoOutline);
//				this.pillar.render(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
//				this.crossbar.render(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY);

//				RendererUtils.modifyMatricesBannerSwing(banner, entity, true);

//				RendererUtils.renderCanvasFromItem(itemStack, matrices, vertexConsumers, light, OverlayTexture.NO_OVERLAY, banner);

			}
		} catch (Exception exception) {
			ErrorSystemManager.reportException();
			exception.printStackTrace();
		}
		matrices.popPose();
	}

}
