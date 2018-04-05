package com.cheng.o2o.service.impl;

import com.cheng.o2o.dao.ProductCategoryDao;
import com.cheng.o2o.dto.ProductCategoryExecution;
import com.cheng.o2o.entity.ProductCategory;
import com.cheng.o2o.enums.ProductCategoryStateEnum;
import com.cheng.o2o.exceptions.ProductCategoryOperationException;
import com.cheng.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cheng
 *         2018/4/4 14:31
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Override
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) {
        if (productCategoryList != null && productCategoryList.size() > 0) {
            try {
                int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
                if (effectedNum <= 0) {
                    throw new ProductCategoryOperationException("店铺商品类别创建失败!");
                } else {
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                }
            } catch (Exception e) {
                throw new ProductCategoryOperationException("batchAddProductCategory" + e.getMessage());
            }
        } else {
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException {
        // TODO 将此类别下的商品里的类别id置为空
        try {
            int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
            if (effectedNum <= 0) {
                throw new ProductCategoryOperationException("商品类别删除失败!");
            } else {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        } catch (Exception e) {
            throw new ProductCategoryOperationException("deleteProductCategory error:" + e.getMessage());
        }
    }

    @Override
    public List<ProductCategory> getProductCategory(long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }
}
