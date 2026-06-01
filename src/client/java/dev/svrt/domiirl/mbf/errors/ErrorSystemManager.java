package dev.svrt.domiirl.mbf.errors;

import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.net.URI;

public class ErrorSystemManager {

	private static long lastTimeReported = -1;

	public static void reportException() {
		if (canBeReportedAgain()) {
			lastTimeReported = System.currentTimeMillis();
			LocalPlayer player = Minecraft.getInstance().player;
			if (player != null) {
				MutableComponent text = Component.translatable("mbf.message.error");
				Style style = text.getStyle().withClickEvent(new ClickEvent.OpenUrl(URI.create("https://discord.gg/7BSqZa9r3P")));
				text.setStyle(style);
				player.sendSystemMessage(text);
			}
		}
	}

	public static boolean canBeReportedAgain() {
		return MBFOptions.ERRORS.getBooleanValue() && (lastTimeReported == -1 || System.currentTimeMillis() - lastTimeReported >= 60000);
	}

}
