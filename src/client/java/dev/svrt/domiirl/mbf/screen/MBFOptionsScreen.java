package dev.svrt.domiirl.mbf.screen;

import dev.svrt.domiirl.mbf.config.MBFConfigManager;
import dev.svrt.domiirl.mbf.config.options.BooleanOption;
import dev.svrt.domiirl.mbf.config.options.IOption;
import net.minecraft.client.Minecraft;
//? if >=1.19 {
import net.minecraft.client.OptionInstance;
//?} else {
/*import net.minecraft.client.CycleOption;
import net.minecraft.client.Option;
*///?}
//? if >=1.19.3 {
import net.minecraft.client.gui.components.Tooltip;
//?}
import net.minecraft.client.gui.screens.Screen;
//? if >=1.21 {
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
//?} else {
/*import net.minecraft.client.gui.screens.SimpleOptionsSubScreen;
*///?}
import net.minecraft.network.chat.Component;

// Before 1.21 SimpleOptionsSubScreen takes the options up front instead of asking addOptions
//? if >=1.21 {
public class MBFOptionsScreen extends OptionsSubScreen {
//?} else {
/*public class MBFOptionsScreen extends SimpleOptionsSubScreen {
*///?}

	private final Screen previous;

	@SuppressWarnings("resource")
	public MBFOptionsScreen(Screen previous) {
		//? if >=1.21 {
		super(previous, Minecraft.getInstance().options, Component.translatable("mbf.options"));
		//?} else {
		/*super(previous, Minecraft.getInstance().options, Component.translatable("mbf.options"), getAllToDisplay());
		*///?}
		this.previous = previous;
	}

	//? if >=1.21 {
	@Override
	protected void addOptions() {
		this.list.addSmall(getAllToDisplay());
	}
	//?}

	@Override
	public void removed() {
		MBFConfigManager.save();
	}

	//? if >=1.19 {
	public static OptionInstance<?>[] getAllToDisplay() {
		return MBFConfigManager.getAllOptions().stream().filter(IOption::shouldDisplay).map(MBFOptionsScreen::toOption).toArray(OptionInstance[]::new);
	}

	public static OptionInstance<?> toOption(IOption option) {
		if (option instanceof BooleanOption bOption) {
			if (bOption.tooltip != null) {
				//? if >=1.19.3 {
				return OptionInstance.createBoolean("mbf.options." + bOption.key, (value) -> Tooltip.create(bOption.tooltip), bOption.value, newValue -> {
				//?} else {
				/*return OptionInstance.createBoolean("mbf.options." + bOption.key, OptionInstance.cachedConstantTooltip(bOption.tooltip), bOption.value, newValue -> {
				*///?}
					bOption.value = newValue;
				});
			} else {
				return OptionInstance.createBoolean("mbf.options." + bOption.key, bOption.value, newValue -> {
					bOption.value = newValue;
				});
			}
		}
		return null;
	}
	//?} else {
	/*// Before 1.19 options were CycleOptions bound to the vanilla Options object, so ours ignore it
	public static Option[] getAllToDisplay() {
		return MBFConfigManager.getAllOptions().stream().filter(IOption::shouldDisplay).map(MBFOptionsScreen::toOption).toArray(Option[]::new);
	}

	public static Option toOption(IOption option) {
		if (option instanceof BooleanOption bOption) {
			return CycleOption.createOnOff("mbf.options." + bOption.key, bOption.tooltip, options -> bOption.value, (options, opt, newValue) -> {
				bOption.value = newValue;
			});
		}
		return null;
	}
	*///?}

}
