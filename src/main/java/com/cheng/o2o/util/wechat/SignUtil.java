package com.cheng.o2o.util.wechat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 微信请求校验工具类
 *
 * @author cheng
 *         2018/4/13 19:01
 */
public class SignUtil {

    /**
     * 与接口配置信息中的 Token 要一致
     */
    private static final String TOKEN = "4D01FFF0CB94468EA907EF42780668AB";

    public static boolean checkSignature(String signature, String timestamp, String nonce) {

        String[] arr = {TOKEN, timestamp, nonce};
        // 将 TOKEN、timestamp、nonce 三个参数进行字典序排序
        Arrays.sort(arr);
        String content = Arrays.stream(arr).collect(Collectors.joining());

        MessageDigest md;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行 sha-1 加密
            byte[] digest = md.digest(content.getBytes());
            tmpStr = byteToString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // 将 sha-1 加密后的字符串可与 signature 对比，标识该请求来源于微信
        return tmpStr != null && tmpStr.equals(signature.toUpperCase());
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteToString(byte[] byteArray) {
        StringBuilder strDigest = new StringBuilder();
        for (byte aByteArray : byteArray) {
            strDigest.append(byteToHexStr(aByteArray));
        }

        return strDigest.toString();
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param myByte
     * @return
     */
    private static String byteToHexStr(byte myByte) {
        char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];

        tempArr[0] = digit[(myByte >>> 4) & 0X0F];
        tempArr[1] = digit[myByte & 0X0F];

        return new String(tempArr);
    }
}
