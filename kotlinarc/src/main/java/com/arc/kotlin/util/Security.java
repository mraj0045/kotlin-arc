package com.arc.kotlin.util;

import android.annotation.SuppressLint;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressLint("GetInstance")
final class Security {

    public static String encrypt(String secretKey, String input) {
        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(secretKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return urlEncode(Base64.encodeToString(crypted, Base64.DEFAULT));
    }

    public static String decrypt(String secretKey, String input) {
        input = urlDecode(input);
        byte[] output = null;
        String result = "";
        try {
            SecretKeySpec skey = new SecretKeySpec(secretKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(Base64.decode(input, Base64.DEFAULT));
            result = new String(output);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return result;
    }

    private static String urlEncode(String data) {
        return data.replace("+", "-").replace("/", "_").replace("=", ",");
    }

    private static String urlDecode(String data) {
        return data.replace("-", "+").replace("_", "/").replace(",", "=");
    }

    public static String SHA1(String text) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes(StandardCharsets.ISO_8859_1), 0, text.length());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] sha1hash = md != null ? md.digest() : new byte[0];
        return convertToHex(sha1hash);
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append(
                        (0 <= halfbyte) && (halfbyte <= 9)
                                ? (char) ('0' + halfbyte)
                                : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }
}