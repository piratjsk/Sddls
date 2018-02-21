package net.piratjsk.sddls;

import net.piratjsk.sddls.mount.ProtectedMount;
import net.piratjsk.sddls.signature.FancySignature;
import net.piratjsk.sddls.signature.Signature;
import net.piratjsk.sddls.signature.SignatureType;
import net.piratjsk.sddls.signature.UUIDSignature;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import net.piratjsk.sddls.listeners.MountListener;
import net.piratjsk.sddls.listeners.SigningListener;
import static net.piratjsk.sddls.utils.StringUtils.capitalizeFirstLetter;
import org.bukkit.entity.EntityType;

import java.util.stream.Collectors;

public final class Sddls extends JavaPlugin {

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
        this.getServer().getPluginManager().registerEvents(new SigningListener(this), this);
        this.getServer().getPluginManager().registerEvents(new MountListener(this), this);
    }

    private void registerRecipes() {
        this.getServer().addRecipe(this.saddleSignRecipe);
        if (this.canBeProtected(EntityType.LLAMA))
            this.getServer().addRecipe(this.carpetSignRecipe);
    }

    public void sendNoAccessMessage(final Player player, final ProtectedMount mount) {
        final Entity entity = mount.getEntity();
        final String type = entity.getType().toString().toLowerCase().replaceAll("_", "-");
        final String name = entity.getCustomName() != null ? entity.getCustomName() : type;
        final String players = mount.getSaddle().getSignatures().stream()
                .map(Signature::getName)
                .collect(Collectors.joining(", "));
        final String defaultMessage = this.getConfig().getString("no-access-msg", "&6[Sddls] &7You don't have access to this %mount%.");
        final String message = this.getConfig().getString("no-access-msg-" + type, defaultMessage)
                .replaceAll("%mount%", type)
                .replaceAll("%Mount%", capitalizeFirstLetter(type))
                .replaceAll("%name%", name)
                .replaceAll("%Name%", capitalizeFirstLetter(name))
                .replaceAll("%players%", players);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public boolean canBeProtected(final EntityType entityType) {
        final ConfigurationSection config = this.getConfig().getConfigurationSection("protect");
        if (entityType.equals(EntityType.HORSE)) return config.getBoolean("horse");
        if (entityType.equals(EntityType.ZOMBIE_HORSE)) return config.getBoolean("zombie-horse");
        if (entityType.equals(EntityType.SKELETON_HORSE)) return config.getBoolean("skeleton-horse");
        if (entityType.equals(EntityType.DONKEY)) return config.getBoolean("donkey");
        if (entityType.equals(EntityType.MULE)) return config.getBoolean("mule");
        if (entityType.equals(EntityType.PIG)) /* TODO: handle pigs */return false /*config.getBoolean("pig")*/;
        if (entityType.equals(EntityType.LLAMA)) return config.getBoolean("lama");
        return false;
    }

    public boolean isSigningRecipe(final Recipe recipe) {
        if (!(recipe instanceof ShapelessRecipe)) return false;
        final ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
        if (shapelessRecipe.getKey().equals(this.saddleSignRecipe.getKey())) return true;
        if (shapelessRecipe.getKey().equals(this.carpetSignRecipe.getKey())) return true;
        return false;
    }

    public Signature getPlayerSignature(final OfflinePlayer player) {
        final String typeString = this.getConfig().getString("signature-type", "fancy").toUpperCase().replaceAll(" ", "_");
        final SignatureType type = SignatureType.valueOf(typeString);
        if (type == SignatureType.UUID)
            return new UUIDSignature(player.getUniqueId());
        if (type == SignatureType.FANCY)
            return new FancySignature(player.getUniqueId());
        return null;
    }

    public static int getOfflineDaysLimit() {
        final Configuration config = JavaPlugin.getPlugin(Sddls.class).getConfig();
        int offlineDaysLimit = 0; // signatures will not expire if limit is set to 0
        if (config.isInt("signature-expires-after"))
            offlineDaysLimit = config.getInt("signature-expires-after");
        return offlineDaysLimit;
    }

}
