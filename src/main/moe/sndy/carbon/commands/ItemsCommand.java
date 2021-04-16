package moe.sndy.carbon.commands;

import moe.sndy.carbon.Carbon;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemsCommand implements CommandExecutor {

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1 && args[0].equals("reload")) {
            Carbon.getPlugin().getDataManager().getItemLoader().reload();
        }
        if (args.length == 2 && args[0].equals("drops")) {
            if (sender instanceof Player) {
                ((Player) sender).getInventory().addItem(Carbon.getPlugin().getDataManager().getItemLoader().getDrops(args[1]));
            } else {
                for (ItemStack item : Carbon.getPlugin().getDataManager().getItemLoader().getDrops(args[1])) {
                    sender.sendMessage(ChatColor.GREEN + " + " + item.getItemMeta().getDisplayName());
                }
            }
            sender.sendMessage(ChatColor.GREEN + "Received drops from loot table '" + ChatColor.GOLD + args[1] + ChatColor.GREEN + "'.");
        }
        return true;
    }
}
