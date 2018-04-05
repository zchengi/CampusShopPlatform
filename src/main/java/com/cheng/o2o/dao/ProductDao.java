package com.cheng.o2o.dao;

import com.cheng.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cheng
 *         2018/4/5 21:56
 */
public interface ProductDao {

    /**
     * 插入商品
     *
     * @param product
     * @return
     */
    int insertProduct(Product product);

    /**
     * 更新商品信息
     *
     * @param product
     * @return
     */
    int updateProduct(Product product);

    /**
     * 查询商品列表并分页，可输入的查询条件有：商品名(模糊)，商品状态，店铺id，商品类别
     *
     * @param productCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<Product> queryProductList(@Param("productCondition") Product productCondition,
                                   @Param("rowIndex") int rowIndex,
                                   @Param("pageSize") int pageSize);

    /**
     * 查询对应的商品总数
     *
     * @param productCondition
     * @return
     */
    int queryProductCount(@Param("productCondition") Product productCondition);
}
