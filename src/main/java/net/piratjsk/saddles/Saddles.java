package net.piratjsk.saddles;

import java.util.ArrayList;
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

import net.piratjsk.saddles.listeners.HorseListener;
import net.piratjsk.saddles.listeners.SigningListener;

public class Saddles extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SigningListener(), this);
        getServer().getPluginManager().registerEvents(new HorseListener(), this);
        // register 'sign saddle' recipe
        final ShapelessRecipe recipe = new ShapelessRecipe(new ItemStack(Material.SADDLE));
        recipe.addIngredient(Material.SADDLE);
        getServer().addRecipe(recipe);
    }

    public static ItemStack signSaddle(final ItemStack saddle, final Player player) {
        final ItemMeta meta = saddle.getItemMeta();
        ArrayList<String> lore = meta.hasLore() ? (ArrayList<String>) meta.getLore() : new ArrayList<String>();
        if (lore.size()>0) {
            for (String line : lore)
                if (line.equals(player.getUniqueId().toString()))
                    lore = null;
            if (lore!=null) lore.add(player.getUniqueId().toString());
        } else {
            lore.add(player.getUniqueId().toString());
        }
        meta.setLore(lore);
        saddle.setItemMeta(meta);
        return saddle;
    }

    public static boolean hasAccess(final ItemStack saddle, final Entity entity) {
        if (!(entity instanceof Player)) return false;
        if (!saddle.getItemMeta().hasLore())
            return true;
        for (final String line : saddle.getItemMeta().getLore()) {
            if (line.equals(entity.getUniqueId().toString()))
                return true;
        }
        return false;
    }

    public static OfflinePlayer getOwner(final  ItemStack saddle) {
        return Bukkit.getOfflinePlayer(UUID.fromString(saddle.getItemMeta().getLore().get(0)));
    }

}
