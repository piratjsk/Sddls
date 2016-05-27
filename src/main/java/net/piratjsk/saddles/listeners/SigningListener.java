package net.piratjsk.saddles.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import net.piratjsk.saddles.Saddles;

public final class SigningListener implements Listener {

    @EventHandler
    public void onSaddleSign(final PrepareItemCraftEvent event) {
        if (event.getRecipe() instanceof ShapelessRecipe) {
            final ShapelessRecipe recipe = (ShapelessRecipe)event.getRecipe();
            if (recipe.getIngredientList().equals(Saddles.recipe.getIngredientList())) {
                final Player player = (Player) event.getView().getPlayer();
                ItemStack saddle = null;
                for (final ItemStack item : event.getInventory().getMatrix())
                    if (item.getType() == Material.SADDLE) saddle = item;
                saddle = Saddles.signSaddle(saddle, player);
                event.getInventory().setResult(saddle);
            }
        }
    }

}
