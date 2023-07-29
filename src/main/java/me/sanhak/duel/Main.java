package me.sanhak.duel;

import com.github.sirblobman.combatlogx.api.ICombatLogX;
import com.github.sirblobman.combatlogx.api.manager.ICombatManager;
import me.sanhak.duel.commands.AdminCommand;
import me.sanhak.duel.commands.DuelCommand;
import me.sanhak.duel.commands.DuelTopCommand;
import me.sanhak.duel.commands.ReloadCommand;
import me.sanhak.duel.configuration.DataFile;
import me.sanhak.duel.listeners.DuelEnd;
import me.sanhak.duel.listeners.KitInv;
import me.sanhak.duel.listeners.ReceiverInv;
import me.sanhak.duel.listeners.SetupInv;
import me.sanhak.duel.manager.PAPIHook;
import me.sanhak.duel.manager.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin {
	private static Main instance;
	private static Map<UUID, PlayerData> playerDataMap;
	private static HashMap<Player, Player> playersInDuel = new HashMap<>();
	private DataFile df;

	@Override
	public void onEnable() {
		instance = this;
		df = new DataFile(this, true);
		loadCommands();
		loadEvents();
		if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new PAPIHook().register();
		}
		PlayerData.loadData();
	}

	private void loadCommands() {
		getCommand("1v1setup").setExecutor(new AdminCommand());
		getCommand("1v1rl").setExecutor(new ReloadCommand());
		getCommand("duel").setExecutor(new DuelCommand());
		getCommand("dueltop").setExecutor(new DuelTopCommand());
	}

	private void loadEvents() {
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new DuelEnd(), this);
		pluginManager.registerEvents(new ReceiverInv(), this);
		pluginManager.registerEvents(new SetupInv(), this);
		pluginManager.registerEvents(new KitInv(), this);
	}

	public static Main getInstance() {
		return instance;
	}

	public static ICombatLogX getAPI() {
		PluginManager pluginManager = Bukkit.getPluginManager();
		Plugin plugin = pluginManager.getPlugin("CombatLogX");
		return (ICombatLogX) plugin;
	}

	public boolean isInCombat(Player player) {
		ICombatLogX plugin = getAPI();
		ICombatManager combatManager = plugin.getCombatManager();
		return combatManager.isInCombat(player);
	}

	public static HashMap<Player, Player> getPlayersInDuel() {
		return playersInDuel;
	}

	public static boolean isInDuel(Player player) {
		return playersInDuel.containsKey(player) || playersInDuel.containsValue(player);
	}
}