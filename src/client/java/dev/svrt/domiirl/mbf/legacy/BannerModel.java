package dev.svrt.domiirl.mbf.legacy;

//? if <1.21.4 {
/*import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;

// The banner pole and bar as 1.21.4's BannerModel draws them, built from the single banner layer before it.
// That layer sits 12 units lower, so the parts are lifted to the 1.21.4 positions every renderer is placed for
public class BannerModel {

	static final float LAYER_OFFSET = -12.0F;

	private final ModelPart root;

	public BannerModel(ModelPart root) {
		this.root = root;
		root.getChild("pole").y += LAYER_OFFSET;
		root.getChild("bar").y += LAYER_OFFSET;
	}

	public ModelPart root() {
		return this.root;
	}

	// Nothing in the pole or bar is animated
	public void resetPose() {
	}

	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay) {
		this.root.getChild("pole").render(poseStack, vertexConsumer, light, overlay);
		this.root.getChild("bar").render(poseStack, vertexConsumer, light, overlay);
	}
}
*///?}
