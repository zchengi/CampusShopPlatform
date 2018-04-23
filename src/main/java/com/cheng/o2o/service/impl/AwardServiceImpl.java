package com.cheng.o2o.service.impl;

import com.cheng.o2o.dao.AwardDao;
import com.cheng.o2o.dto.AwardExecution;
import com.cheng.o2o.dto.ImageHolder;
import com.cheng.o2o.entity.Award;
import com.cheng.o2o.enums.AwardStateEnum;
import com.cheng.o2o.exceptions.UserAwardMapOperationException;
import com.cheng.o2o.service.AwardService;
import com.cheng.o2o.util.FileUtil;
import com.cheng.o2o.util.ImgUtil;
import com.cheng.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author cheng
 *         2018/4/23 13:58
 */
@Service
public class AwardServiceImpl implements AwardService {

    @Autowired
    private AwardDao awardDao;


    /**
     * 1. 处理缩略图，获取缩略图相对路径并赋值给 award
     * 2. 在 tb_award 种写入奖品信息
     *
     * @param award
     * @param thumbnail
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AwardExecution addAward(Award award, ImageHolder thumbnail) {

        // 空值判断
        if (award != null && award.getShopId() != null) {
            // 给 award 赋值
            award.setCreateTime(new Date());
            award.setLastEditTime(new Date());
            // 默认可用
            award.setEnableStatus(1);

            if (thumbnail != null) {
                // 如果传入图片信息不为空则添加图片
                addThumbnail(award, thumbnail);
            }

            try {
                // 添加奖品信息
                int effectedNum = awardDao.insertAward(award);
                if (effectedNum < 0) {
                    throw new UserAwardMapOperationException("创建奖品失败");
                }
            } catch (Exception e) {
                throw new UserAwardMapOperationException("创建奖品失败 : " + e.toString());
            }

            return new AwardExecution(AwardStateEnum.SUCCESS, award);
        } else {
            return new AwardExecution(AwardStateEnum.NULL_AWARD_INFO);
        }
    }

    /**
     * 1. 如果有缩略图，处理缩略图
     * 2. 如果原先存在缩略图则先删除再添加新图，之后获取缩略图相对路径并赋值给 award
     * 3. 更新 tb_award 的信息
     *
     * @param award
     * @param thumbnail
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AwardExecution modifyAward(Award award, ImageHolder thumbnail) {

        // 空值判断
        if (award != null && award.getShopId() != null) {
            award.setLastEditTime(new Date());
            if (thumbnail != null) {
                // 如果存在原图则删除原图并添加新图
                Award tempAward = awardDao.queryAwardByAwardId(award.getAwardId());
                if (tempAward.getAwardImg() != null) {
                    ImgUtil.deleteFileOrPath(tempAward.getAwardImg());
                }

                addThumbnail(award, thumbnail);
            }

            try {
                int effectedNum = awardDao.updateAward(award);
                if (effectedNum < 0) {
                    throw new UserAwardMapOperationException("创建奖品失败");
                }
            } catch (Exception e) {
                throw new UserAwardMapOperationException("创建奖品失败 : " + e.toString());
            }
            
            return new AwardExecution(AwardStateEnum.SUCCESS, award);
        } else {
            return new AwardExecution(AwardStateEnum.NULL_AWARD_INFO);
        }
    }

    @Override
    public AwardExecution getAwardList(Award awardCondition, Integer pageIndex, Integer pageSize) {

        // 页转行
        int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        // 根据传入的查询条件分页返回用户积分列表信息
        List<Award> userAwardMapList = awardDao.
                queryAwardList(awardCondition, beginIndex, pageSize);
        // 返回总数
        int count = awardDao.queryAwardCount(awardCondition);

        AwardExecution ae = new AwardExecution();
        ae.setUserAwardMapList(userAwardMapList);
        ae.setCount(count);
        return ae;
    }

    @Override
    public Award getAwardById(long awardId) {
        return awardDao.queryAwardByAwardId(awardId);
    }

    /**
     * 存储图片流至指定路径并返回相对路径
     *
     * @param award
     * @param thumbnail
     */
    private void addThumbnail(Award award, ImageHolder thumbnail) {
        String desc = FileUtil.getShopImagePath(award.getShopId());
        String thumbnailAddr = ImgUtil.generateThumbnail(thumbnail, desc);
        award.setAwardImg(thumbnailAddr);
    }
}
