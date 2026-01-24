package dev.svrt.domiirl.mbf.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.svrt.domiirl.mbf.RendererUtils;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import dev.svrt.domiirl.mbf.errors.ErrorSystemManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelPart.Cube;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class BannerCapeFeatureRenderer extends RenderLayer<AvatarRenderState, PlayerModel> {

	private final ModelPart cloak;
	private final Model<Unit> cloakModel;

	private final MaterialSet materials;
	private final EquipmentAssetManager equipmentAssets;

	public BannerCapeFeatureRenderer(RenderLayerParent<AvatarRenderState, PlayerModel> featureRendererContext, MaterialSet materials, EquipmentAssetManager equipmentAssetManager) {
		super(featureRendererContext);

		this.materials = materials;
		this.equipmentAssets = equipmentAssetManager;

		// OWN CLOAK WITH CUSTOM TEXTURE SIZE TO FIT THE BANNER TEXTURE
		CubeListBuilder modelPartBuilder = new CubeListBuilder();
		modelPartBuilder.texOffs(0, 0).addBox(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F);
		List<Cube> cuboids = modelPartBuilder.getCubes().stream().map(modelCuboidData -> modelCuboidData.bake(34, 27)).collect(Collectors.toList());
		cloak = new ModelPart(cuboids, new HashMap<>());

		// Wrap the ModelPart in a Model.Simple
		this.cloakModel = new Model.Simple(cloak, RenderType::entityNoOutline);
	}

	private boolean hasLayer(ItemStack itemStack, EquipmentClientInfo.LayerType layerType) {
		Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
		if (equippable != null && !equippable.assetId().isEmpty()) {
			EquipmentClientInfo equipmentClientInfo = this.equipmentAssets.get(equippable.assetId().get());
			return !equipmentClientInfo.getLayers(layerType).isEmpty();
		} else {
			return false;
		}
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, AvatarRenderState state, float limbAngle, float limbDistance) {
		try {
			if (!state.isInvisible && state instanceof Bannerable bannerable && bannerable.mbf$isEnabled() && RendererUtils.isLegitPlayerBannerEquipment(bannerable.mbf$getBannerItem())) {
				if (!MBFOptions.ELYTRA_CAPES.getBooleanValue() && this.hasLayer(state.chestEquipment, EquipmentClientInfo.LayerType.WINGS)) {
					return;
				}

				poseStack.pushPose();

				if (this.hasLayer(state.chestEquipment, EquipmentClientInfo.LayerType.HUMANOID)) {
					poseStack.translate(0.0F, -0.053125F, 0.06875F);
				}

				poseStack.translate(0.0D, 0.0D, 0.145D);

				float capeLean = state.capeLean;
				float capeLean2 = state.capeLean2;
				float capeFlap = state.capeFlap;
				boolean isCrouching = state.isCrouching;

				if (isCrouching) {
					poseStack.translate(0.0D, 0.14D, -0.02D);
				}

				Quaternionf quaternionf = new Quaternionf()
					.rotateY((float) Math.PI)
					.rotateX(-(6.0F + capeLean / 2.0F + capeFlap + (isCrouching ? 25.0F : 0.0F)) * ((float) Math.PI / 180F))
					.rotateZ(-capeLean2 / 2.0F * ((float) Math.PI / 180F))
					.rotateY(-(180.0F - capeLean2 / 2.0F) * ((float) Math.PI / 180F));
				poseStack.mulPose(quaternionf);

				RendererUtils.renderCanvasFromItem(
					this.materials,
					bannerable.mbf$getBannerItem(),
					poseStack,
					submitNodeCollector,
					light,
					OverlayTexture.NO_OVERLAY,
					this.cloakModel,
					Unit.INSTANCE
				);

				poseStack.popPose();
			}
		} catch (Exception exception) {
			ErrorSystemManager.reportException();
			exception.printStackTrace();
		}
	}
}
