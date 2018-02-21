package net.piratjsk.sddls.utils;

import org.bukkit.ChatColor;

import java.util.UUID;

public class UUIDUtils {

    public static String encodeAsColors(final UUID uuid) {
        final String uuidString = uuid.toString();
        final StringBuilder encodedStringBuilder = new StringBuilder(72);
        for (final char c : uuidString.toCharArray()) {
            if (c == '-')
                encodedStringBuilder.append(ChatColor.RESET);
            else
                encodedStringBuilder.append(ChatColor.getByChar(c));
        }
        return encodedStringBuilder.toString();
    }

    public static UUID decodeFromColors(final String code) {
        final StringBuilder uuidStringBuilder = new StringBuilder(36);
        for (final char c : code.toCharArray()) {
            if (c == ChatColor.COLOR_CHAR) continue;
            if (c == ChatColor.RESET.getChar())
                uuidStringBuilder.append('-');
            else
                uuidStringBuilder.append(c);
        }
        final String uuidString = uuidStringBuilder.toString();
        return UUID.fromString(uuidString);
    }

}
