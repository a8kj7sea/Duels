package me.sanhak.duel.itemstack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.sanhak.duel.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ItemCreator {

	public static ItemStack createItemWithName(String displayName, Material itemType) {
		ItemStack itemStack = new ItemStack(itemType);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(StringUtils.format(displayName));
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack createDescriptionItem(String displayName, Material itemType, List<String> lore) {
		ItemStack itemStack = new ItemStack(itemType);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(StringUtils.format(displayName));
		List<String> formattedLore = new ArrayList<>();
		for (String line : lore) {
			formattedLore.add(StringUtils.format(line));
		}
		itemMeta.setLore(formattedLore);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack createPlayersLocationItem(String displayName, String description, Material itemType, int amount) {
		ItemStack itemStack = new ItemStack(itemType, amount);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(StringUtils.format(displayName));
		List<String> lores = new ArrayList<>();
		lores.add(StringUtils.format(description));
		itemMeta.setLore(lores);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack createColorWoolItem(String displayName, int color) {
		ItemStack itemStack = new ItemStack(Material.WOOL, 1, (short) color);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(StringUtils.format(displayName));
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
}