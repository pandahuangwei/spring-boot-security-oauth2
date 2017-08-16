package com.panda.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author panda.
 * @since 2017-07-20 20:32.
 */
public class EncryptUtil {

    public static String encodeMD5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) {
        System.out.println(encodeMD5("12345678"));
        //12345678->25d55ad283aa400af464c76d713c07ad
    }
}
