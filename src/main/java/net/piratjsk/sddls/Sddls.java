package net.piratjsk.sddls;

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

import net.piratjsk.sddls.listeners.HorseListener;
import net.piratjsk.sddls.listeners.SigningListener;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public final class Sddls extends JavaPlugin {

    public final static ShapelessRecipe recipe = new ShapelessRecipe(new ItemStack(Material.SADDLE)).addIngredient(Material.SADDLE);

    @Override
    public void onEnable() {
        this.setupConfig();
        this.registerCommand();
        this.registerListeners();
        this.registerRecipe();
    }

    private void setupConfig() {
        this.saveDefaultConfig();
    }

    private void registerCommand() {
        this.getCommand("sddls").setExecutor(new SddlsCommand(this));
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new SigningListener(), this);
        this.getServer().getPluginManager().registerEvents(new HorseListener(), this);
    }

    private void registerRecipe() {
        this.getServer().addRecipe(recipe);
    }
    
    public static boolean isHorse(final Entity entity) {
        final EntityType type = entity.getType();
        return     type.equals(EntityType.HORSE)
                || type.equals(EntityType.SKELETON_HORSE)
                || type.equals(EntityType.ZOMBIE_HORSE)
                || type.equals(EntityType.DONKEY)
                || type.equals(EntityType.MULE);
    }

    public static ItemStack getSaddle(final Entity entity) {
        if (!Sddls.isHorse(entity)) return null;
        final Inventory inv = ((InventoryHolder) entity).getInventory();;
        if (inv instanceof HorseInventory)
            return ((HorseInventory) inv).getSaddle();
        return inv.getItem(0);
    }

    public static ItemStack signSaddle(final ItemStack saddle, final Player player) {
        final ItemMeta meta = saddle.getItemMeta();
        final ArrayList<String> lore = meta.hasLore() ? (ArrayList<String>) meta.getLore() : new ArrayList<String>();
        if (isSigned(saddle)) {
            boolean cleaned = false;
            for (final UUID signature : Sddls.getSignatures(saddle)) {
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
        if (entity.hasPermission("sddls.bypass")) return true;
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
