package net.piratjsk.sddls.saddle;

import net.piratjsk.sddls.Sddls;
import net.piratjsk.sddls.signature.Signature;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        return new ArrayList<>(this.signatures);
    }

    public void removeAllSignatures() {
        this.signatures.clear();
    }

    public void sign(final Player player) {
        final Signature signature = new Signature(player.getUniqueId());
        this.sign(signature);
    }

    public void sign(final Signature signature) {
        this.signatures.add(signature);
    }

    public void updateItem() {
        final List<String> lore = getSaddleItemLore();

        // remove all signatures from item lore
        lore.removeIf(line -> line.startsWith(Sddls.PREFIX));

        // re-add signatures to lore (if there are any)
        if (this.isSigned()) {
            this.getSignatures().forEach( sig -> lore.add(sig.toString()));
        }

        // update item lore
        this.setSaddleItemLore(lore);
    }

    private List<String> getSaddleItemLore() {
        final List<String> lore = new ArrayList<>();
        if (this.saddleItem.hasItemMeta() && this.saddleItem.getItemMeta().hasLore()) {
            lore.clear();
            lore.addAll(this.saddleItem.getItemMeta().getLore());
        }
        return lore;
    }

    private void setSaddleItemLore(final List<String> newLore) {
        final ItemMeta itemMeta = this.saddleItem.getItemMeta();
        itemMeta.setLore(newLore);
        this.saddleItem.setItemMeta(itemMeta);
    }

    public void updateSignatures(final int offlineDaysLimit) {
        this.signatures.removeIf(sig -> sig.hasExpired(offlineDaysLimit));
    }

    public static SignedSaddle fromItemStack(final ItemStack saddleItem) {
        if (!isValidSaddleItem(saddleItem)) return new NotReallyASaddle(saddleItem);
        final Collection<Signature> signatures = new ArrayList<>();
        if (saddleItem.hasItemMeta() && saddleItem.getItemMeta().hasLore()) {
            saddleItem.getItemMeta().getLore().forEach( line -> {
                final Signature signature = Signature.fromString(line);
                if (signature != null) signatures.add(signature);
            });
        }
        return new SignedSaddle(signatures, saddleItem);
    }

    public static boolean isValidSaddleItem(final ItemStack item) {
        return item != null && (item.getType().equals(Material.SADDLE) || Tag.CARPETS.getValues().contains(item.getType()));
    }

}
