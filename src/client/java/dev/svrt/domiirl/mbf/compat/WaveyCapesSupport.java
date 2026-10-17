//? if !forge || <1.21.11 {
package dev.svrt.domiirl.mbf.compat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.svrt.domiirl.mbf.api.BannerCapeApi;
import dev.svrt.domiirl.mbf.layer.BannerCapeFeatureRenderer;
import dev.svrt.domiirl.mbf.platform.Platform;
import dev.tr7zw.transition.mc.entitywrapper.PlayerWrapper;
import dev.tr7zw.waveycapes.render.CapeInfos;
import dev.tr7zw.waveycapes.render.CapePass;
import dev.tr7zw.waveycapes.render.CapeRenderer;
import dev.tr7zw.waveycapes.support.ModSupport;
import dev.tr7zw.waveycapes.support.SupportManager;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
//? if >=1.20 {
import net.minecraft.core.Direction;
//?}
import net.minecraft.world.entity.LivingEntity;

//? if >=1.20 {
import java.util.EnumSet;
//?}
import java.util.List;
import java.util.Map;
//? if >=1.20 {
import java.util.Set;
//?}

// Lets WaveyCapes animate the banner cape in place of MBF's static one
public final class WaveyCapesSupport {

	private WaveyCapesSupport() {
	}

	public static void register() {
		if (Platform.isModLoaded("waveycapes") && hasCapePasses()) {
			SupportManager.mods.add(new Support());
		}
	}

	// WaveyCapes releases without the pass API cannot draw a banner
	private static boolean hasCapePasses() {
		try {
			Class.forName("dev.tr7zw.waveycapes.render.CapePass");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	// Its own class so WaveyCapes' types only load once WaveyCapes is known to be there
	private static final class Support implements ModSupport {

		private final BannerCapeRenderer renderer = new BannerCapeRenderer();

		@Override
		public boolean shouldBeUsed(PlayerWrapper capeRenderInfo) {
			LivingEntity player = player(capeRenderInfo);
			return player != null && BannerCapeApi.hasBannerCape(player);
		}

		@Override
		public CapeRenderer getRenderer() {
			return this.renderer;
		}

		// Above the other cape sources, the banner is what the player chose to wear
		@Override
		public int priority() {
			return 100;
		}

		@Override
		public boolean blockFeatureRenderer(Object feature) {
			return feature instanceof BannerCapeFeatureRenderer;
		}
	}

	private static final class BannerCapeRenderer implements CapeRenderer {

		private final ModelPart[] strips = buildStrips();

		@Override
		public List<CapePass> getCapePasses(PlayerWrapper capeRenderInfo) {
			return BannerCapeApi.capePasses(player(capeRenderInfo)).stream()
				.map(pass -> new CapePass(pass.renderType(), pass::wrap)).toList();
		}

		@Override
		public void render(PlayerWrapper capeRenderInfo, int part, ModelPart model, PoseStack poseStack,
				VertexConsumer vertexConsumer, int light, int overlay) {
			this.strips[part].render(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
		}

		@Override
		public boolean singleSidedTexture() {
			return true;
		}

		@Override
		public int uvSheetWidth() {
			return BannerCapeApi.TEXTURE_WIDTH;
		}

		@Override
		public int uvSheetHeight() {
			return BannerCapeApi.TEXTURE_HEIGHT;
		}

		@Override
		public CapeInfos getCapeInfo(PlayerWrapper capeRenderInfo) {
			return null;
		}
	}

	private static LivingEntity player(PlayerWrapper wrapper) {
		//? if >=1.21.9 {
		return wrapper.getAvatar();
		//?} else {
		/*return wrapper.getEntity();
		*///?}
	}

	// One strip per WaveyCapes segment, each sampling its own row of the banner texture
	private static ModelPart[] buildStrips() {
		//? if >=1.20 {
		// Top and bottom faces would sample the neighbouring rows and show as seams
		Set<Direction> sides = EnumSet.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
		//?}
		ModelPart[] parts = new ModelPart[16];
		for (int i = 0; i < parts.length; i++) {
			//? if >=1.20 {
			ModelPart.Cube cube = new ModelPart.Cube(0, i, -5.0F, i, -1.0F, 10.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, false,
				BannerCapeApi.TEXTURE_WIDTH, BannerCapeApi.TEXTURE_HEIGHT, sides);
			//?} else {
			/*ModelPart.Cube cube = new ModelPart.Cube(0, i, -5.0F, i, -1.0F, 10.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, false,
				BannerCapeApi.TEXTURE_WIDTH, BannerCapeApi.TEXTURE_HEIGHT);
			*///?}
			parts[i] = new ModelPart(List.of(cube), Map.of());
		}
		return parts;
	}
}
//?}
