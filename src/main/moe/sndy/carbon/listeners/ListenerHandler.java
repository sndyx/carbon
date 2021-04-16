package moe.sndy.carbon.listeners;

import moe.sndy.carbon.Carbon;
import org.bukkit.Bukkit;

public class ListenerHandler {

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(Carbon.getPlugin().getDataManager().getProfileManager(), Carbon.getPlugin());
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), Carbon.getPlugin());
    }

}
