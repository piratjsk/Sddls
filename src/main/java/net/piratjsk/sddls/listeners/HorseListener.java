package net.piratjsk.sddls.listeners;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import net.piratjsk.sddls.Sddls;

public final class HorseListener implements Listener {

    @EventHandler
    public void onHorseAccess(final PlayerInteractEntityEvent event) {
        if (this.isHorse(event.getRightClicked())) {
            final ItemStack saddle = this.getSaddle(event.getRightClicked());
            if (saddle!=null && Sddls.isSigned(saddle)) {
                if (!Sddls.hasAccess(saddle, event.getPlayer())) {
                    event.setCancelled(true);
                    final OfflinePlayer owner = Sddls.getOwner(saddle);
                    String name = "undefined";
                    if (owner!=null) {
                        if (owner.hasPlayedBefore()) {
                            name = owner.getName();
                            if (owner.isOnline())
                                name = ((Player) owner).getDisplayName();
                        } else {
                            name = owner.getUniqueId().toString();
                        }
                    }
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Sddls.noAccessMsg.replaceAll("%owner%", name)));
                }
            }
        }
    }

    @EventHandler
    public void onHorseDamage(final EntityDamageEvent event) {
        if (this.isHorse(event.getEntity())) {
            final ItemStack saddle = this.getSaddle(event.getEntity());
            if (saddle!=null && Sddls.isSigned(saddle)) {
                Entity damager = null;
                if (event instanceof EntityDamageByEntityEvent) {
                    damager = ((EntityDamageByEntityEvent) event).getDamager();
                    if (damager instanceof Projectile)
                        if (((Projectile)damager).getShooter() instanceof Player)
                            damager = (Player) ((Projectile) damager).getShooter();
                    if (!(damager instanceof Player)) damager = null;
                }
                if (damager!=null) {
                    if (!Sddls.hasAccess(saddle, damager))
                        event.setCancelled(true);
                } else {
                    if (event.getEntity().getPassenger()==null)
                        event.setCancelled(true);
                }
            }
        }
    }

    private boolean isHorse(final Entity entity) {
        final EntityType type = entity.getType();
        return     type.equals(EntityType.HORSE)
                || type.equals(EntityType.SKELETON_HORSE)
                || type.equals(EntityType.ZOMBIE_HORSE)
                || type.equals(EntityType.DONKEY)
                || type.equals(EntityType.MULE);
    }

    private ItemStack getSaddle(final Entity entity) {
        if (!this.isHorse(entity)) return null;
        final Inventory inv = ((InventoryHolder) entity).getInventory();;
        if (inv instanceof HorseInventory)
            return ((HorseInventory) inv).getSaddle();
        return inv.getItem(0);
    }

}
