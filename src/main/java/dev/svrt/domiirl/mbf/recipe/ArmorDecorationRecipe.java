package dev.svrt.domiirl.mbf.recipe;

import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import dev.svrt.domiirl.mbf.registry.ModRecipeSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.NotNull;

public class ArmorDecorationRecipe extends CustomRecipe {

    public ArmorDecorationRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipeSerializers.ARMOR_DECORATION;
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        ItemStack chestplate = ItemStack.EMPTY;
        ItemStack banner = ItemStack.EMPTY;
        int itemCount = 0;

        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (!stack.isEmpty()) {
                itemCount++;
                if (isValidArmor(stack)) {
                    if (!chestplate.isEmpty()) return false; // Multiple chestplates
                    chestplate = stack;
                } else if (stack.getItem() instanceof BannerItem) {
                    if (!banner.isEmpty()) return false; // Multiple banners
                    banner = stack;
                }
            }
        }

        return itemCount == 2 && !chestplate.isEmpty() && !banner.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider registryAccess) {
        ItemStack chestplate = ItemStack.EMPTY;
        ItemStack banner = ItemStack.EMPTY;

        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (!stack.isEmpty()) {
                if (isValidArmor(stack)) {
                    chestplate = stack;
                } else if (stack.getItem() instanceof BannerItem) {
                    banner = stack;
                }
            }
        }

        if (chestplate.isEmpty() || banner.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = chestplate.copy();

        DyeColor baseColor = ((BannerItem) banner.getItem()).getColor();
        BannerPatternLayers patterns = banner.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);

        result.set(ModDataComponents.BANNER_BASE_COLOR, baseColor);
        result.set(DataComponents.BANNER_PATTERNS, patterns);

        return result;
    }

    public boolean isValidArmor(ItemStack itemStack) {
        Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
        return equippable != null && (equippable.slot() == EquipmentSlot.CHEST
//          || equippable.slot() == EquipmentSlot.HEAD
        );
    }
}
