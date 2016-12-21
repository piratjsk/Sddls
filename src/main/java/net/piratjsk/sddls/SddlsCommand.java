package net.piratjsk.sddls;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public final class SddlsCommand implements CommandExecutor {

    private final Sddls plugin;

    public SddlsCommand(Sddls plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")){
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("sddls.reload")) {
                plugin.reloadConfig();
                plugin.loadConfig();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[Sddls] &rReloaded!"));
                return true;
            }
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[Sddls] &rv"+plugin.getDescription().getVersion()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&r"+plugin.getDescription().getDescription()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7"+plugin.getDescription().getWebsite()));
        if (sender instanceof ConsoleCommandSender || sender.hasPermission("sddls.reload"))
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/sddls reload &r- reloads config"));
        return true;
    }

}
