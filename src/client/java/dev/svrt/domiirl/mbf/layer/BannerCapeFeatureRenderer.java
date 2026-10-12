package dev.svrt.domiirl.mbf.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.svrt.domiirl.mbf.Poses;
import dev.svrt.domiirl.mbf.RendererUtils;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.api.BannerCapeApi;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import dev.svrt.domiirl.mbf.errors.ErrorSystemManager;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelPart.Cube;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.renderer.rendertype.RenderType;
//? if >=1.21.11 {
import net.minecraft.client.renderer.rendertype.RenderTypes;
//?}
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

//? if >=1.21.2 {
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.equipment.Equippable;
//?} else {
/*import net.minecraft.util.Mth;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
*///?}

//? if >=1.21.2 {
public class BannerCapeFeatureRenderer extends RenderLayer<AvatarRenderState, PlayerModel> {
//?} else {
/*public class BannerCapeFeatureRenderer extends RenderLayer<Avatar, PlayerModel<Avatar>> {
*///?}

	private final ModelPart cloak;
	//? if >=1.21.9 {
	private final Model<Unit> cloakModel;
	//?}

	//? if >=1.21.2 {
	private final EquipmentAssetManager equipmentAssets;

	public BannerCapeFeatureRenderer(RenderLayerParent<AvatarRenderState, PlayerModel> featureRendererContext, EquipmentAssetManager equipmentAssetManager) {
		super(featureRendererContext);

		this.equipmentAssets = equipmentAssetManager;
	//?} else {
	/*public BannerCapeFeatureRenderer(RenderLayerParent<Avatar, PlayerModel<Avatar>> featureRendererContext) {
		super(featureRendererContext);
	*///?}

		// OWN CLOAK WITH CUSTOM TEXTURE SIZE TO FIT THE BANNER TEXTURE
		CubeListBuilder modelPartBuilder = new CubeListBuilder();
		modelPartBuilder.texOffs(0, 0).addBox(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F);
		List<Cube> cuboids = modelPartBuilder.getCubes().stream().map(modelCuboidData -> modelCuboidData.bake(BannerCapeApi.TEXTURE_WIDTH, BannerCapeApi.TEXTURE_HEIGHT)).collect(Collectors.toList());
		cloak = new ModelPart(cuboids, new HashMap<>());

		// Wrap the ModelPart in a Model.Simple
		//? if >=26.1 {
		this.cloakModel = new Model.Simple(cloak, RenderTypes::entitySolid);
		//?} else if >=1.21.9 {
		/*this.cloakModel = new Model.Simple(cloak, RenderTypes::entityNoOutline);
		*///?}
	}

	//? if >=1.21.2 {
	private boolean hasLayer(ItemStack itemStack, EquipmentClientInfo.LayerType layerType) {
		Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
		if (equippable != null && !equippable.assetId().isEmpty()) {
			EquipmentClientInfo equipmentClientInfo = this.equipmentAssets.get(equippable.assetId().get());
			return !equipmentClientInfo.getLayers(layerType).isEmpty();
		} else {
			return false;
		}
	}
	//?}

	@Override
	//? if >=1.21.2 {
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, AvatarRenderState state, float limbAngle, float limbDistance) {
		boolean invisible = state.isInvisible;
		boolean wings = this.hasLayer(state.chestEquipment, EquipmentClientInfo.LayerType.WINGS);
		boolean chestArmor = this.hasLayer(state.chestEquipment, EquipmentClientInfo.LayerType.HUMANOID);
		float capeFlap = state.capeFlap;
		float capeLean = state.capeLean;
		float capeLean2 = state.capeLean2;
	//?} else {
	/*public void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, Avatar state, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean invisible = state.isInvisible();
		boolean wings = state.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA);
		// The 1.21.1 vanilla cape does not move out for chest armor either
		boolean chestArmor = false;

		// Vanilla 1.21.1 CapeLayer's swing, later precomputed into the render state as capeFlap and capeLean
		double dx = Mth.lerp(partialTick, state.xCloakO, state.xCloak) - Mth.lerp(partialTick, state.xo, state.getX());
		double dy = Mth.lerp(partialTick, state.yCloakO, state.yCloak) - Mth.lerp(partialTick, state.yo, state.getY());
		double dz = Mth.lerp(partialTick, state.zCloakO, state.zCloak) - Mth.lerp(partialTick, state.zo, state.getZ());
		float bodyRot = Mth.rotLerp(partialTick, state.yBodyRotO, state.yBodyRot) * ((float) Math.PI / 180F);
		double sin = Mth.sin(bodyRot);
		double cos = -Mth.cos(bodyRot);
		float capeFlap = Mth.clamp((float) dy * 10.0F, -6.0F, 32.0F);
		float capeLean = Mth.clamp((float) (dx * sin + dz * cos) * 100.0F, 0.0F, 150.0F);
		float capeLean2 = Mth.clamp((float) (dx * cos - dz * sin) * 100.0F, -20.0F, 20.0F);
		capeFlap += Mth.sin(Mth.lerp(partialTick, state.walkDistO, state.walkDist) * 6.0F) * 32.0F * Mth.lerp(partialTick, state.oBob, state.bob);
		if (state.isCrouching()) {
			capeFlap += 25.0F;
		}
	*///?}
		try {
			if (!invisible && state instanceof Bannerable bannerable && bannerable.mbf$isEnabled() && RendererUtils.isLegitPlayerBannerEquipment(bannerable.mbf$getBannerItem())) {
				if (!MBFOptions.ELYTRA_CAPES.getBooleanValue() && wings) {
					return;
				}

				poseStack.pushPose();

				if (chestArmor) {
					poseStack.translate(0.0F, -0.053125F, 0.06875F);
				}

				//? if >=1.21.2 {
				// Placed like the vanilla cape, child of the body part, PI cancels setupAnim's -PI
				this.getParentModel().body.translateAndRotate(poseStack);
				//?}
				poseStack.translate(0.0F, 0.0F, 0.125F);

				Poses.rotate(poseStack, new Quaternionf()
					.rotateX((6.0F + capeLean / 2.0F + capeFlap) * ((float) Math.PI / 180F))
					.rotateZ(capeLean2 / 2.0F * ((float) Math.PI / 180F))
					.rotateY((180.0F - capeLean2 / 2.0F) * ((float) Math.PI / 180F)));

				//? if <1.21.2 {
				/*// 1.21.1's PlayerModel moves its cloak by this much while crouching
				if (state.isCrouching()) {
					poseStack.translate(0.0F, 1.85F / 16.0F, 1.4F / 16.0F);
				}
				*///?}

				RendererUtils.renderCanvasFromItem(
					bannerable.mbf$getBannerItem(),
					poseStack,
					submitNodeCollector,
					light,
					OverlayTexture.NO_OVERLAY,
					//? if >=1.21.9 {
					this.cloakModel,
					Unit.INSTANCE
					//?} else {
					/*this.cloak
					*///?}
				);

				poseStack.popPose();
			}
		} catch (Exception exception) {
			ErrorSystemManager.reportException();
			exception.printStackTrace();
		}
	}
}
