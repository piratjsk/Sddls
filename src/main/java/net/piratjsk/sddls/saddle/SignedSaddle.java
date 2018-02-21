package net.piratjsk.sddls.saddle;

import net.piratjsk.sddls.Sddls;
import net.piratjsk.sddls.signature.Signature;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SignedSaddle {

    private final Collection<Signature> signatures;
    private final ItemStack saddleItem;

    public SignedSaddle(final Collection<Signature> signatures, final ItemStack saddleItem) {
        this.signatures = signatures;
        this.saddleItem = saddleItem;
    }

    public boolean isSigned() {
        return !this.signatures.isEmpty();
    }

    public boolean isSignedBy(final OfflinePlayer player) {
        return this.signatures.stream().anyMatch( sig -> sig.matchesPlayer(player));
    }

    public ItemStack getItem() {
        this.updateSignatures(Sddls.getOfflineDaysLimit());
        this.updateItem();
        return this.saddleItem;
    }

    public Collection<Signature> getSignatures() {
        return this.signatures;
    }

    public void sign(final Signature signature) {
        this.signatures.add(signature);
    }

    public void updateItem() {
        final List<String> lore = this.saddleItem.getItemMeta().getLore();
        lore.clear();
        this.signatures.forEach( sig -> lore.add(sig.toString()));
        this.saddleItem.getItemMeta().setLore(lore);
    }

    public void updateSignatures(final int offlineDaysLimit) {
        this.signatures.removeIf(sig -> sig.hasExpired(offlineDaysLimit));
    }

    public static SignedSaddle fromItemStack(final ItemStack saddleItem) {
        if (!isValidSaddleItem(saddleItem)) return null;
        final Collection<Signature> signatures = new ArrayList<>();
        saddleItem.getItemMeta().getLore().forEach( line -> {
            final Signature signature = Signature.fromString(line);
            if (signature != null) signatures.add(signature);
        });
        return new SignedSaddle(signatures, saddleItem);
    }

    private static boolean isValidSaddleItem(final ItemStack item) {
        return item.getType().equals(Material.SADDLE) || item.getType().equals(Material.CARPET);
    }

}
