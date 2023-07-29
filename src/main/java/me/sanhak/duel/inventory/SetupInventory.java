package me.sanhak.duel.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import me.sanhak.duel.itemstack.ItemCreator;
import me.sanhak.duel.utils.StringUtils;

public class SetupInventory {
	public static Inventory createSetupInventory() {
		Inventory inv = Bukkit.createInventory(null, 3 * 9, StringUtils.format("&7Duels Setup"));
		inv.setItem(11, ItemCreator.createPlayersLocationItem("&b1st Location",
				"&fTo set the 1st duel spawn point &b&l(Click here)", Material.EGG, 1));
		inv.setItem(15, ItemCreator.createPlayersLocationItem("&b2nd Location",
				"&fTo set the 2nd duel spawn point &b&l(Click here)", Material.EGG, 2));
		inv.setItem(26, ItemCreator.createColorWoolItem("&aFinish Setup", 5));
		return inv;
	}
}