package dev.svrt.domiirl.mbf.legacy;

//? if <1.21.4 {
/*import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

// The banner flag as 1.21.4's BannerFlagModel draws it, see BannerModel for the offset
public class BannerFlagModel {

	// 1.21.1's BannerRenderer moves the flag to y -32 on every frame
	private static final float FLAG_Y = -32.0F;

	private final ModelPart flag;

	public BannerFlagModel(ModelPart root) {
		this.flag = root.getChild("flag");
		BannerModel.place(this.flag, FLAG_Y);
	}

	public ModelPart root() {
		return this.flag;
	}

	public void resetPose() {
		this.flag.resetPose();
	}

	public void setupAnim(float phase) {
		this.flag.xRot = (-0.0125F + 0.01F * Mth.cos((float) (Math.PI * 2) * phase)) * (float) Math.PI;
	}
}
*///?}
