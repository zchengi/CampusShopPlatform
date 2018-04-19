package com.cheng.o2o.web.frontend;

import com.cheng.o2o.entity.HeadLine;
import com.cheng.o2o.entity.ShopCategory;
import com.cheng.o2o.service.HeadLineService;
import com.cheng.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cheng
 *         2018/4/10 19:42
 */
@Controller
@RequestMapping("/frontend")
public class MainPageController {

    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private HeadLineService headLineService;

    /**
     * 初始化前端展示系统的主页信息，包括获取一级店铺类别列表以及头条列表
     *
     * @return
     */
    @GetMapping("/listmainpageinfo")
    @ResponseBody
    public Map<String, Object> listMainPageInfo() {

        Map<String, Object> modelMap = new HashMap<>(4);
        List<ShopCategory> shopCategoryList;
        try {
            // 获取一级店铺类别列表(即parentId为空的ShopCategory)
            shopCategoryList = shopCategoryService.getShopCategoryList(null);
            modelMap.put("shopCategoryList", shopCategoryList);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        List<HeadLine> headLineList;
        try {
            // 获取状态为可用(1)的头条列表
            HeadLine headLineCondition = new HeadLine();
            headLineCondition.setEnableStatus(1);
            headLineList = headLineService.getHeadLineList(headLineCondition);
            modelMap.put("headLineList", headLineList);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        modelMap.put("success", true);
        return modelMap;
    }


}
