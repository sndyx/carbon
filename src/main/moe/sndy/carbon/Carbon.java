package moe.sndy.carbon;

import moe.sndy.carbon.commands.CommandHandler;
import moe.sndy.carbon.data.DataManager;
import moe.sndy.carbon.listeners.ListenerHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Carbon extends JavaPlugin {

    private static Carbon plugin;
    private DataManager dataManager;
    private ListenerHandler listenerHandler;
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
        plugin = this;
        dataManager = new DataManager();
        listenerHandler = new ListenerHandler();
        listenerHandler.registerListeners();
        commandHandler = new CommandHandler();
        commandHandler.registerCommands();
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

    public DataManager getDataManager() {
        return dataManager;
    }

}