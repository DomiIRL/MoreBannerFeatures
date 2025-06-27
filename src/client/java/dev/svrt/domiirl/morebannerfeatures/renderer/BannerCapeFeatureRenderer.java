package dev.svrt.domiirl.morebannerfeatures.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.svrt.domiirl.morebannerfeatures.core.accessor.Bannerable;
import dev.svrt.domiirl.morebannerfeatures.core.config.MBFOptions;
import dev.svrt.domiirl.morebannerfeatures.errors.ErrorSystemManager;
import dev.svrt.domiirl.morebannerfeatures.RendererUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerCapeModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelPart.Cube;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class BannerCapeFeatureRenderer extends RenderLayer<PlayerRenderState, PlayerModel> {

	private final HumanoidModel<PlayerRenderState> model;
	private final ModelPart cloak;
	private final ModelPart pole;
	private final ModelPart bar;

	private final EquipmentAssetManager equipmentAssets;
	private final ModelPart modelPart;

	public BannerCapeFeatureRenderer(RenderLayerParent<PlayerRenderState, PlayerModel> featureRendererContext, EntityModelSet entityModelSet, EquipmentAssetManager equipmentAssetManager) {
		super(featureRendererContext);

		this.equipmentAssets = equipmentAssetManager;
		this.modelPart = entityModelSet.bakeLayer(ModelLayers.WALL_BANNER);
		this.model = new PlayerCapeModel(entityModelSet.bakeLayer(ModelLayers.PLAYER_CAPE));
		this.pole = modelPart.getChild("pole");
		this.bar = modelPart.getChild("bar");

		// OWN CLOAK WITH CUSTOM TEXTURE SIZE TO FIT THE BANNER TEXTURE
		CubeListBuilder modelPartBuilder = new CubeListBuilder();
		modelPartBuilder.texOffs(0, 0).addBox(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F);
		List<Cube> cuboids = modelPartBuilder.getCubes().stream().map(modelCuboidData -> modelCuboidData.bake(34, 27)).collect(Collectors.toList());
		cloak = new ModelPart(cuboids, new HashMap<>());
	}

	private boolean hasLayer(ItemStack itemStack, EquipmentClientInfo.LayerType layerType) {
		Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
		if (equippable != null && !equippable.assetId().isEmpty()) {
			EquipmentClientInfo equipmentClientInfo = this.equipmentAssets.get((ResourceKey)equippable.assetId().get());
			return !equipmentClientInfo.getLayers(layerType).isEmpty();
		} else {
			return false;
		}
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, PlayerRenderState player, float limbAngle, float limbDistance) {
		// Safety try catch to avoid crashes!
		try {
			if (hasCustomBanner(player) && !player.isInvisible) {
				ItemStack bannerItem = ((Bannerable) player).getBannerItem();
				ItemStack itemStack = player.chestEquipment;
				if (this.hasLayer(player.chestEquipment, EquipmentClientInfo.LayerType.WINGS)) {
					return;
				}
				poseStack.pushPose();

				if (this.hasLayer(player.chestEquipment, EquipmentClientInfo.LayerType.HUMANOID)) {
					poseStack.translate(0.0F, -0.053125F, 0.06875F);
				}

				if (MBFOptions.SAMURAI_BANNER.getBooleanValue()) {
					VertexConsumer vertexConsumer = ModelBakery.BANNER_BASE.buffer(multiBufferSource, RenderType::entityNoOutline);

					poseStack.translate(0, -0.2, 0.5);

					if (player.isCrouching) {
						poseStack.mulPose(Axis.XP.rotationDegrees(28));
						poseStack.translate(0, -0.1, -0.15);
					} else {
						poseStack.translate(0, 0, -0.02);
					}

					float scale = 0.4F;
					poseStack.pushPose();
					poseStack.scale(scale, scale, scale);
					poseStack.translate(0, 3.96, -0.828);
					bar.render(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(player, 0));

					poseStack.scale(0.99F, 0.99F, 1.25F);
					poseStack.translate(0, -2.3, 0.97);
					poseStack.mulPose(Axis.YP.rotationDegrees(90));
					bar.render(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(player, 0));

					poseStack.popPose();

					poseStack.pushPose();
					poseStack.scale(scale, scale, scale);
					poseStack.translate(0, 1.5, -0.75);
					poseStack.mulPose(Axis.XN.rotationDegrees(10));
					pole.render(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(player, 0));
					poseStack.popPose();

					poseStack.mulPose(Axis.YP.rotationDegrees(90));

					poseStack.translate(-0.15, -0.1, 0.015);
					poseStack.scale(1, 1, 0.5F);

					RendererUtils.renderCanvasFromItem(bannerItem, poseStack, multiBufferSource, light, OverlayTexture.NO_OVERLAY, cloak);

				} else {
//					poseStack.translate(0.0D, 0.0D, 0.125D);
//					double d = Mth.lerp(tickDelta, player.xCloakO, player.xCloak) - Mth.lerp(tickDelta, player.xo, player.getX());
//					double e = Mth.lerp(tickDelta, player.yCloakO, player.yCloak) - Mth.lerp(tickDelta, player.yo, player.getY());
//					double m = Mth.lerp(tickDelta, player.zCloakO, player.zCloak) - Mth.lerp(tickDelta, player.zo, player.getZ());
//					float n = player.yBodyRotO + (player.yBodyRot - player.yBodyRotO);
//					double o = Mth.sin(n * 0.017453292F);
//					double p = -Mth.cos(n * 0.017453292F);
//					float q = (float)e * 10.0F;
//					q = Mth.clamp(q, -6.0F, 32.0F);
//					float r = (float)(d * o + m * p) * 100.0F;
//					r = Mth.clamp(r, 0, 150.0F);
//					float s = (float)(d * p - m * o) * 100.0F;
//					s = Mth.clamp(s, -20.0F, 20.0F);
//					if (r < 0.0F) {
//						r = 0.0F;
//					}
//
//					float t = Mth.lerp(tickDelta, player.oBob, player.bob);
//					q += Mth.sin(Mth.lerp(tickDelta, player.walkDistO, player.walkDist) * 6.0F) * 32.0F * t;
//					if (player.isCrouching()) {
//						q += 25.0F;
//						poseStack.translate(0, 0.14, -0.02);
//					}
//
//					poseStack.mulPose(Axis.ZP.rotationDegrees(s / 2.0F));
//					poseStack.mulPose(Axis.XP.rotationDegrees(6.0F + r / 2.0F + q));
//					poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - s / 2.0F));

					this.model.setupAnim(player);
					RendererUtils.renderCanvasFromItem(bannerItem, poseStack, multiBufferSource, light, OverlayTexture.NO_OVERLAY, cloak);
				}


				poseStack.popPose();
			}
		} catch (Exception exception) {
			ErrorSystemManager.reportException();
			exception.printStackTrace();
		}
	}

	public boolean hasCustomBanner(PlayerRenderState entityRenderState) {
		return entityRenderState instanceof Bannerable && !((Bannerable) entityRenderState).getBannerItem().isEmpty();
	}
}
