package com.cheng.o2o.dto;

import com.cheng.o2o.entity.Product;
import com.cheng.o2o.enums.ProductStateEnum;

import java.util.List;

/**
 * @author cheng
 *         2018/4/5 23:04
 */
public class ProductExecution {

    /**
     * 结果状态
     */
    private int state;

    /**
     * 状态标识
     */
    private String stateInfo;

    /**
     * 商品数量
     */
    private int count;

    /**
     * 操作的product(增删改商品的时候使用)
     */
    private Product product;

    /**
     * 获取的product列表(查询商品列表的时候使用)
     */
    private List<Product> productList;

    public ProductExecution() {
    }

    /**
     * 商品操作失败时使用的构造器
     */
    public ProductExecution(ProductStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 商品操作成功时使用的构造器
     */
    public ProductExecution(ProductStateEnum stateEnum, Product product) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.product = product;
    }

    /**
     * 商品操作成功时使用的构造器
     */
    public ProductExecution(ProductStateEnum stateEnum, List<Product> productList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.productList = productList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
