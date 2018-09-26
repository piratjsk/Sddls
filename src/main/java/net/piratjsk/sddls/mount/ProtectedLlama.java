package net.piratjsk.sddls.mount;

import net.piratjsk.sddls.saddle.SignedSaddle;
import org.bukkit.entity.Llama;
import org.bukkit.inventory.ItemStack;

public class ProtectedLlama extends ProtectedHorse {

    public ProtectedLlama(Llama llama) {
        super(llama);
        final ItemStack saddleItem = llama.getInventory().getDecor();
        this.saddle = SignedSaddle.fromItemStack(saddleItem);
    }

}
