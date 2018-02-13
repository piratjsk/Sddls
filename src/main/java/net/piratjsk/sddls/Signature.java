package net.piratjsk.sddls;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

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

    public static Signature fromString(final String loreLine) {
        final String uuidCode = loreLine.split(" ")[1].trim();
        // TODO: implement
        return null;
    }


}
