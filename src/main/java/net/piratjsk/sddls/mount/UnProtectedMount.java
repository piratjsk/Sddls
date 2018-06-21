package net.piratjsk.sddls.mount;

import net.piratjsk.sddls.saddle.SignedSaddle;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;

public class UnProtectedMount extends GenericMount {

    public UnProtectedMount(final Entity entity) {
        super(entity);
    }

    @Override
    public SignedSaddle getSaddle() {
        return null;
    }

    @Override
    public boolean isProtectedFromEnvironment() {
        return false;
    }

    @Override
    public boolean isProtectedFromPlayer(final OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean isSaddled() {
        return false;
    }

}
