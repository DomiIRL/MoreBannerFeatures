package dev.svrt.domiirl.mbf.mixin.horse;

import dev.svrt.domiirl.mbf.gui.BannerSlot;
//? if >=1.20 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?} else {
/*import com.mojang.blaze3d.vertex.PoseStack;
*///?}
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
//? if >=1.21.11 {
import net.minecraft.client.gui.screens.inventory.AbstractMountInventoryScreen;
import net.minecraft.world.inventory.AbstractMountInventoryMenu;
//?} else {
/*import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.world.inventory.HorseInventoryMenu;
*///?}
//? if >=1.20 && <1.21.4 {
/*import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
*///?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21.11 {
@Mixin(AbstractMountInventoryScreen.class)
public abstract class AbstractMountInventoryScreenMixin extends AbstractContainerScreen<AbstractMountInventoryMenu> {
//?} else {
/*@Mixin(HorseInventoryScreen.class)
public abstract class AbstractMountInventoryScreenMixin extends AbstractContainerScreen<HorseInventoryMenu> {
*///?}

	//? if >=26.1 {
	@Shadow protected abstract void extractSlot(GuiGraphicsExtractor guiGraphics, int i, int j);
	//?} else if >=1.21.4 {
	/*@Shadow protected abstract void drawSlot(GuiGraphicsExtractor guiGraphics, int i, int j);
	*///?} else if >=1.20.2 {
	/*@Shadow @Final private static Identifier SADDLE_SLOT_SPRITE;
	*///?} else if >=1.20 {
	/*@Shadow @Final private static Identifier HORSE_INVENTORY_LOCATION;
	*///?}

	//? if >=1.21.11 {
	public AbstractMountInventoryScreenMixin(AbstractMountInventoryMenu handler, Inventory inventory, Component title) {
	//?} else {
	/*public AbstractMountInventoryScreenMixin(HorseInventoryMenu handler, Inventory inventory, Component title) {
	*///?}
		super(handler, inventory, title);
	}

	//? if >=26.1 {
	@Inject(method = "extractBackground", at = @At("TAIL"))
	public void drawBackground(GuiGraphicsExtractor guiGraphics, int i, int j, float f, CallbackInfo ci) {
	//?} else if >=1.20 {
	/*@Inject(method = "renderBg", at = @At("TAIL"))
	public void drawBackground(GuiGraphicsExtractor guiGraphics, float f, int i, int j, CallbackInfo ci) {
	*///?} else {
	/*@Inject(method = "renderBg", at = @At("TAIL"))
	public void drawBackground(PoseStack poseStack, float f, int i, int j, CallbackInfo ci) {
	*///?}
		if (this.menu.slots.get(this.menu.slots.size() - 1) instanceof BannerSlot) {
			int localI = (this.width - this.imageWidth) / 2;
			int localJ = (this.height - this.imageHeight) / 2;

			//? if >=26.1 {
			extractSlot(guiGraphics, localI + 7, localJ + 53);
			//?} else if >=1.21.4 {
			/*drawSlot(guiGraphics, localI + 7, localJ + 53);
			*///?} else if >=1.20.2 {
			/*// The saddle slot is a plain slot frame, drawSlot only arrived in 1.21.4
			guiGraphics.blitSprite(SADDLE_SLOT_SPRITE, localI + 7, localJ + 53, 18, 18);
			*///?} else if >=1.20 {
			/*guiGraphics.blit(HORSE_INVENTORY_LOCATION, localI + 7, localJ + 53, 18, this.imageHeight + 54, 18, 18);
			*///?} else {
			/*// The empty frame vanilla draws for the saddle slot, the horse texture is still bound here
			this.blit(poseStack, localI + 7, localJ + 53, 18, this.imageHeight + 54, 18, 18);
			*///?}
		}
	}
}
