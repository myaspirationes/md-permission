package com.yodoo.megalodon.permission.util;

import java.security.MessageDigest;

/**
 * @Date 2019/6/10 20:03
 * @author  by houzhen
 */
public class Md5Util {

    /**
     * 这里主要是遍历8个byte，转化为16位进制的字符，即0-F
     *
     * @param byteArr
     * @return
     */
    private static String byteArrayToHexString(byte[] byteArr) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < byteArr.length; i++) {
            resultSb.append(byteToHexString(byteArr[i]));
        }
        return resultSb.toString();
    }

    /**
     * 这里是针对单个byte，256的byte通过16拆分为d1和d2
     *
     * @param b
     * @return
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS_ARR[d1] + HEX_DIGITS_ARR[d2];
    }

    private static final String[] HEX_DIGITS_ARR = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 返回大写MD5
     *
     * @param origin
     * @return
     */
    public static String md5Encode(String origin) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes("utf-8")));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return resultString.toUpperCase();
    }

}