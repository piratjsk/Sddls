package net.piratjsk.sddls;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import net.piratjsk.sddls.listeners.MountListener;
import net.piratjsk.sddls.listeners.SigningListener;

import java.util.ArrayList;

public final class Sddls extends JavaPlugin {

    public final static String PREFIX = ChatColor.translateAlternateColorCodes('&',"&5&d&d&l&5");
    public final static String SEPARATOR = ChatColor.UNDERLINE.toString();
    public final static String NAME_FORMAT = ChatColor.GRAY + "" + ChatColor.ITALIC;

    private final ShapelessRecipe signSaddleRecipe = new ShapelessRecipe(
            new NamespacedKey(this, "signSaddle"),
            new ItemStack(Material.SADDLE)
    ).addIngredient(Material.SADDLE);

    private final ShapelessRecipe signCarpetRecipe = new ShapelessRecipe(
            new NamespacedKey(this, "signCarpet"),
            new ItemStack(Material.WHITE_CARPET)
    );

    @Override
    public void onEnable() {
        this.setupConfig();
        this.registerCommand();
        this.registerListeners();
        this.prepareSignCarpetRecipe();
        this.registerRecipes();
    }

    private void setupConfig() {
        this.saveDefaultConfig();
    }

    private void registerCommand() {
        this.getCommand("sddls").setExecutor(new SddlsCommand(this));
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new SigningListener(), this);
        this.getServer().getPluginManager().registerEvents(new MountListener(), this);
    }

    private void prepareSignCarpetRecipe() {
        this.signCarpetRecipe.addIngredient(new MaterialChoice(new ArrayList<>(Tag.CARPETS.getValues())));
    }

    private void registerRecipes() {
        this.getServer().addRecipe(this.signSaddleRecipe);
        if (canBeProtected(EntityType.LLAMA))
            this.getServer().addRecipe(this.signCarpetRecipe);
    }

    public static boolean canBeProtected(final EntityType entityType) {
        final String type = entityType.name().toLowerCase().replaceAll("_", "-");
        final Configuration config = JavaPlugin.getPlugin(Sddls.class).getConfig();
        return config.getBoolean("protect."+type, false);
    }

    public static boolean isSigningRecipe(final Recipe recipe) {
        if (!(recipe instanceof ShapelessRecipe)) return false;
        final NamespacedKey recipeKey = ((ShapelessRecipe) recipe).getKey();
        final NamespacedKey saddleRecipeKey = JavaPlugin.getPlugin(Sddls.class).signSaddleRecipe.getKey();
        final NamespacedKey carpetRecipeKey = JavaPlugin.getPlugin(Sddls.class).signCarpetRecipe.getKey();
        return recipeKey.equals(saddleRecipeKey) || recipeKey.equals(carpetRecipeKey);
    }

    public static int getOfflineDaysLimit() {
        final Configuration config = JavaPlugin.getPlugin(Sddls.class).getConfig();
        return config.getInt("signature-expires-after", 0);
    }

}
