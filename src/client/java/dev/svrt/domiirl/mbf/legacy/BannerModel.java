package dev.svrt.domiirl.mbf.legacy;

//? if <1.21.4 {
/*import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;

// The banner pole and bar as 1.21.4's BannerModel draws them, built from the single banner layer before it.
// That layer sits 12 units lower, so the parts are lifted to the 1.21.4 positions every renderer is placed for
public class BannerModel {

	static final float LAYER_OFFSET = -12.0F;

	private final ModelPart root;

	public BannerModel(ModelPart root) {
		this.root = root;
		lift(root.getChild("pole"));
		lift(root.getChild("bar"));
	}

	static void lift(ModelPart part) {
		place(part, part.getInitialPose().y);
	}

	static void place(ModelPart part, float layerY) {
		PartPose pose = part.getInitialPose();
		part.setInitialPose(PartPose.offsetAndRotation(pose.x, layerY + LAYER_OFFSET, pose.z, pose.xRot, pose.yRot, pose.zRot));
		part.resetPose();
	}

	public ModelPart root() {
		return this.root;
	}

	public void resetPose() {
		this.root.getAllParts().forEach(ModelPart::resetPose);
	}

	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay) {
		this.root.getChild("pole").render(poseStack, vertexConsumer, light, overlay);
		this.root.getChild("bar").render(poseStack, vertexConsumer, light, overlay);
	}
}
*///?}
