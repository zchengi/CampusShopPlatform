package com.cheng.o2o.dao;

import com.cheng.o2o.BaseTest;
import com.cheng.o2o.entity.ProductImg;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;


/**
 * ProductImgDao Tester.
 *
 * @author cheng
 * @version 1.1
 * @since <pre>04/05/2018</pre>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductImgDaoTest extends BaseTest {

    @Autowired
    private ProductImgDao productImgDao;

    @Test
    public void testABatchInsertProductImg() {
        // productId 为 1 的商品里添加两个详情图片记录
        ProductImg productImg1 = new ProductImg();
        ProductImg productImg2 = new ProductImg();
        List<ProductImg> productImgList = new ArrayList<>();

        productImg1.setImgAddr("图片1");
        productImg1.setImgDesc("测试图片1");
        productImg1.setPriority(1);
        productImg1.setCreateTime(new Date());
        productImg1.setProductId(1L);

        productImg2.setImgAddr("图片2");
        productImg2.setImgDesc("测试图片2");
        productImg2.setPriority(2);
        productImg2.setCreateTime(new Date());
        productImg2.setProductId(1L);

        productImgList.add(productImg1);
        productImgList.add(productImg2);

        int effectedNum = productImgDao.batchInsertProductImg(productImgList);
        assertEquals(2, effectedNum);
    }

    @Test
    public void testCDeleteProductImgByProductId() {
        // 删除新增的两条商品详情图片记录
        long productId = 1L;
        int effectedNum = productImgDao.deleteProductImgByProductId(productId);
        assertEquals(2, effectedNum);
    }

    @Test
    public void testBQueryProductImgList() {
        // 检查 productId 为1的商品是否有且仅有两张商品详情图片
        List<ProductImg> productImgList = productImgDao.queryProductImgList(1L);
        assertEquals(2, productImgList.size());
    }
}