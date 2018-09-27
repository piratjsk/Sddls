package net.piratjsk.sddls.listeners;

import net.piratjsk.sddls.mount.ProtectedMount;
import net.piratjsk.sddls.signature.Signature;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import net.piratjsk.sddls.Sddls;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

import java.util.UUID;
import java.util.stream.Collectors;

import static net.piratjsk.sddls.utils.StringUtils.capitalizeFirstLetter;

public final class MountListener implements Listener {

    @EventHandler
    public void onMountAccess(final PlayerInteractEntityEvent event) {
        if (!Sddls.canBeProtected(event.getRightClicked().getType())) return;
        final ProtectedMount mount = ProtectedMount.fromEntity(event.getRightClicked());
        if (mount.isProtectedFromPlayer(event.getPlayer())) {
            event.setCancelled(true);
            sendNoAccessMessage(event.getPlayer(), mount);
        }
    }

    @EventHandler
    public void onMountDamage(final EntityDamageEvent event) {
        if (!Sddls.canBeProtected(event.getEntityType())) return;
        final ProtectedMount mount = ProtectedMount.fromEntity(event.getEntity());
        final Damager damager = getDamager(event);
        if (shouldBeProtected(mount, damager))
            event.setCancelled(true);
    }

    private boolean shouldBeProtected(final ProtectedMount mount, final Damager damager) {
        if (damager.isPlayer()) {
            final Player player = getPlayer(damager);
            return mount.isProtectedFromPlayer(player);
        }
        return mount.isProtectedFromEnvironment();
    }

    private Player getPlayer(final Damager damager) {
        return Bukkit.getPlayer(damager.uuid);
    }

    private void sendNoAccessMessage(final Player player, final ProtectedMount mount) {
        final Configuration config = Sddls.getInstance().getConfig();
        final Entity entity = mount.getEntity();
        final String type = entity.getType().toString().toLowerCase().replaceAll("_", "-");
        final String name = entity.getCustomName() != null ? entity.getCustomName() : type;
        final String players = mount.getSaddle().getSignatures().stream()
                .map(Signature::getName)
                .collect(Collectors.joining(", "));
        final String defaultMessage = config.getString("no-access-msg", "&6[Sddls] &7You don't have access to this %mount%.");
        final String message = config.getString("no-access-msg-" + type, defaultMessage)
                .replaceAll("%mount%", type)
                .replaceAll("%Mount%", capitalizeFirstLetter(type))
                .replaceAll("%name%", name)
                .replaceAll("%Name%", capitalizeFirstLetter(name))
                .replaceAll("%players%", players);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private Damager getDamager(final EntityDamageEvent event) {
        if (!(event instanceof EntityDamageByEntityEvent)) return new Damager();
        final EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
        final Entity damager = damageEvent.getDamager();
        if (damager instanceof Projectile) {
            final ProjectileSource shooter = ((Projectile) damager).getShooter();
            return new Damager((Entity) shooter);
        }
        return new Damager(damageEvent.getDamager());
    }

    private final class Damager {
        private final EntityType type;
        private final UUID uuid;
        Damager() {
            this.type = null;
            this.uuid = null;
        }
        Damager(final Entity entity) {
            this.type = entity.getType();
            this.uuid = entity.getUniqueId();
        }
        boolean isPlayer() {
            return type == EntityType.PLAYER;
        }
    }

}
