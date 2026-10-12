package dev.svrt.domiirl.mbf;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// Adds the mixins whose targets only exist on some Minecraft versions, a mixin json can't be versioned
public abstract class MixinPlugin implements IMixinConfigPlugin {

	public static final class Common extends MixinPlugin {

		@Override
		public List<String> getMixins() {
			List<String> mixins = new ArrayList<>();
			//? if >=1.21.6 {
			mixins.add("ghast.HappyGhastMixin");
			//?}
			// Chest minecarts only override interact from 1.20, the container minecart hook covers them before
			//? if >=1.20 {
			mixins.add("minecart.MinecartChestMixin");
			//?}
			return mixins;
		}
	}

	public static final class Client extends MixinPlugin {

		@Override
		public List<String> getMixins() {
			List<String> mixins = new ArrayList<>();
			// Forge and NeoForge load the client config on servers too and only skip its "client" list there
			if (MixinEnvironment.getDefaultEnvironment().getSide() != MixinEnvironment.Side.CLIENT) {
				return mixins;
			}
			//? if >=1.21.9 {
			mixins.add("banner.BannerRenderStateMixin");
			//?}
			//? if >=1.21.2 {
			mixins.add("boat.BoatRenderStateMixin");
			mixins.add("horse.AbstractHorseRenderStateMixin");
			mixins.add("minecart.MinecartRenderStateMixin");
			mixins.add("player.PlayerRenderStateMixin");
			mixins.add("strider.StriderRenderStateMixin");
			//?}
			//? if >=1.21.4 {
			mixins.add("common.ItemModelResolverMixin");
			mixins.add("common.LivingEntityRendererMixin");
			//?} else {
			/*mixins.add("common.CustomHeadLayerMixin");
			*///?}
			//? if >=1.21.6 {
			mixins.add("ghast.HappyGhastRendererMixin");
			mixins.add("ghast.HappyGhastRenderStateMixin");
			//?}
			return mixins;
		}
	}

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
}
