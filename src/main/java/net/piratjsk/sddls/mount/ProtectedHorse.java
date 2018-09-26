package net.piratjsk.sddls.mount;

import net.piratjsk.sddls.saddle.SignedSaddle;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.inventory.ItemStack;

public class ProtectedHorse extends GenericProtectedMount {

    public ProtectedHorse(final AbstractHorse horse) {
        super(horse);
        final ItemStack saddleItem = horse.getInventory().getSaddle();
        this.saddle = SignedSaddle.fromItemStack(saddleItem);
    }

}
