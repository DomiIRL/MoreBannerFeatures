package dev.svrt.domiirl.mbf.mixin.minecart;

import dev.svrt.domiirl.mbf.accessor.MinecartBannerable;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
//? if >=1.20.3 {
import net.minecraft.world.entity.vehicle.VehicleEntity;
//?} else {
/*import net.minecraft.world.entity.Entity;
*///?}
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
//? if >=1.21.6 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?} else {
/*import dev.svrt.domiirl.mbf.utils.BannerSaveData;
import net.minecraft.nbt.CompoundTag;
*///?}
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
//? if >=1.20.3 {
public abstract class AbstractMinecartMixin extends VehicleEntity implements MinecartBannerable {
//?} else {
/*public abstract class AbstractMinecartMixin extends Entity implements MinecartBannerable {
*///?}

	private static final EntityDataAccessor<ItemStack> BANNER = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.ITEM_STACK);

	public AbstractMinecartMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	public @NotNull ItemStack mbf$getBannerItem() {
		if (!mbf$isEnabled()) {
			return ItemStack.EMPTY;
		}
		return this.entityData.get(BANNER);
	}

	@Override
	public void mbf$setBannerItem(@NotNull ItemStack itemStack) {
		this.entityData.set(BANNER, itemStack);
	}

	@Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
	//? if >=1.20.5 {
	private void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(BANNER, ItemStack.EMPTY);
	}
	//?} else {
	/*private void defineSynchedData(CallbackInfo ci) {
		this.entityData.define(BANNER, ItemStack.EMPTY);
	}
	*///?}

	//? if >=1.21.6 {
	@Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
	private void readAdditionalSaveData(ValueInput input, CallbackInfo ci) {
		input.read("Banner", ItemStack.CODEC).ifPresent(this::mbf$setBannerItem);
	}

	@Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
	private void addAdditionalSaveData(ValueOutput output, CallbackInfo ci) {
		if (!mbf$getBannerItem().isEmpty()) {
			output.storeNullable("Banner", ItemStack.CODEC, mbf$getBannerItem());
		}
	}
	//?} else {
	/*@Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
	private void readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
		BannerSaveData.read(tag, this).ifPresent(this::mbf$setBannerItem);
	}

	@Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
	private void addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
		BannerSaveData.write(tag, this, mbf$getBannerItem());
	}
	*///?}

	@Override
	public void remove(RemovalReason removalReason) {
		if (!mbf$getBannerItem().isEmpty() && this.level() instanceof ServerLevel serverLevel) {
			spawnAtLocation(serverLevel, mbf$getBannerItem());
			mbf$setBannerItem(ItemStack.EMPTY);
		}
		super.remove(removalReason);
	}
}
