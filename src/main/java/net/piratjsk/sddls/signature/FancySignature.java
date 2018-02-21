package net.piratjsk.sddls.signature;

import net.piratjsk.sddls.utils.UUIDUtils;
import org.bukkit.ChatColor;

import java.util.UUID;

public class FancySignature extends UUIDSignature {

    private final static String separator = ChatColor.UNDERLINE.toString();
    private final static String nameFormat = ChatColor.GRAY + "" + ChatColor.ITALIC;
    private final static String usernamePattern = "[a-zA-Z0-9_]{2,16}";
    public static final String pattern = nameFormat + usernamePattern + separator + "(" + ChatColor.COLOR_CHAR + "[0-9a-fr]){36}";

    public FancySignature(final UUID uuid) {
        super(uuid);
    }

    @Override
    public boolean matchesString(final String line) {
        final String code = line.split(separator)[1];
        final UUID uuid = UUIDUtils.decodeFromColors(code);
        return this.getUUID().equals(uuid);
    }

    @Override
    public SignatureType getType() {
        return SignatureType.FANCY;
    }

    @Override
    public String toString() {
        final String code = UUIDUtils.encodeAsColors(this.getUUID());
        return nameFormat + this.getName() + separator + code;
    }

    public static FancySignature fromString(final String line) {
        final String code = line.split(separator)[1];
        final UUID uuid = UUIDUtils.decodeFromColors(code);
        return new FancySignature(uuid);
    }

}
