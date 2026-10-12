package dev.svrt.domiirl.mbf.utils;

import dev.svrt.domiirl.mbf.accessor.Bannerable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

// The entity type constants moved from EntityType into EntityTypes in 26.2
//? if >=26.2 {
import static net.minecraft.world.entity.EntityTypes.*;
//?} else {
/*import static net.minecraft.world.entity.EntityType.*;
*///?}

public class BannerableEntity {

  public static final List<EntityType<?>> BANNERABLE_ENTITY_TYPES = new ArrayList<>(List.of(
    //? if >=1.21.2 {
    ACACIA_BOAT,
    BIRCH_BOAT,
    DARK_OAK_BOAT,
    JUNGLE_BOAT,
    OAK_BOAT,
    SPRUCE_BOAT,
    CHERRY_BOAT,
    ACACIA_CHEST_BOAT,
    BIRCH_CHEST_BOAT,
    DARK_OAK_CHEST_BOAT,
    JUNGLE_CHEST_BOAT,
    OAK_CHEST_BOAT,
    SPRUCE_CHEST_BOAT,
    CHERRY_CHEST_BOAT,
    //?} else {
    /*BOAT,
    CHEST_BOAT,
    *///?}
    STRIDER,
    HORSE,
    DONKEY,
    MULE,
    SKELETON_HORSE,
    ZOMBIE_HORSE,
    MINECART,
    CHEST_MINECART,
    TNT_MINECART,
    SPAWNER_MINECART,
    FURNACE_MINECART,
    COMMAND_BLOCK_MINECART,
    HOPPER_MINECART,
    //? if >=1.21.6 {
    HAPPY_GHAST,
    //?}
    VILLAGER
  ));

  public static void handleInteract(Entity entity, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
    if (!(BANNERABLE_ENTITY_TYPES.contains(entity.getType()))) {
      return;
    }

    if (!(entity instanceof Bannerable bannerable) || !bannerable.mbf$isEnabled() || player.isSecondaryUseActive()) {
      return;
    }

    if (entity instanceof AgeableMob ageableMob && ageableMob.isBaby()) {
      return;
    }

    if (entity instanceof TamableAnimal tamableAnimal && !tamableAnimal.isTame()) {
      return;
    }

    // AbstractHorse is not a TamableAnimal, so the check above misses horses
    if (entity instanceof AbstractHorse abstractHorse && !abstractHorse.isTamed()) {
      return;
    }

    ItemStack itemStack = player.getItemInHand(hand);
    if (itemStack.getItem() instanceof BannerItem) {
      if (ItemStack.isSameItem(bannerable.mbf$getBannerItem(), itemStack)) return;

      entity.level().playSound(null, entity, SoundEvents.HORSE_STEP_WOOD, SoundSource.PLAYERS, 1.0F, 1.0F);

      if (!bannerable.mbf$getBannerItem().isEmpty()) {
        dropBanner(entity, bannerable);
      }

      ItemStack copy = itemStack.copy();
      copy.setCount(1);
      bannerable.mbf$setBannerItem(copy);

      if (!player.getAbilities().instabuild) {
        itemStack.shrink(1);
      }

      cir.setReturnValue(InteractionResult.SUCCESS);
      cir.cancel();
    } else if (itemStack.getItem() instanceof ShearsItem && !bannerable.mbf$getBannerItem().isEmpty()) {

      entity.level().playSound(null, entity, SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
      entity.gameEvent(GameEvent.SHEAR, player);
      EquipmentSlot equipmentSlot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
      //? if >=1.20.5 {
      itemStack.hurtAndBreak(1, player, equipmentSlot);
      //?} else {
      /*itemStack.hurtAndBreak(1, player, broken -> broken.broadcastBreakEvent(equipmentSlot));
      *///?}

      dropBanner(entity, bannerable);
      bannerable.mbf$setBannerItem(ItemStack.EMPTY);
      cir.setReturnValue(InteractionResult.SUCCESS);
      cir.cancel();
    }
  }

  private static void dropBanner(Entity entity, Bannerable bannerable) {
    if (!(entity.level() instanceof ServerLevel serverLevel)) {
      return;
    }
    //? if >=1.21.6 {
    Vec3 vec3 = entity.getType().getDimensions().attachments().getAverage(EntityAttachment.PASSENGER);
    entity.spawnAtLocation(serverLevel, bannerable.mbf$getBannerItem(), vec3);
    //?} else if >=1.20.5 {
    /*Vec3 vec3 = entity.getType().getDimensions().attachments().get(EntityAttachment.PASSENGER, 0, 0.0F);
    entity.spawnAtLocation(serverLevel, bannerable.mbf$getBannerItem(), (float) vec3.y);
    *///?} else {
    /*// No attachment points yet, the top of the entity is where a rider sits
    entity.spawnAtLocation(serverLevel, bannerable.mbf$getBannerItem(), entity.getBbHeight());
    *///?}
  }
}
