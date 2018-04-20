package com.cheng.o2o.dao;

import com.cheng.o2o.BaseTest;
import com.cheng.o2o.entity.Award;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * AwardDao Tester.
 *
 * @author cheng
 * @version 1.0
 * @since <pre>04/20/2018</pre>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AwardDaoTest extends BaseTest {

    @Autowired
    private AwardDao awardDao;

    /**
     * 测试添加功能
     */
    @Test
    public void testAInsertAward() {

        long shopId = 1;
        // 创建奖品一
        Award award1 = new Award();
        award1.setAwardName("测试1");
        award1.setAwardImg("test1");
        award1.setPoint(5);
        award1.setPriority(1);
        award1.setEnableStatus(1);
        award1.setCreateTime(new Date());
        award1.setShopId(shopId);
        int effectedNum = awardDao.insertAward(award1);
        assertEquals(1, effectedNum);

        // 创建奖品二
        Award award2 = new Award();
        award2.setAwardName("测试2");
        award2.setAwardImg("test2");
        award2.setPoint(5);
        award2.setPriority(2);
        award2.setEnableStatus(2);
        award2.setCreateTime(new Date());
        award2.setShopId(shopId);
        effectedNum = awardDao.insertAward(award2);
        assertEquals(1, effectedNum);
    }

    /**
     * 测试删除功能
     */
    @Test
    public void testEDeleteAward() {
        Award awardCondition = new Award();
        awardCondition.setAwardName("测试");
        // 查询所有测试奖品并删除
        List<Award> awardList = awardDao.queryAwardList(awardCondition, 0, 2);
        assertEquals(2, awardList.size());

        for (Award award : awardList) {
            int effectedNum = awardDao.deleteAward(award.getAwardId(), award.getShopId());
            assertEquals(1, effectedNum);
        }

    }

    /**
     * 测试更新功能
     */
    @Test
    public void testDUpdateAward() {
        Award awardCondition = new Award();
        awardCondition.setAwardName("测试1");
        List<Award> awardList = awardDao.queryAwardList(awardCondition, 0, 1);

        // 修改奖品名称
        awardList.get(0).setAwardName("测试 ce shi 1");
        int effectedNum = awardDao.updateAward(awardList.get(0));
        assertEquals(1, effectedNum);

        // 查找修改后的奖品
        Award award = awardDao.queryAwardByAwardId(awardList.get(0).getAwardId());
        assertEquals("测试 ce shi 1", award.getAwardName());
    }

    /**
     * 测试查询列表功能
     */
    @Test
    public void testBQueryAwardList() {

        Award award = new Award();
        List<Award> awardList = awardDao.queryAwardList(award, 0, 3);
        assertEquals(2, awardList.size());

        int count = awardDao.queryAwardCount(award);
        assertEquals(2, count);

        award.setAwardName("测试");
        awardList = awardDao.queryAwardList(award, 0, 3);
        assertEquals(2, awardList.size());

        count = awardDao.queryAwardCount(award);
        assertEquals(2, count);
    }


    /**
     * 测试按照id查询功能
     */
    @Test
    public void testCQueryAwardByAwardId() {
        Award awardCondition = new Award();
        awardCondition.setAwardName("测试1");
        // 按照特定名字查询反返回特定的奖品
        List<Award> awardList = awardDao.queryAwardList(awardCondition, 0, 1);
        assertEquals(1, awardList.size());

        // 通过特定名字查询返回的特定奖品的id 去测试方法
        Award award = awardDao.queryAwardByAwardId(awardList.get(0).getAwardId());
        assertEquals("测试1", award.getAwardName());
    }
}