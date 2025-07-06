package dev.svrt.domiirl.morebannerfeatures.mixin.horse;

import dev.svrt.domiirl.morebannerfeatures.core.accessor.InventoryBannerable;
import dev.svrt.domiirl.morebannerfeatures.core.accessor.SideBannerable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin extends Animal implements SideBannerable, InventoryBannerable {

	@Shadow protected SimpleContainer inventory;

	private static final EntityDataAccessor<ItemStack> BANNER_ITEM = SynchedEntityData.defineId(AbstractHorse.class, EntityDataSerializers.ITEM_STACK);

	protected AbstractHorseMixin(EntityType<? extends Animal> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "getInventorySize", at = @At(value = "RETURN"), cancellable = true)
	private void getInventorySize(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(cir.getReturnValue() + 1);
	}

//	@Inject(method = "containerChanged", at = @At(value = "HEAD"), cancellable = true)
//	private void onInventoryChanged(Container sender, CallbackInfo ci) {
//
//		ItemStack newStack = sender.getItem(getSlot());
//		ItemStack oldStack = this.entityData.get(BANNER_ITEM);
//		this.entityData.set(BANNER_ITEM, newStack);
//
//		if (newStack != oldStack && newStack.getItem() instanceof BannerItem) {
//			this.level().playSound(null, this, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.5F, 1.0F);
//		}
//
//	}

	@Override
	public ItemStack getBannerItem() {
		return this.entityData.get(BANNER_ITEM);
	}

	@Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
	private void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(BANNER_ITEM, ItemStack.EMPTY);
	}

	@Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
	private void readAdditionalSaveData(ValueInput input, CallbackInfo ci) {
		input.read("Banner", ItemStack.CODEC).ifPresent(itemStack -> {
			this.entityData.set(BANNER_ITEM, itemStack);
			this.inventory.setItem(getSlot(), itemStack);
		});
	}

	@Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
	private void addAdditionalSaveData(ValueOutput output, CallbackInfo ci) {
		ItemStack item = this.inventory.getItem(getSlot());
		if (!item.isEmpty()) {
			output.storeNullable("Banner", ItemStack.CODEC, item);
		}
	}
}
