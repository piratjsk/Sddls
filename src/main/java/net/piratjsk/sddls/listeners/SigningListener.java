package net.piratjsk.sddls.listeners;

import net.piratjsk.sddls.saddle.SignedSaddle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import net.piratjsk.sddls.Sddls;
import org.bukkit.inventory.Recipe;

import java.util.Arrays;
import java.util.Objects;

import static net.piratjsk.sddls.saddle.SignedSaddle.isValidSaddleItem;

public final class SigningListener implements Listener {

    @EventHandler
    public void onSaddleSign(final PrepareItemCraftEvent event) {
        if (isSomeoneActuallyTryingToSignASaddle(event))
            signSaddle(event);
    }

    private boolean isSomeoneActuallyTryingToSignASaddle(final PrepareItemCraftEvent event) {
        return isSigningRecipe(event.getRecipe()) && isValidSaddleItem(getSaddleItem(event));
    }

    private boolean isSigningRecipe(final Recipe recipe) {
        return Sddls.isSigningRecipe(recipe);
    }

    private ItemStack getSaddleItem(final PrepareItemCraftEvent event) {
        return Arrays.stream(event.getInventory().getMatrix())
                .filter(Objects::nonNull)
                .toArray(ItemStack[]::new)[0].clone();

    }

    private void signSaddle(final PrepareItemCraftEvent event) {
        final ItemStack item = getSaddleItem(event);
        final SignedSaddle saddle = SignedSaddle.fromItemStack(item);
        final Player player = (Player) event.getView().getPlayer();
        if (saddle.isSignedBy(player)) {
            saddle.removeAllSignatures();
        } else {
            saddle.sign(player);
        }
        event.getInventory().setResult(saddle.getItem());
    }

}
