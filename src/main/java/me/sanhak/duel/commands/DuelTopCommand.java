package me.sanhak.duel.commands;

import me.sanhak.duel.manager.PlayerData;
import me.sanhak.duel.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelTopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        StringBuilder message = new StringBuilder();
        message.append("\n")
                .append(StringUtils.format("          &aTop 10 Wins - Duels\n"))
                .append("\n");

        int position = 1;
        for (PlayerData data : PlayerData.getTopKills()) {
            String playerName = data.getPlayer().getName();
            int kills = data.getKills();

            message.append(position).append(". ").append(playerName).append(": ").append(kills).append(" Kills\n");
            position++;
        }

        message.append(" ");

        player.sendMessage(message.toString());

        return true;
    }
}