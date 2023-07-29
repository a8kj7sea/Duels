package me.sanhak.duel.listeners;

import me.sanhak.duel.Main;
import me.sanhak.duel.enums.StatusEnum;
import me.sanhak.duel.manager.Game;
import me.sanhak.duel.manager.PlayerData;
import me.sanhak.duel.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DuelEnd implements Listener {
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player loser = e.getEntity();
		Player winner = null;
		World world = loser.getWorld();
		if(Main.isInDuel(loser)) {
			if (e.getEntity().getLastDamageCause() != null && e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE) {
				if (Main.getPlayersInDuel().size() > 1) {
					for (Player player : Main.getPlayersInDuel().keySet()) {
						if (player != loser) {
							winner = player;
							break;
						}
					}
				}
			} else {
				winner = loser.getKiller();
			}

			e.setDeathMessage(null);

			if (winner != null) {
				String kitType = Game.getKitType();
				PlayerData data = new PlayerData(winner);

				data.incrementKills();
				winner.sendMessage(StringUtils.format("&aYou have killed " + loser.getName()));
				loser.sendMessage(StringUtils.format("&cYou have been killed by " + winner.getName()));
				Bukkit.getScheduler().cancelTask(new Game(loser, winner, kitType).drawTaskID);

				Player finalWinner = winner;
				new BukkitRunnable() {
					@Override
					public void run() {
						Game game = new Game(loser, finalWinner, kitType);
						game.gameStatus = StatusEnum.END;
						game.started = false;
						game.endGame(finalWinner, loser);

						finalWinner.teleport(world.getSpawnLocation());
						loser.teleport(world.getSpawnLocation());

						Main.getPlayersInDuel().remove(finalWinner);
						Main.getPlayersInDuel().remove(loser);
						Main.getPlayersInDuel().clear();
					}
				}.runTaskLater(Main.getInstance(), 40L);
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player loser = e.getPlayer();
		Player winner = null;

		if (Main.isInDuel(loser)) {
			String kitType = Game.getKitType();

			for (Player player : Main.getPlayersInDuel().keySet()) {
				if (player != loser) {
					winner = player;
					break;
				}
			}

			if (winner != null) {
				winner.sendMessage(StringUtils.format("&aYour opponent has disconnected. You win!"));
				loser.setHealth(0);

				Bukkit.getScheduler().cancelTask(new Game(loser, winner, kitType).drawTaskID);

				World world = loser.getWorld();
				Game game = new Game(loser, winner, kitType);
				game.gameStatus = StatusEnum.END;
				game.started = false;
				game.endGame(winner, loser);

				winner.teleport(world.getSpawnLocation());
				loser.teleport(world.getSpawnLocation());

				Main.getPlayersInDuel().remove(winner);
				Main.getPlayersInDuel().remove(loser);
				Main.getPlayersInDuel().clear();
			}
		}
	}

	@EventHandler
	public void RespawnEvent(PlayerDeathEvent e) {
		Player player = e.getEntity();
		World world = player.getWorld();

		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			player.spigot().respawn();
			player.teleport(world.getSpawnLocation());
		}, 2L);
	}
}