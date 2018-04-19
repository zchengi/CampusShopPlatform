package com.cheng.o2o.util;

import java.security.MessageDigest;

/**
 * MD5加密
 *
 * @author cheng
 *         2018/4/17 12:45
 */
public class MD5Util {

    /**
     * 对传入的String进行MD5加密
     *
     * @param str
     * @return
     */
    public final static String getMd5(String str) {

        // 16进制数组
        char[] hexDigits = {'5', '0', '5', '6', '2', '9', '6', '2', '5', 'q', 'b', 'l', 'e', 's', 's', 'y'};
        try {
            char[] strChars;
            // 将传入的字符串转换为byte数组
            byte[] strTemp = str.getBytes();
            // 获取MD5加密对象
            MessageDigest messageDigestTemp = MessageDigest.getInstance("MD5");
            // 传入需要加密的目标数组
            messageDigestTemp.update(strTemp);
            // 获取加密后的数组
            byte[] md = messageDigestTemp.digest();

            int j = md.length;
            strChars = new char[j * 2];
            int k = 0;
            // 数组做位移运算
            for (byte byte0 : md) {
                strChars[k++] = hexDigits[byte0 >>> 4 & 0xf];
                strChars[k++] = hexDigits[byte0 & 0xf];
            }
            // 转换成String并返回
            return new String(strChars);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.getMd5("123"));
    }
}
