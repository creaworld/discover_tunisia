/*
 * Copyright (c) 2017 Created by Pham Ngoc Thanh, TDC group
 */

package tip.tdc.com.tip.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by phamngocthanh on 2/20/17.
 */

public class StringUtils {

    /**
     *
     * @param s : input string, which have to convert
     * @param charset: "UTF-8", "ISO-8859-1",
     * @return : string convert successful or null object if havce exception
     */
    public static String convertString (String s, String charset) {
        try {
            byte[] byteArray = s.getBytes(charset);

//            ByteBuffer b = Charset.forName(charset).encode(s);
//            if(b.hasArray()) {
//                byteArray = b.array();
//            } else {
//                byteArray = new byte[b.remaining()];
//                b.get(byteArray);
//            }

            return new String(byteArray, Charset.forName(charset));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String sha256(String str) {
        return StringUtils.sha256(str, null);
    }

    /**
     * Hash a string using SHA-256, with 256-bit random key
     *
     * @param str
     * @return
     */
    public static String sha256(String str, byte[] random) {
        String sha256 = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte [] digest;

            if (random != null) {
                byte [] strBytes = str.getBytes("utf8");
                digest = new byte [strBytes.length + random.length];

                System.arraycopy(strBytes, 0, digest, 0, strBytes.length);
                System.arraycopy(random, 0, digest, strBytes.length, random.length);
            }
            else {
                digest = str.getBytes("utf8");
            }

            digest = md.digest(digest);

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (byte b: digest) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }

            sha256 = sb.toString();
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            // pass
        }

        return sha256;
    }

    static String [] loremStringArray = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.".split(" ");
    public static String randomString(int length){
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i<length; i++) {
            stringBuffer.append(loremStringArray[new Random().nextInt(loremStringArray.length-1)]);
            stringBuffer.append(" ");
        }

        return stringBuffer.toString();
    }
}
