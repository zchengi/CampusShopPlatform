package com.cheng.o2o.entity;

import java.util.Date;
import java.util.List;

/**
 * 商品实体类
 *
 * @author cheng
 *         2018/3/26 23:18
 */
public class Product {

    /**
     * id
     */
    private Long productId;
    /**
     * 名称
     */
    private String productName;
    /**
     * 描述
     */
    private String productDesc;
    /**
     * 简略图
     */
    private String imgAddr;
    /**
     * 原价
     */
    private String normalPrice;
    /**
     * 促销价
     */
    private String promotionPrice;
    /**
     * 权重
     */
    private Integer priority;
    /**
     * 商品积分
     */
    private Integer point;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date lastEditTime;
    /**
     * 状态
     * 0:下架,1:上架
     */
    private Integer enableStatus;
    /**
     * 商品详情图片列表,跟商品是多对一的关系
     */
    private List<ProductImg> productImgList;
    /**
     * 商品所属类别，一件商品仅属于一个商品类别
     */
    private ProductCategory productCategory;
    /**
     * 商品所属店铺，标明商品属于哪个店铺
     */
    private Shop shop;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getImgAddr() {
        return imgAddr;
    }

    public void setImgAddr(String imgAddr) {
        this.imgAddr = imgAddr;
    }

    public String getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(String normalPrice) {
        this.normalPrice = normalPrice;
    }

    public String getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(String promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public List<ProductImg> getProductImgList() {
        return productImgList;
    }

    public void setProductImgList(List<ProductImg> productImgList) {
        this.productImgList = productImgList;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
