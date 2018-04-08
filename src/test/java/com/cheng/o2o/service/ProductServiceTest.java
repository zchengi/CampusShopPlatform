package com.cheng.o2o.service;

import com.cheng.o2o.BaseTest;
import com.cheng.o2o.dto.ImageHolder;
import com.cheng.o2o.dto.ProductExecution;
import com.cheng.o2o.entity.Product;
import com.cheng.o2o.entity.ProductCategory;
import com.cheng.o2o.entity.Shop;
import com.cheng.o2o.enums.ProductStateEnum;
import com.cheng.o2o.exceptions.ProductOperationException;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * ProductService Tester.
 *
 * @author cheng
 * @version 1.1
 * @since <pre>04/06/2018</pre>
 */

public class ProductServiceTest extends BaseTest {

    @Autowired
    private ProductService productService;

    @Test
    @Ignore
    public void testAddProduct() throws ProductOperationException, FileNotFoundException {
        //创建 shopId 为 1 且 productCategoryId 为 1 的商品实例并给其成员变量赋值
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(1L);
        product.setShop(shop);
        product.setProductCategory(pc);
        product.setProductName("测试商品a");
        product.setProductDesc("测试商品a");
        product.setPriority(20);
        product.setCreateTime(new Date());
        product.setEnableStatus(ProductStateEnum.SUCCESS.getState());

        // 创建缩略图文件流
        File thumbnailFile = new File("D:/IntelliJProject/O2O/target/test-classes/img/002.png");
        InputStream is = new FileInputStream(thumbnailFile);
        ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), is);
        // 创建两个商品详情图文件流并将它们添加到详情图列表中
        File productImg1 = new File("D:/IntelliJProject/O2O/target/test-classes/img/002.png");
        InputStream is1 = new FileInputStream(productImg1);
        File productImg2 = new File("D:/IntelliJProject/O2O/target/test-classes/img/002.png");
        InputStream is2 = new FileInputStream(productImg2);
        List<ImageHolder> productImgList = new ArrayList<>();
        productImgList.add(new ImageHolder(productImg1.getName(), is1));
        productImgList.add(new ImageHolder(productImg2.getName(), is2));

        // 添加商品并验证
        ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
        assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
    }

    @Test
    public void testModifyProduct() throws ProductOperationException, FileNotFoundException {
        // 创建 shopId 为 1且 productCategoryId 为1的商品实例并给其他成员变量赋值
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(1L);
        product.setProductId(1L);
        product.setShop(shop);
        product.setProductCategory(pc);
        product.setProductName("正式的商品");
        product.setProductDesc("正式的商品");

        // 创建缩略图文件流
        File thumbnailFile = new File("D:/IntelliJProject/O2O/target/test-classes/img/002.png");
        InputStream is = new FileInputStream(thumbnailFile);
        ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), is);
        // 创建两个商品详情图文件流并将它们添加到详情图列表中
        File productImg1 = new File("D:/IntelliJProject/O2O/target/test-classes/img/002.png");
        InputStream is1 = new FileInputStream(productImg1);
        File productImg2 = new File("D:/IntelliJProject/O2O/target/test-classes/img/002.png");
        InputStream is2 = new FileInputStream(productImg2);
        List<ImageHolder> productImgList = new ArrayList<>();
        productImgList.add(new ImageHolder(productImg1.getName(), is1));
        productImgList.add(new ImageHolder(productImg2.getName(), is2));

        // 添加商品并验证
        ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
        assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
    }
}
