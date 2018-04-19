package com.cheng.o2o.util;

/**
 * 分页转换
 *
 * @author cheng
 *         2018/4/3 12:42
 */
public class PageCalculator {
    public static int calculateRowIndex(int pageIndex, int pageSize) {
        return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;
    }
}
