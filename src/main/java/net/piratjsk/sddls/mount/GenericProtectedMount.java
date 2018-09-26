package net.piratjsk.sddls.mount;

import net.piratjsk.sddls.saddle.SignedSaddle;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class GenericProtectedMount implements ProtectedMount {

    private final UUID entity;
    protected SignedSaddle saddle;

    public GenericProtectedMount(final Entity entity) {
        this.entity = entity.getUniqueId();
    }

    @Override
    public SignedSaddle getSaddle() {
        return this.saddle;
    }

    @Override
    public boolean isProtectedFromEnvironment() {
        if (!this.isSaddled()) return false;
        if (!this.saddle.isSigned()) return false;
        return !this.hasPassenger();
    }

    @Override
    public boolean isProtectedFromPlayer(final OfflinePlayer player) {
        if (!this.isSaddled()) return false;
        if (player.isOnline() && player.getPlayer().hasPermission("sddls.bypass")) return false;
        return !this.saddle.isSignedBy(player);
    }

    @Override
    public boolean isSaddled() {
        return this.saddle != null;
    }

    @Override
    public Entity getEntity() {
        return Bukkit.getEntity(this.entity);
    }

    @Override
    public boolean hasPassenger() {
        return !this.getEntity().getPassengers().isEmpty();
    }
}
