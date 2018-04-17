package com.cheng.o2o.interceptor.controller;

import com.cheng.o2o.entity.Shop;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 店家管理系统操作验证拦截器
 *
 * @author cheng
 *         2018/4/17 16:12
 */
public class ShopPermissionInterceptor implements HandlerInterceptor {

    /**
     * 用户操作提交后拦截，未处理请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {

        // 从session种获取当前选择的店铺
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

        // 从session中获取当前用户可操作的店铺列表
        @SuppressWarnings("unchecked")
        List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");

        // 非空判断
        if (currentShop != null && shopList != null) {
            // 遍历可操作的店铺列表
            for (Shop shop : shopList) {
                // 如果当前店铺在可操作的店铺列表里则返回 true
                if (shop.getShopId().equals(currentShop.getShopId())) {
                    return true;
                }
            }
        }

        // 不满足拦截器的验证则返回false，终止用户的操作执行
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object object, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object object, Exception e) throws Exception {
    }
}
