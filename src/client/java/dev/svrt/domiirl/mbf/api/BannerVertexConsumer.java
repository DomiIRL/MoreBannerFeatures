package dev.svrt.domiirl.mbf.api;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ARGB;

// Maps uvs onto the sprite itself, vanilla's wrap loses them when setUv is chained before 26.3
record BannerVertexConsumer(VertexConsumer delegate, TextureAtlasSprite sprite, int tint) implements VertexConsumer {

	//? if >=1.21 {
	@Override
	public VertexConsumer addVertex(float x, float y, float z) {
		this.delegate.addVertex(x, y, z);
		return this;
	}

	@Override
	public VertexConsumer setColor(int red, int green, int blue, int alpha) {
		return this.setColor(ARGB.color(alpha, red, green, blue));
	}

	@Override
	public VertexConsumer setColor(int color) {
		this.delegate.setColor(ARGB.multiply(color, this.tint));
		return this;
	}

	@Override
	public VertexConsumer setUv(float u, float v) {
		this.delegate.setUv(this.sprite.getU(u), this.sprite.getV(v));
		return this;
	}

	@Override
	public VertexConsumer setUv1(int u, int v) {
		this.delegate.setUv1(u, v);
		return this;
	}

	@Override
	public VertexConsumer setUv2(int u, int v) {
		this.delegate.setUv2(u, v);
		return this;
	}

	//? if >=26.3 {
	@Override
	public VertexConsumer setUv3(float u, float v) {
		this.delegate.setUv3(u, v);
		return this;
	}
	//?}

	@Override
	public VertexConsumer setNormal(float x, float y, float z) {
		this.delegate.setNormal(x, y, z);
		return this;
	}

	//? if >=1.21.11 {
	@Override
	public VertexConsumer setLineWidth(float width) {
		this.delegate.setLineWidth(width);
		return this;
	}
	//?}
	//?} else {
	/*@Override
	public VertexConsumer vertex(double x, double y, double z) {
		this.delegate.vertex(x, y, z);
		return this;
	}

	@Override
	public VertexConsumer color(int red, int green, int blue, int alpha) {
		int color = ARGB.multiply(ARGB.color(alpha, red, green, blue), this.tint);
		this.delegate.color(ARGB.red(color), ARGB.green(color), ARGB.blue(color), ARGB.alpha(color));
		return this;
	}

	@Override
	public VertexConsumer uv(float u, float v) {
		this.delegate.uv(this.sprite.getU(u), this.sprite.getV(v));
		return this;
	}

	@Override
	public VertexConsumer overlayCoords(int u, int v) {
		this.delegate.overlayCoords(u, v);
		return this;
	}

	@Override
	public VertexConsumer uv2(int u, int v) {
		this.delegate.uv2(u, v);
		return this;
	}

	@Override
	public VertexConsumer normal(float x, float y, float z) {
		this.delegate.normal(x, y, z);
		return this;
	}

	@Override
	public void endVertex() {
		this.delegate.endVertex();
	}

	@Override
	public void defaultColor(int red, int green, int blue, int alpha) {
		this.delegate.defaultColor(red, green, blue, alpha);
	}

	@Override
	public void unsetDefaultColor() {
		this.delegate.unsetDefaultColor();
	}
	*///?}
}
