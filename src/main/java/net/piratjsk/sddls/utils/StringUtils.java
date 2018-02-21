package net.piratjsk.sddls.utils;

public class StringUtils {

    public static String capitalizeFirstLetter(final String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

}
