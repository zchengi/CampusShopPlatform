package com.cheng.o2o.dao;

import com.cheng.o2o.entity.ProductImg;

import java.util.List;

/**
 * @author cheng
 *         2018/4/5 22:01
 */
public interface ProductImgDao {

    /**
     * 批量插入商品详情图片
     *
     * @param productImgList
     * @return
     */
    int batchInsertProductImg(List<ProductImg> productImgList);

    /**
     * 删除当前商品的所有详情图片
     *
     * @param productId
     * @return
     */
    int deleteProductImgByProductId(long productId);

    /**
     * 查询当前商品下的所有详情图片
     *
     * @param productId
     * @return
     */
    List<ProductImg> queryProductImgList(long productId);
}
