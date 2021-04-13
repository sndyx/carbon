package moe.sndy.carbon;

import moe.sndy.carbon.data.DataManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Carbon extends JavaPlugin {

    private static Carbon plugin;
    private DataManager dataManager;

    @Override
    public void onEnable() {
        plugin = this;
        dataManager = new DataManager();
        // TODO: Enable modules
    }

    @Override
    public void onDisable() {
        dataManager.end();
    }

    public static Logger logger() {
        return plugin.getLogger();
    }

    public static Carbon getPlugin() {
        return plugin;
    }

}