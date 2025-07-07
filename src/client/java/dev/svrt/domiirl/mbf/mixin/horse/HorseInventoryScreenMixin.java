package dev.svrt.domiirl.mbf.mixin.horse;

import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.HorseInventoryMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
@Mixin(HorseInventoryScreen.class)
public abstract class HorseInventoryScreenMixin extends AbstractContainerScreen<HorseInventoryMenu> {

	@Shadow @Final private AbstractHorse horse;

	@Shadow protected abstract void drawSlot(GuiGraphics guiGraphics, int i, int j);

	public HorseInventoryScreenMixin(HorseInventoryMenu handler, Inventory inventory, Component title) {
		super(handler, inventory, title);
	}

	@Inject(method = "renderBg", at = @At(value = "TAIL"))
	public void drawBackground(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci) {

		if (this.horse instanceof Bannerable && MBFOptions.HORSE_SLOT.getBooleanValue()) {
			int localI = (this.width - this.imageWidth) / 2;
			int localJ = (this.height - this.imageHeight) / 2;

			drawSlot(guiGraphics, localI + 7, localJ + 53);
		}

	}

}
