package net.piratjsk.sddls.listeners;

import net.piratjsk.sddls.saddle.SignedSaddle;
import net.piratjsk.sddls.signature.Signature;
import net.piratjsk.sddls.signature.SignatureType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import net.piratjsk.sddls.Sddls;

public final class SigningListener implements Listener {

    private final Sddls sddls;

    public SigningListener(final Sddls sddlsPlugin) {
        this.sddls = sddlsPlugin;
    }

    @EventHandler
    public void onSaddleSign(final PrepareItemCraftEvent event) {
        if (!this.sddls.isSigningRecipe(event.getRecipe())) return;
        final Player player = (Player) event.getView().getPlayer();
        final ItemStack toSign = event.getInventory().getMatrix()[0];
        final SignedSaddle saddle = SignedSaddle.fromItemStack(toSign);
        if (saddle.isSignedBy(player)) {
            saddle.getSignatures().clear();
        } else {
            final SignatureType signatureType = SignatureType.valueOf(this.sddls.getConfig().getString("signature-type"));
            final Signature signature = this.sddls.getPlayerSignature(player);
            saddle.sign(signature);
        }
        event.getInventory().setResult(saddle.getItem());
    }

}
