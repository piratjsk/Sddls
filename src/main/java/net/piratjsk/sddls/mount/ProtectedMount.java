package net.piratjsk.sddls.mount;

import net.piratjsk.sddls.saddle.SignedSaddle;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;

public interface ProtectedMount {

    /**
     * @return SignedSaddle object or null (if isSaddled() returns false)
     */
    SignedSaddle getSaddle();

    Entity getEntity();

    boolean hasPassenger();

    boolean isProtectedFromEnvironment();

    boolean isProtectedFromPlayer(final OfflinePlayer player);

    boolean isSaddled();

    static ProtectedMount fromEntity(final Entity entity) {
        if (entity instanceof AbstractHorse) {
            final AbstractHorse horse = (AbstractHorse) entity;
            return new ProtectedHorse(horse);
        } else {
            return new UnProtectedMount(entity);
        }
    }

}
