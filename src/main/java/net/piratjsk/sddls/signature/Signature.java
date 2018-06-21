package net.piratjsk.sddls.signature;

import static net.piratjsk.sddls.Sddls.PREFIX;
import static net.piratjsk.sddls.Sddls.SEPARATOR;
import static net.piratjsk.sddls.Sddls.NAME_FORMAT;
import net.piratjsk.sddls.utils.UUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Signature {

    private final UUID uuid;

    public Signature(final UUID uuid) {
        this.uuid = uuid;
    }

    public boolean machesPlayer(final Player player) {
        return matchesUUID(player.getUniqueId());
    }

    public boolean matchesPlayer(final OfflinePlayer player) {
        return matchesUUID(player.getUniqueId());
    }

    public boolean matchesUUID(final UUID uuid) {
        return uuid.equals(this.uuid);
    }

    public boolean hasExpired(int offlineDaysLimit) {
        final OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        return getOfflineDays(player) > offlineDaysLimit;
    }

    private int getOfflineDays(final OfflinePlayer player) {
        final long offlineMillis = System.currentTimeMillis() - player.getLastPlayed();
        return (int) TimeUnit.MILLISECONDS.toDays(offlineMillis);
    }

    public String getName() {
        return Bukkit.getOfflinePlayer(this.uuid).getName();
    }

    @Override
    public String toString() {
        final String encodedUUID = UUIDUtils.encodeAsColors(this.uuid);
        final String name = Bukkit.getOfflinePlayer(this.uuid).getName();
        return PREFIX + NAME_FORMAT + name + SEPARATOR + encodedUUID;
    }

    public static Signature fromString(final String string) {
        if (!string.startsWith(PREFIX)) return null;
        final String encodedUUID = string.split(SEPARATOR)[1];
        final UUID uuid = UUIDUtils.decodeFromColors(encodedUUID);
        return new Signature(uuid);
    }

}
