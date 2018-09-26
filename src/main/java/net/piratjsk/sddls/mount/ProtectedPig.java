package net.piratjsk.sddls.mount;

import net.piratjsk.sddls.saddle.SignedSaddle;
import org.bukkit.Material;
import org.bukkit.entity.Pig;
import org.bukkit.inventory.ItemStack;

public class ProtectedPig extends GenericProtectedMount {

    public ProtectedPig(final Pig pig) {
        super(pig);
        if(pig.hasSaddle())
            this.saddle = SignedSaddle.fromItemStack(new ItemStack(Material.SADDLE));
        else
            this.saddle = SignedSaddle.fromItemStack(null);

    }

}
