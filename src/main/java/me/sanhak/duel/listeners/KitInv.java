package me.sanhak.duel.listeners;

import me.sanhak.duel.inventory.ReceiverInventory;
import me.sanhak.duel.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KitInv implements Listener {
    boolean selected = false;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player sender = (Player) e.getWhoClicked();
        Inventory inventory = e.getInventory();
        int slot = e.getRawSlot();

        if (!inventory.getTitle().equalsIgnoreCase("Kit Selector")) {
            return;
        }

        e.setCancelled(true);

        if (slot != 26) {
            return;
        }

        ItemStack receiverItem = inventory.getItem(slot);

        if (receiverItem == null) {
            return;
        }

        ItemMeta meta = receiverItem.getItemMeta();
        String displayName = meta.getDisplayName();

        if (!displayName.contains("dueling")) {
            return;
        }

        String[] duelPlayer = displayName.split(" ");
        String receiverName = duelPlayer[3];
        Player receiver = Bukkit.getPlayer(receiverName);

        switch (slot) {
            case 11:
                sender.closeInventory();
                receiver.openInventory(ReceiverInventory.createReceiverInventory(sender, "Own Kit"));
                sender.sendMessage(StringUtils.format("&aDuel Request has been sent to " + receiver.getName() + "!"));
                receiver.playSound(receiver.getLocation(), Sound.BURP, 0.5f, 5.0f);
                selected = true;
                break;
            case 13:
                sender.closeInventory();
                receiver.openInventory(ReceiverInventory.createReceiverInventory(sender, "Default"));
                sender.sendMessage(StringUtils.format("&aDuel Request has been sent to " + receiver.getName() + "!"));
                sender.playSound(receiver.getLocation(), Sound.BURP, 0.5f, 5.0f);
                selected = true;
                break;
            case 15:
                sender.closeInventory();
                receiver.openInventory(ReceiverInventory.createReceiverInventory(sender, "OP"));
                sender.sendMessage(StringUtils.format("&aDuel Request has been sent to " + receiver.getName() + "!"));
                sender.playSound(receiver.getLocation(), Sound.BURP, 0.5f, 5.0f);
                selected = true;
                break;
        }
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();

        if (!inventory.getTitle().equalsIgnoreCase("Kit Selector")) {
            return;
        }

        if (!selected) {
            Player player = (Player) e.getPlayer();
            player.sendMessage(StringUtils.format("&cYou have cancelled the duel request!"));
        }
    }
}