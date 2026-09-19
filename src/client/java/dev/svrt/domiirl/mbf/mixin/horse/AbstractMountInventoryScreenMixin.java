package dev.svrt.domiirl.mbf.mixin.horse;

import dev.svrt.domiirl.mbf.gui.BannerSlot;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractMountInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractMountInventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMountInventoryScreen.class)
public abstract class AbstractMountInventoryScreenMixin extends AbstractContainerScreen<AbstractMountInventoryMenu> {

	@Shadow protected abstract void extractSlot(GuiGraphicsExtractor guiGraphics, int i, int j);

	public AbstractMountInventoryScreenMixin(AbstractMountInventoryMenu handler, Inventory inventory, Component title) {
		super(handler, inventory, title);
	}

	@Inject(method = "extractBackground", at = @At("TAIL"))
	public void drawBackground(GuiGraphicsExtractor guiGraphics, int i, int j, float f, CallbackInfo ci) {
		if (this.menu.slots.getLast() instanceof BannerSlot) {
			int localI = (this.width - this.imageWidth) / 2;
			int localJ = (this.height - this.imageHeight) / 2;

			extractSlot(guiGraphics, localI + 7, localJ + 53);
		}
	}
}
