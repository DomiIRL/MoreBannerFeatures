package dev.svrt.domiirl.mbf.mixin.horse;

import dev.svrt.domiirl.mbf.gui.BannerSlot;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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
	//?} else {
	/*@Shadow protected abstract void drawSlot(GuiGraphicsExtractor guiGraphics, int i, int j);
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
	//?} else {
	/*@Inject(method = "renderBg", at = @At("TAIL"))
	public void drawBackground(GuiGraphicsExtractor guiGraphics, float f, int i, int j, CallbackInfo ci) {
	*///?}
		if (this.menu.slots.getLast() instanceof BannerSlot) {
			int localI = (this.width - this.imageWidth) / 2;
			int localJ = (this.height - this.imageHeight) / 2;

			//? if >=26.1 {
			extractSlot(guiGraphics, localI + 7, localJ + 53);
			//?} else {
			/*drawSlot(guiGraphics, localI + 7, localJ + 53);
			*///?}
		}
	}
}
