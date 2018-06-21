package net.piratjsk.sddls.mount;

import net.piratjsk.sddls.saddle.SignedSaddle;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Pig;
import org.bukkit.inventory.ItemStack;

public class ProtectedPig extends GenericMount {

    private final SignedSaddle saddle;

    public ProtectedPig(final Pig pig) {
        super(pig);
        if(pig.hasSaddle())
            this.saddle = SignedSaddle.fromItemStack(new ItemStack(Material.SADDLE));
        else
            this.saddle = SignedSaddle.fromItemStack(null);

    }

    @Override
    public SignedSaddle getSaddle() {
        return this.saddle;
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
