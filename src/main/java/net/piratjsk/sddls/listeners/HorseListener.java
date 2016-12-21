package net.piratjsk.sddls.listeners;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import net.piratjsk.sddls.Sddls;

public final class HorseListener implements Listener {

    @EventHandler
    public void onHorseAccess(final PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Horse) {
            final Horse horse = (Horse) event.getRightClicked();
            final ItemStack saddle = horse.getInventory().getSaddle();
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
        if (event.getEntityType().equals(EntityType.HORSE)) {
            final Horse horse = (Horse) event.getEntity();
            final ItemStack saddle = horse.getInventory().getSaddle();
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
                    if (horse.getPassenger()==null)
                        event.setCancelled(true);
                }
            }
        }
    }

}
