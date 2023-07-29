package me.sanhak.duel.commands;

import me.sanhak.duel.configuration.DataFile;
import me.sanhak.duel.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {
	private static DataFile dataFile;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(StringUtils.format("&cThis command is for players only!"));
			return false;
		}

		Player player = (Player) sender;
		if (!player.hasPermission("1v1.setup")) {
			player.sendMessage(StringUtils.format("&cYou don't have enough permissions to perform this command!"));
			return false;
		}

		if (args.length != 0) {
			player.sendMessage(StringUtils.format("&cYou cannot use this command, please type &f&l/1v1rl"));
			return false;
		}

		dataFile.reload();
		player.sendMessage(StringUtils.format("&aYou have successfully reloaded the data file!"));

		return true;
	}
}