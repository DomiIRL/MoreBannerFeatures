package dev.svrt.domiirl.mbf.mixin.horse;

import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import dev.svrt.domiirl.mbf.gui.BannerSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.HorseInventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HorseInventoryMenu.class)
public abstract class HorseInventoryMenuMixin extends AbstractContainerMenu {

    @Unique
    private static final ResourceLocation SLOT_ICON = ResourceLocation.fromNamespaceAndPath(MoreBannerFeatures.MOD_ID, "container/slot/banner");

    @Shadow @Final private Container horseContainer;
    @Unique
    private Bannerable bannerable;

    protected HorseInventoryMenuMixin(@Nullable MenuType<?> type, int syncId) {
        super(type, syncId);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void init(int syncId, Inventory playerInventory, Container inventory, AbstractHorse entity, int j, CallbackInfo ci) {
        if (entity instanceof Bannerable bannerable && MBFOptions.HORSE_SLOT.getBooleanValue()) {
            this.bannerable = bannerable;
            int x = 8;
            int y = 54;
            Container container2 = entity.createEquipmentSlotContainer(EquipmentSlot.CHEST);
            this.addSlot(new BannerSlot(entity, container2, 0, x, y, SLOT_ICON));
        }
    }

    @Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
    private void onQuickMoveStack(Player player, int index, CallbackInfoReturnable<ItemStack> cir) {
        if (!MBFOptions.HORSE_SLOT.getBooleanValue() || this.bannerable == null) {
            return;
        }

        int bannerSlotIndex = this.slots.size() - 1;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemStack = slot.getItem();
            ItemStack original = itemStack.copy();

            if (index == bannerSlotIndex) {
                if (!this.moveItemStackTo(itemStack, 2 + this.horseContainer.getContainerSize(), bannerSlotIndex, true)) {
                    cir.setReturnValue(ItemStack.EMPTY);
                    return;
                }
                slot.setChanged();
                cir.setReturnValue(original);
                return;
            }

            if (this.getSlot(bannerSlotIndex).mayPlace(itemStack) && !this.getSlot(bannerSlotIndex).hasItem()) {
                ItemStack moved = itemStack.copy();
                if (!this.moveItemStackTo(itemStack, bannerSlotIndex, bannerSlotIndex + 1, false)) {
                    cir.setReturnValue(ItemStack.EMPTY);
                    return;
                }
                slot.setChanged();
                cir.setReturnValue(moved);
            }
        }
    }
}
