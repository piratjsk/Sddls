package net.piratjsk.sddls.mount;

import net.piratjsk.sddls.Sddls;
import net.piratjsk.sddls.saddle.SignedSaddle;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Pig;

public interface ProtectedMount {

    SignedSaddle getSaddle();

    Entity getEntity();

    boolean hasPassenger();

    boolean isProtectedFromEnvironment();

    boolean isProtectedFromPlayer(final OfflinePlayer player);

    boolean isSaddled();

    static ProtectedMount fromEntity(final Entity entity) {
        if (!Sddls.canBeProtected(entity.getType())) return new GenericProtectedMount(entity);
        if (entity instanceof Llama)
            return new ProtectedLlama((Llama) entity);
        if (entity instanceof AbstractHorse)
            return new ProtectedHorse((AbstractHorse) entity);
        if (entity.getType().equals(EntityType.PIG))
            return new ProtectedPig((Pig) entity);
        return new GenericProtectedMount(entity);
    }

}
