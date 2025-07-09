package dev.svrt.domiirl.mbf.mixin.common;

import dev.svrt.domiirl.mbf.utils.BannerableEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

  @Shadow public abstract Level level();

  @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
  private void onInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
    BannerableEntity.handleInteract((Entity) (Object) this, player, hand, cir);
  }
}
