package com.cheng.o2o.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

/**
 * DES 是一种对称加密算法。
 * 对称加密：加密和解密使用相同的密钥的算法
 *
 * @author cheng
 *         2018/4/16 15:17
 */
public class DESUtil {

    private static Key key;

    /**
     * 设置密钥 key
     */
    private static final String KEY_STR = "chengKey";

    private static final String CHARSET = "UTF-8";
    private static final String DES_ALGORITHM = "DES";
    private static final String SHA1_ALGORITHM = "SHA1PRNG";


    static {
        try {
            // 生成DES算法对象
            KeyGenerator generator = KeyGenerator.getInstance(DES_ALGORITHM);
            // 运用SHA1安全策略
            SecureRandom secureRandom = SecureRandom.getInstance(SHA1_ALGORITHM);
            // 设置上密钥种子
            secureRandom.setSeed(KEY_STR.getBytes());
            // 初始化基于SHA1的算法对象
            generator.init(secureRandom);
            // 生成密钥对象
            key = generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取加密后的信息
     *
     * @param str
     * @return
     */
    public static String getEncryptString(String str) {
        // 基于BASE64编码，接收byte[]并转换成String
        BASE64Encoder base64Encoder = new BASE64Encoder();
        try {

            // 按UTF8编码
            byte[] bytes = str.getBytes(CHARSET);
            // 获取加密对象
            Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
            // 初始化密码信息
            cipher.init(Cipher.ENCRYPT_MODE, key);

            // 加密
            byte[] doFinal = cipher.doFinal(bytes);

            // byte[] to encode好的String并返回
            return base64Encoder.encode(doFinal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取解密之后的信息
     *
     * @param str
     * @return
     */
    public static String getDecryptString(String str) {

        // 基于BASE64，接受byte[]并转换成String
        BASE64Decoder base64Decoder = new BASE64Decoder();
        try {
            // 将字符串decode成byte[]
            byte[] bytes = base64Decoder.decodeBuffer(str);
            // 获取解密对象
            Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
            // 初始化解密信息
            cipher.init(Cipher.DECRYPT_MODE, key);
            // 解密
            byte[] doFinal = cipher.doFinal(bytes);
            // 返回解密之后的信息
            return new String(doFinal, CHARSET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(getEncryptString("1"));
        System.out.println(getEncryptString("2"));
        System.out.println(getEncryptString("3"));
        System.out.println(getEncryptString("4"));
    }
}
