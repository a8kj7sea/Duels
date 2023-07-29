package me.sanhak.duel.manager;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PAPIHook extends PlaceholderExpansion {
    @Override
    public String getAuthor() {
        return "Sanhak";
    }

    @Override
    public String getIdentifier() {
        return "Duels";
    }

    @Override
    public String getVersion() {
        return "1.1.4";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equalsIgnoreCase("topkills")) {
            List<PlayerData> topKills = PlayerData.getTopKills();
            if (topKills.isEmpty()) {
                return "";
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < topKills.size(); i++) {
                PlayerData data = topKills.get(i);
                sb.append(i + 1).append(". ").append(data.getPlayer().getName()).append(": ").append(data.getKills());
                if (i < topKills.size() - 1) {
                    sb.append(", ");
                }
            }

            return sb.toString();
        }
        return null;
    }
}