package me.sanhak.duel.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.sanhak.duel.configuration.LocationUtils;
import me.sanhak.duel.utils.StringUtils;

public class SetupInv implements Listener {

	private static final String SETUP_TITLE = StringUtils.format("&7Duels Setup");
	private static final String FIRST_LOCATION_TITLE = StringUtils.format("&b1st Location");
	private static final String SECOND_LOCATION_TITLE = StringUtils.format("&b2nd Location");
	private static final String FINISH_SETUP_TITLE = StringUtils.format("&aFinish Setup");

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player admin = (Player) e.getWhoClicked();
		if (!admin.hasPermission("1v1.setup"))
			return;

		Inventory eventInventory = e.getInventory();
		if (!eventInventory.getTitle().equalsIgnoreCase(SETUP_TITLE))
			return;

		e.setCancelled(true);
		ItemStack item = e.getCurrentItem();
		if (item == null || item.getItemMeta() == null)
			return;

		String displayName = StringUtils.format(item.getItemMeta().getDisplayName());
		if (displayName.equalsIgnoreCase(FIRST_LOCATION_TITLE)) {
			admin.sendMessage(StringUtils.format("&aYou have set the &b1st Location &aSuccessfully!"));
			LocationUtils.setFirstLocation(admin.getWorld().getName(), admin.getLocation().getBlockX(), admin.getLocation().getBlockY(), admin.getLocation().getBlockZ());
			admin.playSound(admin.getLocation(), Sound.LEVEL_UP, 0.5f, 6.0f);
		} else if (displayName.equalsIgnoreCase(SECOND_LOCATION_TITLE)) {
			admin.sendMessage(StringUtils.format("&aYou have set the &b2nd Location &aSuccessfully!"));
			LocationUtils.setSecondLocation(admin.getWorld().getName(), admin.getLocation().getBlockX(), admin.getLocation().getBlockY(), admin.getLocation().getBlockZ());
			admin.playSound(admin.getLocation(), Sound.LEVEL_UP, 0.5f, 6.0f);
		} else if (displayName.equalsIgnoreCase(FINISH_SETUP_TITLE)) {
			admin.playSound(admin.getLocation(), Sound.LEVEL_UP, 0.5f, 6.0f);
			admin.closeInventory();
		}
	}

	@EventHandler
	public void onCloseInventory(InventoryCloseEvent e) {
		Player admin = (Player) e.getPlayer();
		Inventory inv = e.getInventory();

		if (inv.getTitle().equals(SETUP_TITLE)) {
			admin.sendMessage(StringUtils.format("&aYour duels have been setup!"));
			admin.playSound(admin.getLocation(), Sound.LEVEL_UP, 0.5f, 6.0f);
		}
	}
}