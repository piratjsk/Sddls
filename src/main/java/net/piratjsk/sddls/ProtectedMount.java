package net.piratjsk.sddls;

import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ProtectedMount {

    private final UUID entity;
    private final Saddle saddle;

    private ProtectedMount(final UUID entity, final Saddle saddle) {
        this.entity = entity;
        this.saddle = saddle;
    }

    public Saddle getSaddle() {
        return this.saddle;
    }

    public Entity getEntity() {
        return Bukkit.getEntity(entity);
    }

    public boolean hasPassenger() {
        return !this.getEntity().getPassengers().isEmpty();
    }

    public boolean isProtectedFromEnivroment() {
        if (!this.isSaddled()) return false;
        if (!this.saddle.isSigned()) return false;
        return !this.hasPassenger();
    }

    public boolean isProtectedFromPlayer(final Player player) {
        if (player.hasPermission("sddls.bypass")) return false;
        if (!this.isSaddled()) return false;
        if (!this.saddle.isSigned()) return false;
        return !this.saddle.isSignedBy(player);
    }

    public boolean isSaddled() {
        return this.saddle != null;
    }

    public static ProtectedMount fromEntity(final Entity entity) {
        if (!Sddls.canBeProtected(entity.getType())) return null;
        final UUID uuid = entity.getUniqueId();
        Saddle saddle = null;
        if (entity instanceof AbstractHorse) {
            final AbstractHorse mount = (AbstractHorse) entity;
            final ItemStack saddleItem = mount.getInventory().getSaddle();
            saddle = Saddle.fromItemStack(saddleItem);
        }
        if (entity instanceof Pig) {
            // TODO: handle pigs
        }
        return new ProtectedMount(uuid, saddle);
    }

}
