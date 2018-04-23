package com.cheng.o2o.web.frontend;

import com.cheng.o2o.dto.AwardExecution;
import com.cheng.o2o.entity.Award;
import com.cheng.o2o.entity.PersonInfo;
import com.cheng.o2o.entity.UserShopMap;
import com.cheng.o2o.service.AwardService;
import com.cheng.o2o.service.UserShopMapService;
import com.cheng.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cheng
 *         2018/4/23 16:02
 */
@Controller
@RequestMapping("/frontend")
public class ShopAwardController {

    @Autowired
    private AwardService awardService;
    @Autowired
    private UserShopMapService userShopMapService;

    /**
     * 获取店铺奖品信息
     *
     * @param request
     * @return
     */
    @GetMapping("/listawardsbyshop")
    @ResponseBody
    private Map<String, Object> listAwardsByShop(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>(4);

        // 获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

        // 从session种获取当前店铺信息
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");

        // 空值判断
        if (pageIndex > -1 && pageSize > -1 && shopId > -1) {
            // 传入查询条件
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            Award awardCondition = compactAwardCondition2Search(shopId, awardName);

            // 分页获取该店铺下的顾客积分列表
            AwardExecution ae = awardService.getAwardList(awardCondition, pageIndex, pageSize);
            modelMap.put("awardList", ae.getUserAwardMapList());
            modelMap.put("count", ae.getCount());
            modelMap.put("success", true);

            // 从session中获取用户信息
            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            // 空值判断
            if (user != null && user.getUserId() != null) {
                UserShopMap userShopMap = userShopMapService.getUserShopMap(user.getUserId(), shopId);
                if (userShopMap == null) {
                    modelMap.put("totalPoint", 0);
                } else {
                    modelMap.put("totalPoint", userShopMap.getPoint());
                }
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Empty pageSize or pageIndex or shopId");
        }

        return modelMap;
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
