package net.piratjsk.sddls.saddle;

import net.piratjsk.sddls.signature.Signature;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;

public class NotReallyASaddle extends SignedSaddle {

    public NotReallyASaddle(final ItemStack saddleItem) {
        this(Collections.emptyList(), saddleItem!=null ? saddleItem : new ItemStack(Material.AIR));
    }
    
    public NotReallyASaddle(final Collection<Signature> signatures, final ItemStack saddleItem) {
        super(signatures, saddleItem);
    }

    @Override
    public boolean isSigned() {
        return false;
    }

    @Override
    public boolean isSignedBy(final OfflinePlayer player) {
        return false;
    }

    @Override
    public void sign(final Signature signature) {
        // do nothing
    }

    @Override
    public void updateItem() {
        // do nothing
    }

    @Override
    public void updateSignatures(final int offlineDaysLimit) {
        // do nothing
    }
}
