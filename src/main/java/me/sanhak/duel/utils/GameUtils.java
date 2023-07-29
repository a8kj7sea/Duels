package me.sanhak.duel.utils;

import me.sanhak.duel.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class GameUtils {
	public static void preparePlayers(Player player1, Player player2) {
		player1.setHealth(20.0);
		player2.setHealth(20.0);
		player1.setFoodLevel(20);
		player2.setFoodLevel(20);
		player1.setGameMode(GameMode.ADVENTURE);
		player2.setGameMode(GameMode.ADVENTURE);
		removePotionEffects(player1);
		removePotionEffects(player2);
	}

	public static void removePotionEffects(Player player) {
		for (PotionEffect potionEffect : player.getActivePotionEffects()) {
			player.removePotionEffect(potionEffect.getType());
		}
	}


	public static void applyKit(Player player, String selectedKit) {
		File dataFolder = new File(Main.getInstance().getDataFolder(), "kits");
		File playerFile = new File(dataFolder.getPath() + File.separator + selectedKit + ".yml");
		try {
			YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);

			List<?> contents = playerConfig.getList("inventory");
			List<?> armor = playerConfig.getList("armor");

			if (contents instanceof List<?> && armor instanceof List<?>) {
				player.getInventory().setContents(((List<ItemStack>) contents).toArray(new ItemStack[0]));
				player.getInventory().setArmorContents(((List<ItemStack>) armor).toArray(new ItemStack[0]));
			} else {
				throw new IllegalArgumentException("Invalid configuration format.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}