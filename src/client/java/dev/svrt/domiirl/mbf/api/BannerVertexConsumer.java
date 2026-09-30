package dev.svrt.domiirl.mbf.api;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ARGB;

// Maps uvs onto the sprite itself, vanilla's wrap loses them when setUv is chained before 26.3
@Environment(EnvType.CLIENT)
record BannerVertexConsumer(VertexConsumer delegate, TextureAtlasSprite sprite, int tint) implements VertexConsumer {

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

	@Override
	public VertexConsumer setUv3(float u, float v) {
		this.delegate.setUv3(u, v);
		return this;
	}
	@Override
	public VertexConsumer setNormal(float x, float y, float z) {
		this.delegate.setNormal(x, y, z);
		return this;
	}

	@Override
	public VertexConsumer setLineWidth(float width) {
		this.delegate.setLineWidth(width);
		return this;
	}
}
