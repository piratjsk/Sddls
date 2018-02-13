package net.piratjsk.sddls;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

public class Saddle {

    private final Collection<Signature> signatures;
    private final ItemStack saddleItem;

    public Saddle(final Collection<Signature> signatures, final ItemStack saddleItem) {
        this.signatures = signatures;
        this.saddleItem = saddleItem;
    }

    public boolean isSigned() {
        return !this.signatures.isEmpty();
    }

    public boolean isSignedBy(final Player player) {
        return this.signatures.stream().anyMatch( sig -> sig.matchesPlayer(player));
    }

    public ItemStack getItem() {
        return this.saddleItem;
    }

    public Collection<Signature> getSignatures() {
        return this.signatures;
    }

    public void sign(final Signature signature) {
        this.signatures.add(signature);
    }

    public static Saddle fromItemStack(final ItemStack saddleItem) {
        if (!isValidSaddleItem(saddleItem)) return null;
        final Collection<Signature> signatures = new ArrayList<>();
        saddleItem.getItemMeta().getLore().forEach( line -> {
            final Signature signature = Signature.fromString(line);
            if (signature != null) signatures.add(signature);
        });
        return new Saddle(signatures, saddleItem);
    }

    private static boolean isValidSaddleItem(final ItemStack item) {
        return item.getType().equals(Material.SADDLE) || item.getType().equals(Material.CARPET);
    }

}
