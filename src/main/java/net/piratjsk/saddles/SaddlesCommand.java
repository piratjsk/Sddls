package net.piratjsk.saddles;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public final class SaddlesCommand implements CommandExecutor {

    private final Saddles plugin;

    public SaddlesCommand(Saddles plugin) {
        this.plugin = plugin;
        plugin.getCommand("saddles").setExecutor(this);
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("saddles")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")){
                if (sender instanceof ConsoleCommandSender || sender.hasPermission("saddles.reload")) {
                    plugin.reloadConfig();
                    plugin.loadConfig();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[Saddles] &rReloaded!"));
                    return true;
                }
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[Saddles] &rv"+plugin.getDescription().getVersion()));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&r"+plugin.getDescription().getDescription()));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7"+plugin.getDescription().getWebsite()));
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("saddles.reload"))
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/saddles reload &r- reloads config"));
            return true;
        }
        return false;
    }

}
