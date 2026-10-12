package dev.svrt.domiirl.mbf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import org.joml.Quaternionf;

// PoseStack only gained rotate/rotateDegrees in 26.3 and dropped mulPose(Quaternionf) with it
public final class Poses {

	private Poses() {
	}

	public static void rotateDegrees(PoseStack poseStack, Axis axis, float degrees) {
		//? if >=26.3 {
		poseStack.rotateDegrees(axis, degrees);
		//?} else {
		/*poseStack.mulPose(axis.rotationDegrees(degrees));
		*///?}
	}

	public static void rotate(PoseStack poseStack, Axis axis, float radians) {
		//? if >=26.3 {
		poseStack.rotate(axis, radians);
		//?} else {
		/*poseStack.mulPose(axis.rotation(radians));
		*///?}
	}

	public static void rotate(PoseStack poseStack, Quaternionf rotation) {
		//? if >=26.3 {
		poseStack.rotate(rotation);
		//?} else {
		/*poseStack.mulPose(rotation);
		*///?}
	}

}
