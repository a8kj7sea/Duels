package me.sanhak.duel.configuration;

import me.sanhak.duel.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class LocationUtils {
	private static DataFile dataFile;
	private static final FileConfiguration config;

	static {
		JavaPlugin plugin = Main.getInstance();
		dataFile = new DataFile(plugin, true);
		config = dataFile.getConfigurationFile();
	}

	public static Location getFirstLocation() {
		String worldName = config.getString("First-Location.World");
		int x = config.getInt("First-Location.X");
		int y = config.getInt("First-Location.Y");
		int z = config.getInt("First-Location.Z");
		return new Location(Bukkit.getWorld(worldName), x, y, z);
	}

	public static Location getSecondLocation() {
		String worldName = config.getString("Second-Location.World");
		int x = config.getInt("Second-Location.X");
		int y = config.getInt("Second-Location.Y");
		int z = config.getInt("Second-Location.Z");
		return new Location(Bukkit.getWorld(worldName), x, y, z);
	}

	public static void setFirstLocation(String world, int x, int y, int z) {
		config.set("First-Location.World", world);
		config.set("First-Location.X", x);
		config.set("First-Location.Y", y);
		config.set("First-Location.Z", z);
		dataFile.save();
	}

	public static void setSecondLocation(String world, int x, int y, int z) {
		config.set("Second-Location.World", world);
		config.set("Second-Location.X", x);
		config.set("Second-Location.Y", y);
		config.set("Second-Location.Z", z);
		dataFile.save();
	}
}
