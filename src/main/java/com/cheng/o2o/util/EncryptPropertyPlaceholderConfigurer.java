package com.cheng.o2o.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @author cheng
 *         2018/4/16 15:39
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    /**
     * 需要加密的字段数组
     */
    private String[] encryptPropNames = {"jdbc.master.username", "jdbc.master.password",
            "jdbc.slave.username", "jdbc.slave.password", "redis.password"};

    /**
     * 对关键的属性进行转换
     *
     * @param propertyName
     * @param propertyValue
     * @return
     */
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (isEncryptProp(propertyName)) {
            // 对已加密的字段进行解密操作
            return DESUtil.getDecryptString(propertyValue);
        } else {
            return propertyValue;
        }
    }

    /**
     * 该属性是否已经加密
     *
     * @param propertyName
     * @return
     */
    private boolean isEncryptProp(String propertyName) {
        // 若等于需要加密的 field，则进行加密
        for (String encryptPropName : encryptPropNames) {
            if (encryptPropName.equals(propertyName)) {
                return true;
            }
        }

        return false;
    }
}
