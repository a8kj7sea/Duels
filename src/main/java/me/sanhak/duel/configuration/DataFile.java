package me.sanhak.duel.configuration;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DataFile {

	private File file;
	private FileConfiguration configurationFile;
	private String fileName = "data.yml";

	public DataFile(JavaPlugin plugin, boolean saveDefaultData) {
		file = new File(plugin.getDataFolder(), fileName);
		file.getParentFile().mkdirs();

		if (!file.exists()) {
			if (saveDefaultData) {
				plugin.saveResource(fileName, false);
			} else {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		configurationFile = YamlConfiguration.loadConfiguration(file);
	}

	public void save() {
		try {
			configurationFile.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reload() {
		configurationFile = YamlConfiguration.loadConfiguration(file);
	}

	public FileConfiguration getConfigurationFile() {
		return configurationFile;
	}
}