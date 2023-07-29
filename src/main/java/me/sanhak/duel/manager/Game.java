package me.sanhak.duel.manager;

import me.sanhak.duel.Main;
import me.sanhak.duel.utils.GameUtils;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import me.sanhak.duel.configuration.LocationUtils;
import me.sanhak.duel.enums.StatusEnum;
import me.sanhak.duel.utils.StringUtils;

import java.util.HashMap;

public class Game {
	private final Player sender;
	private final Player receiver;
	public boolean started = false;
	public StatusEnum gameStatus;
	private final Location[] locations = { LocationUtils.getFirstLocation(), LocationUtils.getSecondLocation() };
	private int prepareTaskID;
	public int drawTaskID;
	private static String kitType;
	private static final HashMap<String, ItemStack[]> invContents = new HashMap<>();
	private static final HashMap<String, ItemStack[]> armContents = new HashMap<>();

	public Game(Player sender, Player receiver, String kitType) {
		this.sender = sender;
		this.receiver = receiver;
		this.kitType = ChatColor.stripColor(kitType);
	}

	public void startGame() {
		if (sender != null && sender.isOnline() && receiver != null && receiver.isOnline()) {
			started = true;

			for (Item item : sender.getWorld().getEntitiesByClass(Item.class)) {
				item.remove();
			}
			for (Item item : receiver.getWorld().getEntitiesByClass(Item.class)) {
				item.remove();
			}

			prepareTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
				@Override
				public void run() {
					drawTimer();
					gameStatus = StatusEnum.START;

					checkItems(sender);
					checkItems(receiver);
					if (kitType.contains("Own Kit")) {
						return;
					}
					if (kitType.contains("Default")) {
						saveItems();
						clearInv();
						GameUtils.applyKit(sender, "Default");
						GameUtils.applyKit(receiver, "Default");
					} else if (kitType.contains("OP")) {
						saveItems();
						clearInv();
						GameUtils.applyKit(sender, "OP");
						GameUtils.applyKit(receiver, "OP");
					}

					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(StringUtils.format("    &a&l" + sender.getName() + " &avs &a&l" + receiver.getName() + "    "));
					Bukkit.broadcastMessage(StringUtils.format("                   &6Kit: " + kitType));
					Bukkit.broadcastMessage("");

					GameUtils.preparePlayers(sender, receiver);
					sender.teleport(locations[0]);
					receiver.teleport(locations[1]);
					sender.sendMessage(StringUtils.format("&aThe duel has started, good luck!"));
					receiver.sendMessage(StringUtils.format("&aThe duel has started, good luck!"));
					Bukkit.getScheduler().cancelTask(prepareTaskID);
				}
			}, 0, 20L);
		}
	}

	public void drawTimer() {
		drawTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
			private int countdownTime = 900;
			private boolean gameEnded = false;

			@Override
			public void run() {
				if (!gameEnded) {
					return;
				}

				if (countdownTime != 0) {
					countdownTime--;
				} else {
					if (gameStatus == StatusEnum.START) {
						gameStatus = StatusEnum.DRAW;
						sender.sendTitle(StringUtils.format("&6&lDraw"), StringUtils.format("&fNo one has won this duel!"));
						receiver.sendTitle(StringUtils.format("&6&lDraw"), StringUtils.format("&fNo one has won this duel!"));
						sender.playSound(sender.getLocation(), Sound.GHAST_MOAN, 0.5f, 5.0f);
						receiver.playSound(receiver.getLocation(), Sound.GHAST_MOAN, 0.5f, 5.0f);
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage(StringUtils.format("    &cThe duel is a draw!"));
						Bukkit.broadcastMessage("");

						new BukkitRunnable() {
							@Override
							public void run() {
								endGame(null, null);
							}
						}.runTaskLater(Main.getInstance(), 20 * 3);

						gameEnded = true;
					}
				}
			}
		}, 0, 20L);
	}

	public void endGame(Player winner, Player loser) {
		Bukkit.getScheduler().cancelTask(drawTaskID);
		sender.sendMessage("endGame used.");

		if (winner != null && loser != null) {
			winner.sendMessage(StringUtils.format("&aGG! You won the duel against " + loser.getName()));
			winner.playSound(winner.getLocation(), Sound.ENDERDRAGON_GROWL, 0.5f, 6.0f);

			loser.sendMessage(StringUtils.format("&cBetter luck next time! You lost the duel against " + winner.getName()));
			loser.playSound(loser.getLocation(), Sound.DONKEY_DEATH, 0.5f, 6.0f);

			winner.sendTitle(StringUtils.format("&a&l" + winner.getName()), StringUtils.format("&fWon the duel!"));
			loser.sendTitle(StringUtils.format("&c&l" + winner.getName()), StringUtils.format("&fWon the duel!"));

			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(StringUtils.format("    &a&l" + winner.getName() + " &awon the duel!"));
			Bukkit.broadcastMessage("");
		} else {
			drawTimer();
		}

		World world = sender.getWorld();
		sender.teleport(world.getSpawnLocation());
		receiver.teleport(world.getSpawnLocation());
		Main.getPlayersInDuel().remove(sender);
		Main.getPlayersInDuel().remove(receiver);
		Main.getPlayersInDuel().clear();

		if (kitType.contains("Own")) {
			return;
		}
		clearInv();
		restoreItems();
		checkKitItems(winner);
		checkKitItems(loser);

		started = false;
		gameStatus = null;
	}

	private void clearInv() {
		sender.getInventory().clear();
		sender.getInventory().setArmorContents(null);
		receiver.getInventory().clear();
		receiver.getInventory().setArmorContents(null);
	}

	private void saveItems() {
		invContents.put(sender.getName(), sender.getInventory().getContents().clone());
		armContents.put(sender.getName(), sender.getInventory().getArmorContents().clone());
		invContents.put(receiver.getName(), receiver.getInventory().getContents().clone());
		armContents.put(receiver.getName(), receiver.getInventory().getArmorContents().clone());
		clearInv();
	}

	private void restoreItems() {
		ItemStack[] senderContents = invContents.get(sender.getName());
		ItemStack[] senderArmor = armContents.get(sender.getName());
		ItemStack[] receiverContents = invContents.get(receiver.getName());
		ItemStack[] receiverArmor = armContents.get(receiver.getName());

		if(senderContents != null) {
			sender.getInventory().setContents(senderContents);
		} else if (senderArmor != null) {
			sender.getInventory().setArmorContents(senderArmor);
		}
		if(receiverContents != null) {
			receiver.getInventory().setContents(receiverContents);
		} else if (receiverArmor != null) {
			receiver.getInventory().setArmorContents(receiverArmor);
		}
	}

	private void checkKitItems(Player player) {
		final Inventory top = player.getOpenInventory().getTopInventory();

		if (top.getType() == InventoryType.CRAFTING) {
			top.clear();
		}

		if (player.getItemOnCursor() != null) {
			if (player.getItemOnCursor().getItemMeta() != null) {
				if (player.getItemOnCursor().getItemMeta().getDisplayName() != null) {
					if (player.getItemOnCursor().getItemMeta().getDisplayName().contains(ChatColor.stripColor("Default"))) {
						player.setItemOnCursor(null);
					} else if (player.getItemOnCursor().getItemMeta().getDisplayName().contains(ChatColor.stripColor("OP"))) {
						player.setItemOnCursor(null);
					}
				}
			}
		}

		GameUtils.removePotionEffects(player);
	}

	private void checkItems(Player player) {
		final Inventory top = player.getOpenInventory().getTopInventory();

		if (top.getType() == InventoryType.CRAFTING) {
			top.clear();
		}

		if (player.getItemOnCursor() != null) {
			if (player.getItemOnCursor().getType() != null) {
				player.setItemOnCursor(null);
			} else if (player.getItemOnCursor().getType() != null) {
				player.setItemOnCursor(null);
			}
		}
	}

	public static String getKitType() {
		return kitType;
	}
}