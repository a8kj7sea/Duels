package me.sanhak.duel.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import me.sanhak.duel.itemstack.ItemCreator;
import java.util.Collections;

public class ReceiverInventory {
	public static Inventory createReceiverInventory(Player sender, String kit) {
		Inventory inv = Bukkit.createInventory(null, 3 * 9, sender.getName() + " wants to duel you");
		inv.setItem(13, ItemCreator.createDescriptionItem("&fPlayer: &e" + sender.getName(), Material.BOOK, Collections.singletonList("&fKit Type: &6" + kit)));
		inv.setItem(11, ItemCreator.createColorWoolItem("&aAccept", 5));
		inv.setItem(15, ItemCreator.createColorWoolItem("&cDeny", 14));
		return inv;
	}
}