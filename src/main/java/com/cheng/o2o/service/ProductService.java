package com.cheng.o2o.service;

import com.cheng.o2o.dto.ImageHolder;
import com.cheng.o2o.dto.ProductExecution;
import com.cheng.o2o.entity.Product;
import com.cheng.o2o.exceptions.ProductOperationException;

import java.util.List;

/**
 * @author cheng
 *         2018/4/5 23:03
 */
public interface ProductService {

    /**
     * 添加商品信息以及图片处理
     *
     * @param product
     * @param thumbnail
     * @param productImgHolderList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList)
            throws ProductOperationException;

    /**
     * 修改商品信息以及图片处理
     *
     * @param product
     * @param thumbnail
     * @param productImgHolderList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList)
            throws ProductOperationException;

    /**
     * 通过商品id查询唯一的商品信息
     *
     * @param productId
     * @return
     */
    Product getProductById(long productId);
}
