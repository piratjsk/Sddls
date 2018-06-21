package net.piratjsk.sddls;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import net.piratjsk.sddls.listeners.MountListener;
import net.piratjsk.sddls.listeners.SigningListener;

public final class Sddls extends JavaPlugin {

    public final static String PREFIX = ChatColor.translateAlternateColorCodes('&',"&5&d&d&l&5");
    public final static String SEPARATOR = ChatColor.UNDERLINE.toString();
    public final static String NAME_FORMAT = ChatColor.GRAY + "" + ChatColor.ITALIC;

    private final ShapelessRecipe saddleSignRecipe = new ShapelessRecipe(
            new NamespacedKey(this, "saddleSign"),
            new ItemStack(Material.SADDLE)
    ).addIngredient(Material.SADDLE);

    private final ShapelessRecipe carpetSignRecipe = new ShapelessRecipe(
            new NamespacedKey(this, "carpetSign"),
            new ItemStack(Material.CARPET)
    ).addIngredient(Material.CARPET);

    @Override
    public void onEnable() {
        this.setupConfig();
        this.registerCommand();
        this.registerListeners();
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

    private void registerRecipes() {
        this.getServer().addRecipe(this.saddleSignRecipe);
        if (canBeProtected(EntityType.LLAMA))
            this.getServer().addRecipe(this.carpetSignRecipe);
    }

    public static boolean canBeProtected(final EntityType entityType) {
        final String type = entityType.name().toLowerCase().replaceAll("_", "-");
        final Configuration config = JavaPlugin.getPlugin(Sddls.class).getConfig();
        return config.getBoolean("protect."+type, false);
    }

    public static boolean isSigningRecipe(final Recipe recipe) {
        if (!(recipe instanceof ShapelessRecipe)) return false;
        final NamespacedKey recipeKey = ((ShapelessRecipe) recipe).getKey();
        final NamespacedKey saddleRecipeKey = JavaPlugin.getPlugin(Sddls.class).saddleSignRecipe.getKey();
        final NamespacedKey carpetRecipeKey = JavaPlugin.getPlugin(Sddls.class).carpetSignRecipe.getKey();
        return recipeKey.equals(saddleRecipeKey) || recipeKey.equals(carpetRecipeKey);
    }

    public static int getOfflineDaysLimit() {
        final Configuration config = JavaPlugin.getPlugin(Sddls.class).getConfig();
        return config.getInt("signature-expires-after", 0);
    }

}
