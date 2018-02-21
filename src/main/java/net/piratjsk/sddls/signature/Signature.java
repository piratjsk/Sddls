package net.piratjsk.sddls.signature;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

public interface Signature {

    UUID getUUID();
    String getName();
    OfflinePlayer getPlayer();

    boolean matchesPlayer(final OfflinePlayer player);
    boolean matchesUUID(final UUID uuid);
    boolean matchesString(final String line);

    boolean hasExpired(final int offlineDaysLimit);

    SignatureType getType();

    static Signature fromString(final String line) {
        if (line.matches(UUIDSignature.pattern)) {
            return UUIDSignature.fromString(line);
        }
        if (line.matches(FancySignature.pattern)) {
            return FancySignature.fromString(line);
        }
        return null;
    }

}
