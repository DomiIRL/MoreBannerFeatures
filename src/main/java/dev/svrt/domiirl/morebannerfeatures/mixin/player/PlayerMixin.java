package dev.svrt.domiirl.morebannerfeatures.mixin.player;

import dev.svrt.domiirl.morebannerfeatures.core.accessor.Bannerable;
import dev.svrt.domiirl.morebannerfeatures.utils.MBFUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0.4
 */
@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements Bannerable {

	public PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public ItemStack getBannerItem() {
		return MBFUtils.getCloakItem(((Player) (Object) this));
	}

}
