package net.piratjsk.sddls;

import net.piratjsk.sddls.storage.SddlsStorage;
import org.bukkit.ChatColor;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class DataManager {

    private final SddlsStorage storage;
    private final Map<String,UUID> uuidMap;
    private final Map<UUID,Saddle> protectedMounts;
    private final Random rand = new Random();

    public DataManager(final SddlsStorage storage) {
        this.storage = storage;
        this.uuidMap = this.storage.loadUUIDMap();
        this.protectedMounts = this.storage.loadProtectedMounts();
    }

    public UUID getUUID(final String uuidCode) {
        return this.uuidMap.get(uuidCode);
    }

    public String putUUID(final UUID uuid) {
        if (this.uuidMap.containsValue(uuid)) {
            for (Map.Entry<String,UUID> entry: this.uuidMap.entrySet()) {
                if (entry.getValue().equals(uuid)) {
                    return entry.getKey();
                }
            }
        }
        final String uuidCode = this.generateNewCode();
        this.uuidMap.put(uuidCode, uuid);
        this.storage.saveUUIDMap(this.uuidMap);
        return uuidCode;
    }

    private String generateNewCode() {
        char[] chars = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','k','l','m','n','o','r'};
        int limit = 10;
        final StringBuilder code = new StringBuilder();
        do {
            code.setLength(0);
            for (int i=0;i<12;i++) {
                char c = chars[rand.nextInt(chars.length)];
                code.append(ChatColor.getByChar(c));
            }
        } while (this.uuidMap.containsKey(code.toString()) && limit-- > 0);
        return code.toString();
    }

    public ProtectedMount getProtectedMount(final UUID uuid) {
        final Saddle saddle = this.protectedMounts.get(uuid);
        return new ProtectedMount(uuid,saddle);
    }

    public void putProtectedMount(final ProtectedMount mount) {
        final UUID uuid = mount.getEntity().getUniqueId();
        final Saddle saddle = mount.getSaddle();
        this.protectedMounts.put(uuid,saddle);
        this.storage.saveProtectedMounts(this.protectedMounts);
    }

}
