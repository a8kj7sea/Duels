package me.sanhak.duel.commands;

import me.sanhak.duel.Main;
import me.sanhak.duel.inventory.KitSelectInventory;
import me.sanhak.duel.cooldown.Cooldown;
import me.sanhak.duel.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DuelCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if (!(commandSender instanceof Player)) {
			commandSender.sendMessage(StringUtils.format("&cThis command is for players only!"));
			return false;
		}
		Player sender = (Player) commandSender;
		UUID sUUID = sender.getUniqueId();

		if (args.length != 1) {
			sender.sendMessage(StringUtils.format("&cCorrect Usage: /duel <player>"));
			return false;
		}

		Player receiver = Bukkit.getPlayer(args[0]);
		if (receiver == null) {
			sender.sendMessage(StringUtils.format("&c" + args[0] + " is offline!"));
			return false;
		}

		if (receiver == sender) {
			sender.sendMessage(StringUtils.format("&cYou cannot send yourself a duel!"));
			return false;
		}

		if (Main.getInstance().isInCombat(receiver)) {
			sender.sendMessage(StringUtils.format("&cThat player is currently in combat."));
			return false;
		}

		Cooldown cooldown = new Cooldown();
		if (!cooldown.isOut(sUUID)) {
			sender.sendMessage(StringUtils.format("&cPlease wait " + cooldown.getRemainingTime(sUUID) + " before using this command again!"));
			return false;
		}

		cooldown.addPlayer(sUUID, 60);
		sender.openInventory(KitSelectInventory.selectKit(receiver));
		return true;
	}
}