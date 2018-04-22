package com.cheng.o2o.dto;

import java.util.HashSet;
import java.util.TreeSet;

/**
 * echart 中的 xAxis 项
 *
 * @author cheng
 *         2018/4/22 22:14
 */
public class EchartXAxis {

    private String type = "category";

    /**
     * 去重，且有序
     */
    private TreeSet<String> data;

    public String getType() {
        return type;
    }


    public TreeSet<String> getData() {
        return data;
    }

    public void setData(TreeSet<String> data) {
        this.data = data;
    }
}
