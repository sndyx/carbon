package moe.sndy.carbon.commands;

import moe.sndy.carbon.Carbon;

public class CommandHandler {

    public void registerCommands() {
        Carbon.getPlugin().getCommand("items").setExecutor(new ItemsCommand());
    }

}
