package hou.mymirror.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hou on 2016-1-8.
 */
public class Md5Util {

    public static String MD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(str.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0, length = array.length; i < length; i++) {
                stringBuffer.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
