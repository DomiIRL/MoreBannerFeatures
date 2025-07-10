package dev.svrt.domiirl.mbf.mixin.minecart;

import dev.svrt.domiirl.mbf.accessor.MinecartBannerable;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin extends VehicleEntity implements MinecartBannerable {

	private static final EntityDataAccessor<ItemStack> BANNER = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.ITEM_STACK);

	public AbstractMinecartMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	public @NotNull ItemStack moreBannerFeatures$getBannerItem() {
		if (!moreBannerFeatures$isEnabled()) {
			return ItemStack.EMPTY;
		}
		return this.entityData.get(BANNER);
	}

	@Override
	public void moreBannerFeatures$setBannerItem(@NotNull ItemStack itemStack) {
		this.entityData.set(BANNER, itemStack);
	}

	@Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
	private void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(BANNER, ItemStack.EMPTY);
	}

	@Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
	private void readAdditionalSaveData(ValueInput input, CallbackInfo ci) {
		input.read("Banner", ItemStack.CODEC).ifPresent(this::moreBannerFeatures$setBannerItem);
	}

	@Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
	private void addAdditionalSaveData(ValueOutput output, CallbackInfo ci) {
		if (!moreBannerFeatures$getBannerItem().isEmpty()) {
			output.storeNullable("Banner", ItemStack.CODEC, moreBannerFeatures$getBannerItem());
		}
	}

	@Override
	public void remove(RemovalReason removalReason) {
		if (!moreBannerFeatures$getBannerItem().isEmpty() && this.level() instanceof ServerLevel serverLevel) {
			spawnAtLocation(serverLevel, moreBannerFeatures$getBannerItem());
			moreBannerFeatures$setBannerItem(ItemStack.EMPTY);
		}
		super.remove(removalReason);
	}
}
