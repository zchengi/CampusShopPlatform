package com.cheng.o2o.dto;

import java.util.List;

/**
 * echart 中的 series 项
 * @author cheng
 *         2018/4/22 22:19
 */
public class EchartSeries {

    private String type = "bar";
    private String name;
    private List<Integer> data;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
