package me.sanhak.duel.commands;

import me.sanhak.duel.inventory.SetupInventory;
import me.sanhak.duel.itemstack.ItemCreator;
import me.sanhak.duel.utils.StringUtils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if (!(commandSender instanceof Player)) {
			commandSender.sendMessage(StringUtils.format("&cThis command is for players only!"));
			return false;
		}
		Player admin = (Player) commandSender;
		if (!admin.hasPermission("1v1.setup")) {
			admin.sendMessage(StringUtils.format("&cYou don't have enough permissions to perform this command!"));
			return false;
		}
		if (args.length != 0) {
			admin.sendMessage(StringUtils.format("&cYou cannot use this command with arguments, please type &f&l/1v1setup"));
			return false;
		}
		admin.playSound(admin.getLocation(), Sound.CHEST_OPEN, 0.5f, 6.0f);
		admin.openInventory(SetupInventory.createSetupInventory());
		admin.playSound(admin.getLocation(), Sound.CHICKEN_EGG_POP, 0.5f, 6.0f);
		return true;
	}
}