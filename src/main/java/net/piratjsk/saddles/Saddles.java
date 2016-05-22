package net.piratjsk.saddles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import org.mcstats.MetricsLite;

import net.piratjsk.saddles.listeners.HorseListener;
import net.piratjsk.saddles.listeners.SigningListener;

public final class Saddles extends JavaPlugin {

    public static String msg;

    @Override
    public void onEnable() {
        // config
        saveDefaultConfig();
        loadConfig();

        // 'saddles' command
        new SaddlesCommand(this);

        // listeners
        getServer().getPluginManager().registerEvents(new SigningListener(), this);
        getServer().getPluginManager().registerEvents(new HorseListener(), this);
        // register 'sign saddle' recipe
        final ShapelessRecipe recipe = new ShapelessRecipe(new ItemStack(Material.SADDLE));
        recipe.addIngredient(Material.SADDLE);
        getServer().addRecipe(recipe);

        // plugin metrics
        try {
            final MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (final IOException ignored) {}
    }

    public void loadConfig() {
        msg = this.getConfig().getString("msg");
    }

    public static ItemStack signSaddle(final ItemStack saddle, final Player player) {
        final ItemMeta meta = saddle.getItemMeta();
        final ArrayList<String> lore = meta.hasLore() ? (ArrayList<String>) meta.getLore() : new ArrayList<String>();
        if (isSigned(saddle)) {
            boolean cleaned = false;
            for (final UUID signature : Saddles.getSignatures(saddle)) {
                if (signature.equals(player.getUniqueId())) {
                    for (final Object line : lore.toArray())
                        if (isUUID(line.toString()))
                            lore.remove(line);
                    cleaned = true;
                    break;
                }
            }
            if (!cleaned) lore.add(player.getUniqueId().toString());
        } else {
            lore.add(player.getUniqueId().toString());
        }
        meta.setLore(lore);
        saddle.setItemMeta(meta);
        return saddle;
    }

    public static boolean hasAccess(final ItemStack saddle, final Entity entity) {
        if (!(entity instanceof Player)) return false;
        if (isSigned(saddle)) {
            for (final UUID signature : getSignatures(saddle))
                if (signature.equals(entity.getUniqueId()))
                    return true;
            return false;
        }
        return true;
    }

    public static OfflinePlayer getOwner(final ItemStack saddle) {
        if (isSigned(saddle))
            return Bukkit.getOfflinePlayer(getSignatures(saddle).get(0));
        return null;
    }

    public static boolean isSigned(final ItemStack saddle) {
        if (saddle.getItemMeta().hasLore())
            for (String line : saddle.getItemMeta().getLore())
                if (isUUID(line))
                    return true;
        return false;
    }

    public static List<UUID> getSignatures(final ItemStack saddle) {
        final List<UUID> signatures = new ArrayList<UUID>();
        if (saddle.getItemMeta().hasLore())
            for (final String line : saddle.getItemMeta().getLore())
                if (isUUID(line))
                    signatures.add(UUID.fromString(line));
        return signatures;
    }

    private static boolean isUUID(final String uuid) {
        return uuid.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}");
    }

}
