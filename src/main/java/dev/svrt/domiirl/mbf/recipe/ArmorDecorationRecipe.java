package dev.svrt.domiirl.mbf.recipe;

import dev.svrt.domiirl.mbf.config.MBFOptions;
import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import dev.svrt.domiirl.mbf.registry.ModRecipeSerializers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
//? if >=1.21.2 {
import net.minecraft.world.item.equipment.Equippable;
//?} else {
/*import net.minecraft.world.item.Equipable;
*///?}
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.NotNull;

//? if >=26.1 {
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
//?} else {
/*import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.CraftingBookCategory;
*///?}
//? if <1.21.2 {
/*import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
*///?}

public class ArmorDecorationRecipe extends CustomRecipe {

    //? if >=26.1 {
    public static final MapCodec<ArmorDecorationRecipe> MAP_CODEC = MapCodec.unit(ArmorDecorationRecipe::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, ArmorDecorationRecipe> STREAM_CODEC = StreamCodec.unit(new ArmorDecorationRecipe());
    public static final RecipeSerializer<ArmorDecorationRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    public ArmorDecorationRecipe() {
        super();
    }
    //?} else if >=1.21.2 {
    /*public static final RecipeSerializer<ArmorDecorationRecipe> SERIALIZER = new CustomRecipe.Serializer<>(ArmorDecorationRecipe::new);

    public ArmorDecorationRecipe(CraftingBookCategory category) {
        super(category);
    }
    *///?} else {
    /*public static final RecipeSerializer<ArmorDecorationRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(ArmorDecorationRecipe::new);

    public ArmorDecorationRecipe(CraftingBookCategory category) {
        super(category);
    }
    *///?}

    @Override
    public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipeSerializers.ARMOR_DECORATION;
    }

    //? if <1.21.2 {
    /*@Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }
    *///?}

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
    //? if >=26.1 {
    public ItemStack assemble(CraftingInput craftingInput) {
    //?} else {
    /*public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider registryAccess) {
    *///?}
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
        //? if >=1.21.2 {
        Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
        return equippable != null && (equippable.slot() == EquipmentSlot.CHEST || equippable.slot() == EquipmentSlot.HEAD);
        //?} else {
        /*Equipable equipable = Equipable.get(itemStack);
        return equipable != null && (equipable.getEquipmentSlot() == EquipmentSlot.CHEST || equipable.getEquipmentSlot() == EquipmentSlot.HEAD);
        *///?}
    }
}
