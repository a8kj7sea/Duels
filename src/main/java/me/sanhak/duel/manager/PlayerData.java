package me.sanhak.duel.manager;

import me.sanhak.duel.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlayerData {
    private static final int MAX_TOP_KILLS = 10;
    private static List<PlayerData> topKills = new ArrayList<>();
    private final Player player;
    private int kills;

    public PlayerData(Player player) {
        this.player = player;
        this.kills = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public int getKills() {
        return kills;
    }

    public void incrementKills() {
        kills++;
        updateTopKills();
        saveData();
    }

    private void updateTopKills() {
        topKills.removeIf(data -> data.player.equals(player));
        topKills.add(this);
        topKills.sort(Comparator.comparingInt(PlayerData::getKills).reversed());

        if (topKills.size() > MAX_TOP_KILLS) {
            topKills = topKills.subList(0, MAX_TOP_KILLS);
        }
    }

    public static void loadData() {
        File dataFile = new File(Main.getInstance().getDataFolder(), "top10.txt");
        if (!dataFile.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    String playerName = parts[0];
                    int kills = Integer.parseInt(parts[1]);
                    Player player = Bukkit.getPlayerExact(playerName);
                    if (player != null) {
                        PlayerData playerData = new PlayerData(player);
                        playerData.kills = kills;
                        topKills.add(playerData);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveData() {
        File dataFile = new File(Main.getInstance().getDataFolder(), "data.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            for (PlayerData playerData : topKills) {
                Player player = playerData.getPlayer();
                if (player != null) {
                    String playerName = player.getName();
                    int kills = playerData.getKills();
                    writer.write(playerName + ": " + kills);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<PlayerData> getTopKills() {
        return topKills;
    }
}