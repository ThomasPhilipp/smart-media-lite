package com.zwickit.smart.media.ui.logic;

import java.util.Optional;

/**
 * Created by Thomas Philipp Zwick
 */
public class HashUtils {

    /**
     * Creates a MD5 hash of given text and returns it.
     *
     * @param text
     * @return
     */
    public static Optional<String> toMD5Hash(String text) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(text.getBytes());

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }

            return Optional.of(sb.toString());
        } catch (java.security.NoSuchAlgorithmException e) {
            System.err.println("Could not create MD5-hash because of " + e);
        }

        return Optional.empty();
    }
}
