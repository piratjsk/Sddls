package net.piratjsk.sddls.signature;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UUIDSignature implements Signature {

    public final static String pattern = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";
    private final UUID uuid;

    public UUIDSignature(final UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public String getName() {
        return Bukkit.getOfflinePlayer(this.uuid).getName();
    }

    @Override
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(this.uuid);
    }

    @Override
    public boolean matchesPlayer(final OfflinePlayer player) {
        return this.matchesUUID(player.getUniqueId());
    }

    @Override
    public boolean matchesUUID(final UUID uuid) {
        return this.uuid.equals(uuid);
    }

    @Override
    public boolean matchesString(final String line) {
        return this.toString().equals(line);
    }

    @Override
    public boolean hasExpired(int offlineDaysLimit) {
        if (offlineDaysLimit <= 0 || this.getPlayer().isOnline()) return false;
        final long lastPlayed = this.getPlayer().getLastPlayed();
        if (lastPlayed == 0) return true;
        final long offlineMilisLimit = TimeUnit.MILLISECONDS.convert(offlineDaysLimit, TimeUnit.DAYS);
        final Instant now = Instant.now();
        final Instant expirationDate = Instant.ofEpochMilli(lastPlayed + offlineMilisLimit);
        return now.isAfter(expirationDate);
    }

    @Override
    public SignatureType getType() {
        return SignatureType.UUID;
    }

    @Override
    public String toString() {
        return this.getUUID().toString();
    }

    public static UUIDSignature fromString(final String line) {
        final UUID uuid = UUID.fromString(line);
        return new UUIDSignature(uuid);
    }
}
