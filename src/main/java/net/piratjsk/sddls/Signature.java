package net.piratjsk.sddls;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Signature {

    private final UUID uuid;

    public Signature(final UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getName() {
        return Bukkit.getOfflinePlayer(this.getUUID()).getName();
    }

    public boolean matchesPlayer(final Player player) {
        return this.matchesUUID(player.getUniqueId());
    }

    public boolean matchesUUID(final UUID uuid) {
        return uuid.equals(this.getUUID());
    }

    public boolean matchesName(final String name) {
        return name.equals(this.getName());
    }

    public boolean hasExpired(final int shelfTimeInDays) {
        final OfflinePlayer player = Bukkit.getOfflinePlayer(this.getUUID());
        final long lastPlayed = player.getLastPlayed();
        if (lastPlayed == 0) return true;
        final long shelfTimeInMilis = TimeUnit.MILLISECONDS.convert(shelfTimeInDays,TimeUnit.DAYS);
        final Instant now = Instant.now();
        final Instant expirationDate = Instant.ofEpochMilli(lastPlayed + shelfTimeInMilis);
        return now.isAfter(expirationDate);
    }

    public static Signature fromString(final String loreLine) {
        final String uuidCode = loreLine.split(" ")[1].trim();
        final UUID uuid = Sddls.getInstance().getDataManager().getUUID(uuidCode);
        if (uuid == null)
            return null;
        return new Signature(uuid);
    }

    public String toString() {
         final String name = this.getName();
         final String code = Sddls.getInstance().getDataManager().putUUID(this.uuid);
         return name + " " + code;
    }


}
