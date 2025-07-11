package dev.svrt.domiirl.mbf.mixin.boat;

import dev.svrt.domiirl.mbf.accessor.BoatBannerable;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoat.class)
public abstract class AbstractBoatMixin extends VehicleEntity implements Leashable, BoatBannerable {

	@Unique
	private static final EntityDataAccessor<ItemStack> BANNER = SynchedEntityData.defineId(AbstractBoat.class, EntityDataSerializers.ITEM_STACK);

	public AbstractBoatMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	public @NotNull ItemStack mbf$getBannerItem() {
		if (!MBFOptions.BOAT_BANNERS.getBooleanValue()) {
			return ItemStack.EMPTY;
		}
		return this.entityData.get(BANNER);
	}

	@Override
	public void mbf$setBannerItem(@NotNull ItemStack itemStack) {
		this.entityData.set(BANNER, itemStack);
	}

	@Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
	private void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(BANNER, ItemStack.EMPTY);
	}

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

	@Override
	public void remove(RemovalReason removalReason) {
		super.remove(removalReason);
		if (!mbf$getBannerItem().isEmpty() && this.level() instanceof ServerLevel serverLevel) {
			spawnAtLocation(serverLevel, mbf$getBannerItem());
			mbf$setBannerItem(ItemStack.EMPTY);
		}
	}
}
