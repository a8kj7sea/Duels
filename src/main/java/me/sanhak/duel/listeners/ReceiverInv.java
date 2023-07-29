package me.sanhak.duel.listeners;

import me.sanhak.duel.Main;
import me.sanhak.duel.manager.Game;
import me.sanhak.duel.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ReceiverInv implements Listener {
	boolean accepted = false;

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player receiver = (Player) e.getWhoClicked();
		InventoryView invView = e.getView();

		if (invView.getTitle().contains("duel you")) {
			e.setCancelled(true);

			String[] titleWords = invView.getTitle().split(" ");
			String senderName = titleWords[0];
			Player sender = Bukkit.getPlayer(senderName);

			ItemStack duelInfo = invView.getItem(13);
			ItemMeta meta = duelInfo.getItemMeta();
			List<String> lore = meta.getLore();
			String loreLine = lore.get(0);
			String[] duelType = loreLine.split(" ");
			String kitType = duelType[2];

			if (sender != null && sender.isOnline()) {
				if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null) {
					if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
						String displayName = e.getCurrentItem().getItemMeta().getDisplayName();
						if (displayName.equalsIgnoreCase(StringUtils.format("&aAccept"))) {
							Main.getPlayersInDuel().put(sender, receiver);
							Game game = new Game(sender, receiver, kitType);
							game.startGame();
							receiver.closeInventory();
							accepted = true;
						} else if (displayName.equalsIgnoreCase(StringUtils.format("&cDeny"))) {
							sender.playSound(sender.getLocation(), Sound.VILLAGER_NO, 0.5f, 5.0f);
							receiver.closeInventory();
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onCloseInventory(InventoryCloseEvent e) {
		Player receiver = (Player) e.getPlayer();
		InventoryView invView = e.getView();

		if (invView.getTitle().contains("duel you")) {
			String[] titleWords = invView.getTitle().split(" ");
			String senderName = titleWords[0];

			Player sender = Bukkit.getPlayer(senderName);

			if (sender != null) {
				if(!accepted) {
					receiver.sendMessage(StringUtils.format("&cYou have cancelled the duel request!"));
					sender.sendMessage(StringUtils.format("&f&l" + receiver.getName() + " &chas denied your duel request!"));
					sender.playSound(sender.getLocation(), Sound.VILLAGER_NO, 0.5f, 5.0f);
				}
			}
		}
	}
}