package com.cheng.o2o.web.shopadmin;

import com.cheng.o2o.dto.AwardExecution;
import com.cheng.o2o.dto.ImageHolder;
import com.cheng.o2o.entity.Award;
import com.cheng.o2o.entity.Shop;
import com.cheng.o2o.enums.AwardStateEnum;
import com.cheng.o2o.service.AwardService;
import com.cheng.o2o.util.CodeUtil;
import com.cheng.o2o.util.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cheng
 *         2018/4/23 14:13
 */
@Controller
@RequestMapping("/shopadmin")
public class AwardManagementController {

    @Autowired
    private AwardService awardService;

    @PostMapping("/addaward")
    @ResponseBody
    private Map<String, Object> addAward(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>(4);
        // 验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码有误!");
            return modelMap;
        }

        // 接收前端的参数变量初始值
        ObjectMapper mapper = new ObjectMapper();
        Award award;
        ImageHolder thumbnail = null;
        String awardStr = HttpServletRequestUtil.getString(request, "awardStr");
        CommonsMultipartResolver multipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());

        try {
            if (multipartResolver.isMultipart(request)) {
                thumbnail = handleImage(request, thumbnail);
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        try {
            award = mapper.readValue(awardStr, Award.class);
        } catch (IOException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        if (award != null && thumbnail != null) {
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                award.setShopId(currentShop.getShopId());

                AwardExecution ae = awardService.addAward(award, thumbnail);
                if (ae.getState() == AwardStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", ae.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入奖品信息!");
        }

        return modelMap;
    }

    @PostMapping("/modifyaward")
    @ResponseBody
    private Map<String, Object> modifyAward(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>(4);
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        // 根据传入的状态值确定是否跳过验证码校验
        if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码有误!");
            return modelMap;
        }

        // 接收前端的参数变量初始值
        ObjectMapper mapper = new ObjectMapper();
        Award award;
        ImageHolder thumbnail = null;
        CommonsMultipartResolver multipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());

        try {
            if (multipartResolver.isMultipart(request)) {
                thumbnail = handleImage(request, thumbnail);
            }
        } catch (IOException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        try {
            String awardStr = HttpServletRequestUtil.getString(request, "awardStr");
            award = mapper.readValue(awardStr, Award.class);
        } catch (IOException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        if (award != null && award.getAwardId() != null) {
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                award.setShopId(currentShop.getShopId());
                AwardExecution ae = awardService.modifyAward(award, thumbnail);
                if (ae.getState() == AwardStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", ae.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入奖品信息!");
        }

        return modelMap;
    }

    @GetMapping("/listawardsbyshop")
    @ResponseBody
    private Map<String, Object> listAwardsByShop(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>(4);

        // 获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

        // 从session种获取当前店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

        // 空值判断
        if (pageIndex > -1 && pageSize > -1 && currentShop != null && currentShop.getShopId() != null) {
            // 传入查询条件
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            Award awardCondition = compactAwardCondition2Search(currentShop.getShopId(), awardName);

            // 分页获取该店铺下的顾客积分列表
            AwardExecution ae = awardService.getAwardList(awardCondition, pageIndex, pageSize);
            modelMap.put("awardList", ae.getUserAwardMapList());
            modelMap.put("count", ae.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Empty pageSize or pageIndex or shopId");
        }

        return modelMap;
    }

    /**
     * 通过商品id获取奖品信息
     *
     * @param request
     * @return
     */
    @GetMapping("/getawardbyid")
    @ResponseBody
    private Map<String, Object> getAwardById(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>(4);
        long awardId = HttpServletRequestUtil.getLong(request, "awardId");
        if (awardId > -1) {
            Award award = awardService.getAwardById(awardId);
            modelMap.put("award", award);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Empty awardId");
        }

        return modelMap;
    }


    /**
     * 图片处理
     *
     * @param request
     * @param thumbnail
     * @return
     * @throws IOException
     */
    private ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail) throws IOException {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 取出缩略图并构建 ImageHolder 对象
        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
        if (thumbnailFile != null) {
            thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
        }
        return thumbnail;
    }


    /**
     * 组合查询条件，将条件封装到 awardCondition 对象里返回
     *
     * @param shopId
     * @param awardName
     * @return
     */
    private Award compactAwardCondition2Search(Long shopId, String awardName) {

        Award awardCondition = new Award();
        awardCondition.setShopId(shopId);
        if (awardName != null) {
            awardCondition.setAwardName(awardName);
        }

        return awardCondition;
    }
}
