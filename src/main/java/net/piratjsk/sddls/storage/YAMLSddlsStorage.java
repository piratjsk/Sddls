package net.piratjsk.sddls.storage;

import net.piratjsk.sddls.Saddle;
import net.piratjsk.sddls.Sddls;
import net.piratjsk.sddls.Signature;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class YAMLSddlsStorage implements SddlsStorage {

    private final Sddls plugin;
    private final File mountsFile, uuidsFile;

    public YAMLSddlsStorage(final Sddls plugin) {
        this.plugin = plugin;
        this.mountsFile = new File(this.plugin.getDataFolder(), "/data/mounts.yml");
        this.uuidsFile = new File(this.plugin.getDataFolder(), "/data/uuids.yml");
        this.mountsFile.mkdirs();
        this.uuidsFile.mkdirs();
    }

    @Override
    public Map<UUID, Saddle> loadProtectedMounts() {
        final YamlConfiguration yaml = new YamlConfiguration();
        try {
            yaml.load(mountsFile);
        } catch (final IOException | InvalidConfigurationException e) {
            plugin.getLogger().warning("Could not load protected mounts data.");
            e.printStackTrace();
        }
        final Map<UUID, Saddle> protectedMounts = new HashMap<>();
        final List<Map<?, ?>> mounts = yaml.getMapList("mounts");
        for (Map<?,?> data : mounts) {
            final UUID entity = UUID.fromString((String) data.get("entity"));
            final List<Signature> signatures = new ArrayList<>();
            for (final String uuidString : (List<String>) data.get("signatures")) {
                final UUID uuid = UUID.fromString(uuidString);
                final Signature signature = new Signature(uuid);
                signatures.add(signature);
            }
            final ItemStack saddleItem = new ItemStack(Material.SADDLE);
            final Saddle saddle = new Saddle(signatures, saddleItem);
            protectedMounts.put(entity, saddle);
        }
        return protectedMounts;
    }

    @Override
    public void saveProtectedMounts(Map<UUID, Saddle> protectedMounts) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            final YamlConfiguration yaml = new YamlConfiguration();
            final List<Map<?, ?>> mounts = yaml.getMapList("mounts");
            for (final Map.Entry entry : protectedMounts.entrySet()) {
                final Map<String, Object> mount = new HashMap<>();
                mount.put("entity",entry.getKey().toString());
                final List<String> signatures = new ArrayList<>();
                final Saddle saddle = (Saddle) entry.getValue();
                for (final Signature signature : saddle.getSignatures())
                    signatures.add(signature.getUUID().toString());
                mount.put("signatures", signatures);
                mounts.add(mount);
            }
            yaml.set("mounts", mounts);
            try {
                yaml.save(mountsFile);
            } catch (final IOException e) {
                plugin.getLogger().warning("Could not save protected mounts data.");
                e.printStackTrace();
            }
        });
    }

    @Override
    public Map<String, UUID> loadUUIDMap() {
        final YamlConfiguration yaml = new YamlConfiguration();
        try {
            yaml.load(uuidsFile);
        } catch (final IOException | InvalidConfigurationException e) {
            plugin.getLogger().warning("Could not load code to uuid map data.");
            e.printStackTrace();
        }
        final Map<String, UUID> uuidMap = new HashMap<>();
        for (String code : yaml.getKeys(false)) {
            final UUID uuid = UUID.fromString(yaml.getString(code));
            uuidMap.put(code, uuid);
        }
        return uuidMap;
    }

    @Override
    public void saveUUIDMap(Map<String, UUID> uuidMap) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            final YamlConfiguration yaml = new YamlConfiguration();
            for (final Map.Entry entry : uuidMap.entrySet()) {
                yaml.set(entry.getKey().toString(),entry.getValue().toString());
            }
            try {
                yaml.save(uuidsFile);
            } catch (final IOException e) {
                plugin.getLogger().warning("Could not save code to uuid map data.");
                e.printStackTrace();
            }
        });
    }
}
