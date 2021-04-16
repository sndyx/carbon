package moe.sndy.carbon.listeners;

import moe.sndy.carbon.data.items.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void inventoryClick(PlayerInteractEvent event) {
        if (event.getItem() != null && ItemUtils.getItemId(event.getItem()).equals("menu")) {
            // TODO: Menu GUI
            // event.getPlayer().openInventory();
        }
    }

}
