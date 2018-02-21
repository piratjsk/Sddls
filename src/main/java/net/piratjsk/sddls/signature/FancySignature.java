package net.piratjsk.sddls.signature;

import net.piratjsk.sddls.utils.UUIDUtils;
import org.bukkit.ChatColor;

import java.util.UUID;


public class FancySignature extends UUIDSignature {

    public static final String pattern = ".+? (" + ChatColor.COLOR_CHAR + "[0-9a-fr]){36}";

    public FancySignature(final UUID uuid) {
        super(uuid);
    }

    @Override
    public SignatureType getType() {
        return SignatureType.FANCY;
    }

    @Override
    public String toString() {
        final String code = UUIDUtils.encodeAsColors(this.getUUID());
        return this.getName() + " " + code;
    }

    public static FancySignature fromString(final String line) {
        final String code = line.split(" ")[1];
        final UUID uuid = UUIDUtils.decodeFromColors(code);
        return new FancySignature(uuid);
    }

}
