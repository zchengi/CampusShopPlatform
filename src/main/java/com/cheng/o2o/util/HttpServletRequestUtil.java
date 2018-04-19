package com.cheng.o2o.util;

import javax.servlet.http.HttpServletRequest;

/**
 * http请求类型转换工具类
 *
 * @author cheng
 *         2018/3/29 18:36
 */
public class HttpServletRequestUtil {

    public static int getInt(HttpServletRequest request, String name) {
        try {
            return Integer.decode(request.getParameter(name));
        } catch (Exception e) {
            return -1;
        }
    }

    public static Long getLong(HttpServletRequest request, String name) {
        try {
            return Long.valueOf(request.getParameter(name));
        } catch (Exception e) {
            return -1L;
        }
    }

    public static Double getDouble(HttpServletRequest request, String name) {
        try {
            return Double.valueOf(request.getParameter(name));
        } catch (Exception e) {
            return -1D;
        }
    }

    public static Boolean getBoolean(HttpServletRequest request, String name) {
        try {
            return Boolean.valueOf(request.getParameter(name));
        } catch (Exception e) {
            return false;
        }
    }

    public static String getString(HttpServletRequest request, String name) {
        try {
            String result = request.getParameter(name);
            if (request != null) {
                result = result.trim();
            }
            if ("".equals(result)) {
                result = null;
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}


