package me.sanhak.duel.utils;

import org.bukkit.ChatColor;

public class StringUtils {
	public static String format(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public static String formatTime(int time) {
		int minutes = time / 60;
		int seconds = time % 60;
		return String.format("%02d:%02d", minutes, seconds);
	}
}
