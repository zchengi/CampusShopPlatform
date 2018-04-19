package com.cheng.o2o.service.impl;

import com.cheng.o2o.dao.ProductDao;
import com.cheng.o2o.dao.ProductImgDao;
import com.cheng.o2o.dto.ImageHolder;
import com.cheng.o2o.dto.ProductExecution;
import com.cheng.o2o.entity.Product;
import com.cheng.o2o.entity.ProductImg;
import com.cheng.o2o.enums.ProductStateEnum;
import com.cheng.o2o.exceptions.ProductOperationException;
import com.cheng.o2o.service.ProductService;
import com.cheng.o2o.util.FileUtil;
import com.cheng.o2o.util.ImgUtil;
import com.cheng.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author cheng
 *         2018/4/5 23:35
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;
    @Autowired
    ProductImgDao productImgDao;

    /**
     * 1. 处理缩略图，获取缩略图相对路径并赋值给 product
     * 2. 往 tb_product 写入商品信息，获取 productId
     * 3. 结合 productId 批量处理商品详情图
     * 4. 将商品详情图列表批量插入 tb_product_img 表中
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList)
            throws ProductOperationException {

        // 空值判断
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            // 给商品设置默认属性
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            // 默认为上架的状态
            product.setEnableStatus(1);
            // 若商品缩略图不为空则添加
            if (thumbnail != null) {
                addThumbnail(product, thumbnail);
            }

            try {
                // 创建商品信息
                int effectedNum = productDao.insertProduct(product);
                if (effectedNum <= 0) {
                    throw new ProductOperationException("创建商品失败!");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建商品失败，" + e.toString());
            }

            // 若商品详情图不为空则添加
            if (productImgHolderList != null && productImgHolderList.size() > 0) {
                addProductImgList(product, productImgHolderList);
            }

            return new ProductExecution(ProductStateEnum.SUCCESS, product);

        } else {
            // 传参为空则返回空值错误信息
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    /**
     * 1. 若缩略图参数有值，则先处理缩略图；若原先存在缩略图则先删除再添加新图，之后获取缩略图相对路径并赋值给 product
     * 2. 若商品详情图列表参数有值，对商品详情图片列表进行同样的操作
     * 3. 将 tb_product_img 下面的该商品原先的商品详情图记录全部删除
     * 4. 更新 tb_product_img 、 tb_product 的信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList) throws ProductOperationException {
        // 空值判断
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            // 给商品添加默认属性
            product.setLastEditTime(new Date());
            // 如果商品缩略图不为空且原有缩略图不为空则删除原缩略图并添加
            if (thumbnail != null) {
                // 先获取一遍原有信息，因为原来的信息里有原图片地址
                Product tempProduct = productDao.queryProductByProductId(product.getProductId());
                if (tempProduct.getImgAddr() != null) {
                    ImgUtil.deleteFileOrPath(tempProduct.getImgAddr());
                }
                addThumbnail(product, thumbnail);
            }

            // 如果有新存入的商品详情图，则将原先的删除，并添加新的图片
            if (productImgHolderList != null && productImgHolderList.size() > 0) {
                deleteProductImgList(product.getProductId());
                addProductImgList(product, productImgHolderList);
            }
            try {
                // 更新商品信息
                int effectedNum = productDao.updateProduct(product);
                if (effectedNum <= 0) {
                    throw new ProductOperationException("更新商品信息失败");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS, product);
            } catch (Exception e) {
                throw new ProductOperationException("创建商品详情图失败：" + e.toString());
            }
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductByProductId(productId);
    }

    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        // 页码转换成数据库的行码，并调用dao层取回指定页码的商品列表
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
        // 基于同样的查询条件返回该查询条件下的商品总数
        int count = productDao.queryProductCount(productCondition);

        ProductExecution pe = new ProductExecution();
        pe.setProductList(productList);
        pe.setCount(count);
        return pe;
    }

    /**
     * 添加缩略图
     *
     * @param product
     * @param thumbnail
     */
    private void addThumbnail(Product product, ImageHolder thumbnail) {
        String desc = FileUtil.getShopImagePath(product.getShop().getShopId());
        String thumbnailAddr = ImgUtil.generateThumbnail(thumbnail, desc);
        product.setImgAddr(thumbnailAddr);
    }

    /**
     * 批量添加图片
     *
     * @param product
     * @param productImgHolderList
     */
    private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) {
        // 获取图片存储路径，这里直接存到相应店铺的文件夹下
        String desc = FileUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgList = new ArrayList<>();

        // 遍历图片依次去处理，并添加进 productImg 实体类中
        for (ImageHolder productImageHolder : productImgHolderList) {
            String imgAddr = ImgUtil.generateThumbnail(productImageHolder, desc);
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }

        // 如果确定是有图片需要添加的，就执行批量添加操作
        if (productImgList.size() > 0) {
            try {

                int effectedNum = productImgDao.batchInsertProductImg(productImgList);
                if (effectedNum <= 0) {
                    throw new ProductOperationException("创建商品详情图片失败!");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建商品详情图片失败，" + e.toString());
            }
        }
    }

    /**
     * 删除某个商品下的所有详情图
     *
     * @param productId
     */
    private void deleteProductImgList(Long productId) {
        // 根据 productId 获取原来的图片
        List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
        // 清空原来的图片
        for (ProductImg productImg : productImgList) {
            ImgUtil.deleteFileOrPath(productImg.getImgAddr());
        }
        // 删除数据库里原有图片的信息
        productImgDao.deleteProductImgByProductId(productId);
    }
}
