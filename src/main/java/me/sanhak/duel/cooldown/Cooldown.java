package me.sanhak.duel.cooldown;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cooldown {
	private Map<UUID, Long> cooldowns = new HashMap<>();

	public boolean isOut(UUID playerId) {
		return !cooldowns.containsKey(playerId) || cooldowns.get(playerId) <= System.currentTimeMillis();
	}

	public void addPlayer(UUID playerId, long timeInSeconds) {
		long cooldownEndTime = System.currentTimeMillis() + (timeInSeconds * 1000);
		cooldowns.put(playerId, cooldownEndTime);
	}

	public void removePlayer(UUID playerId) {
		cooldowns.remove(playerId);
	}

	public int getRemainingTime(UUID playerId) {
		long remainingTime = cooldowns.getOrDefault(playerId, 0L) - System.currentTimeMillis();
		return (int) Math.max(0, remainingTime / 1000);
	}
}