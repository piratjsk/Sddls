package net.piratjsk.sddls.mount;

import net.piratjsk.sddls.saddle.SignedSaddle;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Pig;

public class ProtectedPig extends GenericMount {

    public ProtectedPig(Pig pig) {
        super(pig);
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
    public boolean isProtectedFromPlayer(OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean isSaddled() {
        return false;
    }
}
