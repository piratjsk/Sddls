package net.piratjsk.sddls;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

public final class SddlsCommand implements CommandExecutor {

    private static final String TAG = "&6[Sddls] &r";
    private final Sddls sddls;

    public SddlsCommand(final Sddls sddlsPlugin) {
        this.sddls = sddlsPlugin;
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")){
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("sddls.reload")) {
                sddls.reloadConfig();
                sendMsg(sender, "Reloaded!");
                return true;
            }
        }
        this.sendPluginInfo(sender);
        if (sender.hasPermission("sddls.reload"))
            sendMsg(sender, "/sddls reload &7- reloads config");
        return true;
    }

    private void sendPluginInfo(final CommandSender recipient) {
        final PluginDescriptionFile info = sddls.getDescription();
        sendMsg(recipient, info.getFullName());
        sendMsg(recipient, "by " + String.join(", ", info.getAuthors()));
        sendMsg(recipient, info.getDescription());
        sendMsg(recipient, "&7"+ info.getWebsite());
    }

    private static void sendMsg(final CommandSender recipient, final String message) {
        recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', TAG + message));
    }

}
