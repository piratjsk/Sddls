package net.piratjsk.sddls.listeners;

import net.piratjsk.sddls.ProtectedMount;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import net.piratjsk.sddls.Sddls;
import org.bukkit.projectiles.ProjectileSource;

public final class MountListener implements Listener {

    private final Sddls sddls;

    public MountListener(final Sddls sddlsPlugin) {
        this.sddls = sddlsPlugin;
    }

    @EventHandler
    public void onHorseAccess(final PlayerInteractEntityEvent event) {
        if (!Sddls.canBeProtected(event.getRightClicked().getType())) return;
        final ProtectedMount mount = ProtectedMount.fromEntity(event.getRightClicked());
        if (mount.isProtectedFromPlayer(event.getPlayer())) {
            event.setCancelled(true);
            this.sddls.sendNoAccessMessage(event.getPlayer(),mount);
        }
    }

    @EventHandler
    public void onHorseDamage(final EntityDamageEvent event) {
        if (!Sddls.canBeProtected(event.getEntityType())) return;
        final ProtectedMount mount = ProtectedMount.fromEntity(event.getEntity());
        final Player damager = this.getPlayerDamager(event);
        if (damager != null) {
            if (mount.isProtectedFromPlayer(damager)) {
                event.setCancelled(true);
            }
        }

        if (mount.isProtectedFromEnivroment()) {
            event.setCancelled(true);
        }
    }

    private Player getPlayerDamager(final EntityDamageEvent event) {
        if (!(event instanceof EntityDamageByEntityEvent)) return null;
        final EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
        final Entity damager = damageEvent.getDamager();
        if (damager instanceof Player) {
            return (Player) damager;
        }
        if (damager instanceof Projectile) {
            final ProjectileSource shooter = ((Projectile) damager).getShooter();
            if (shooter instanceof Player)
                return (Player) shooter;
        }
        return null;
    }

}
