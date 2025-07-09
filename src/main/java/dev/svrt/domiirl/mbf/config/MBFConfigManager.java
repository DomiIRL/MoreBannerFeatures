package dev.svrt.domiirl.mbf.config;

import com.google.gson.*;
import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import dev.svrt.domiirl.mbf.config.options.IOption;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

public class MBFConfigManager {

	public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

	private static File configFile;

	private static List<IOption> cachedOptions;

	public static void loadConfigFile() {
		if (configFile == null) {
			configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), MoreBannerFeatures.MOD_ID + ".json");
		}
	}

	public static void load() {
		loadConfigFile();

		try {
			if (!configFile.exists()) {
				save();
			}
			if (configFile.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(configFile));
				JsonObject json = new JsonParser().parse(br).getAsJsonObject();

				for (IOption option : getAllOptions()) {
					option.read(json);
				}

			}
		} catch (Exception exception) {
			System.err.println("Couldn't load configuration file");
			exception.printStackTrace();
		}

	}

	public static void save() {
		loadConfigFile();

		JsonObject json = new JsonObject();

		try {
			for (IOption option : getAllOptions()) {
				option.write(json);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		String jsonString = GSON.toJson(json);

		try (FileWriter fileWriter = new FileWriter(configFile)) {
			fileWriter.write(jsonString);
		} catch (IOException e) {
			System.err.println("Couldn't save configuration file");
			e.printStackTrace();
		}

	}

	public static List<IOption> getAllOptions() {

		if (cachedOptions != null && !cachedOptions.isEmpty()) {
			return cachedOptions;
		}

		List<IOption> options = new LinkedList<>();
		for (Field field : MBFOptions.class.getDeclaredFields()) {
			try {
				if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
					if (IOption.class.isAssignableFrom(field.getType())) {
						options.add(((IOption) field.get(null)));
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		cachedOptions = options;
		return options;
	}

}
