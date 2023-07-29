package me.sanhak.duel.inventory;

import me.sanhak.duel.itemstack.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Collections;

public class KitSelectInventory {
    public static Inventory selectKit(Player player) {
        Inventory inv = Bukkit.createInventory(null, 3 * 9, "Kit Selector");
        inv.setItem(11, ItemCreator.createDescriptionItem("&6Your Own Items", Material.BOOK, Collections.singletonList("&7Bring any of your own items.")));
        inv.setItem(13, ItemCreator.createDescriptionItem("&6Normal Kit", Material.BOOK,
                Arrays.asList(
                        "&e5x Diamond Helmets &7(Protection IV Unbreaking III)",
                        "&e4x Diamond Chestplates &7(Protection IV Unbreaking III)",
                        "&e4x Diamond Leggings &7(Protection IV Unbreaking III)",
                        "&e5x Diamond Boots &7(Protection IV Unbreaking III)",
                        "&e1x Diamond Sword &7(Sharpness V Unbreaking III Fire Aspect II)",
                        "&e3x Strength I",
                        "&e3x Speed I",
                        "&e32x Enchanted Golden Apples"
                )));
        inv.setItem(15, ItemCreator.createDescriptionItem("&6OP Kit", Material.BOOK,
                Arrays.asList(
                        "&e5x Diamond Helmets &7(Protection V Unbreaking V)",
                        "&e4x Diamond Chestplates &7(Protection V Unbreaking V)",
                        "&e4x Diamond Leggings &7(Protection V Unbreaking V)",
                        "&e5x Diamond Boots &7(Protection V Unbreaking V)",
                        "&e1x Diamond Sword &7(Sharpness V Unbreaking III Fire Aspect II)",
                        "&e6x Strength I",
                        "&e6x Speed I",
                        "&e64x Enchanted Golden Apples"
                )));
        inv.setItem(26, ItemCreator.createItemWithName("&a&lYou are dueling " + player.getName(), Material.PAPER));
        return inv;
    }
}