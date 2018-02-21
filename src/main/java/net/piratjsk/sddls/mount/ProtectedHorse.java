package net.piratjsk.sddls.mount;

import net.piratjsk.sddls.saddle.SignedSaddle;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.inventory.ItemStack;

public class ProtectedHorse extends GenericMount {

    private final SignedSaddle saddle;

    public ProtectedHorse(final AbstractHorse horse) {
        super(horse);
        final ItemStack saddleItem = horse.getInventory().getSaddle();
        this.saddle = SignedSaddle.fromItemStack(saddleItem);
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
}
