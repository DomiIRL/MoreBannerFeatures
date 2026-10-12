package dev.svrt.domiirl.mbf.screen;

import dev.svrt.domiirl.mbf.config.MBFConfigManager;
import dev.svrt.domiirl.mbf.config.options.BooleanOption;
import dev.svrt.domiirl.mbf.config.options.IOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Tooltip;
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

	public static OptionInstance<?>[] getAllToDisplay() {
		return MBFConfigManager.getAllOptions().stream().filter(IOption::shouldDisplay).map(MBFOptionsScreen::toOption).toArray(OptionInstance[]::new);
	}

	public static OptionInstance<?> toOption(IOption option) {
		if (option instanceof BooleanOption bOption) {
			if (bOption.tooltip != null) {
				return OptionInstance.createBoolean("mbf.options." + bOption.key, (value) -> Tooltip.create(bOption.tooltip), bOption.value, newValue -> {
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

}
