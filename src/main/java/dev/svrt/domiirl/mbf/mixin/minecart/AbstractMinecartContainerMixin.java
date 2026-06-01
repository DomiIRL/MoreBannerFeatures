package dev.svrt.domiirl.mbf.mixin.minecart;

import dev.svrt.domiirl.mbf.utils.BannerableEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecartContainer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecartContainer.class)
public abstract class AbstractMinecartContainerMixin extends AbstractMinecart {

  protected AbstractMinecartContainerMixin(EntityType<?> entityType, Level level) {
    super(entityType, level);
  }

  @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
  private void onInteract(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
    BannerableEntity.handleInteract(this, player, interactionHand, cir);
  }
}
