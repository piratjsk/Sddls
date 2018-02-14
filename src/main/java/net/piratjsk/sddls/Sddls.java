package net.piratjsk.sddls;

import net.piratjsk.sddls.storage.SddlsStorage;
import net.piratjsk.sddls.storage.YAMLSddlsStorage;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import net.piratjsk.sddls.listeners.HorseListener;
import net.piratjsk.sddls.listeners.SigningListener;
import org.bukkit.entity.EntityType;
public final class Sddls extends JavaPlugin {

    public final static ShapelessRecipe recipe = new ShapelessRecipe(new ItemStack(Material.SADDLE)).addIngredient(Material.SADDLE);

    private DataManager dataManager;

    @Override
    public void onEnable() {
        this.setupConfig();
        this.registerCommand();
        this.registerListeners();
        this.registerRecipe();
    }

    private void setupConfig() {
        this.saveDefaultConfig();
    }

    private void registerCommand() {
        this.getCommand("sddls").setExecutor(new SddlsCommand(this));
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new SigningListener(), this);
        this.getServer().getPluginManager().registerEvents(new HorseListener(), this);
    }

    private void registerRecipe() {
        this.getServer().addRecipe(recipe);
    }

    private void setupDataManager() {
        // TODO: implement more storage types
        // TODO: make it configurable using plugin config.yml
        final SddlsStorage storage = new YAMLSddlsStorage(this);
    }

    public DataManager getDataManager() {
        return this.dataManager;
    }


    
    public static boolean canBeProtected(final EntityType entityType) {
        final ConfigurationSection config = getInstance().getConfig().getConfigurationSection("protect");
        if (entityType.equals(EntityType.HORSE)) return config.getBoolean("horse");
        if (entityType.equals(EntityType.ZOMBIE_HORSE)) return config.getBoolean("zombie-horse");
        if (entityType.equals(EntityType.SKELETON_HORSE)) return config.getBoolean("skeleton-horse");
        if (entityType.equals(EntityType.DONKEY)) return config.getBoolean("donkey");
        if (entityType.equals(EntityType.MULE)) return config.getBoolean("mule");
        if (entityType.equals(EntityType.PIG)) /* TODO: handle pigs */return false /*config.getBoolean("pig")*/;
        if (entityType.equals(EntityType.LLAMA)) return config.getBoolean("lama");
        return false;
    }

    private static Sddls getInstance() {
        return JavaPlugin.getPlugin(Sddls.class);
    }
}
