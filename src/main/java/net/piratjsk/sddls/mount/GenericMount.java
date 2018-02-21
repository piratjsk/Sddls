package net.piratjsk.sddls.mount;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.util.UUID;

public abstract class GenericMount implements ProtectedMount {

    private final UUID entity;

    public GenericMount(final Entity entity) {
        this.entity = entity.getUniqueId();
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
