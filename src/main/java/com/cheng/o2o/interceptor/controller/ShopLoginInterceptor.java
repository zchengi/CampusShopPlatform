package com.cheng.o2o.interceptor.controller;

import com.cheng.o2o.entity.PersonInfo;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 店家管理系统登录验证拦截器
 *
 * @author cheng
 *         2018/4/17 15:59
 */
public class ShopLoginInterceptor implements HandlerInterceptor {

    /**
     * 用户操作前的代码，拦截用户操作
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {

        // 从session中获取用户信息
        Object userObj = request.getSession().getAttribute("user");
        if (userObj != null) {
            // 如果用户信息不为空则将session里的用户信息转换成 personInfo 实体类对象
            PersonInfo user = (PersonInfo) userObj;
            // 做空值判断，确保 userId 不为空且该帐号的可用状态为1，并且用户类型为商家
            if (user != null && user.getUserId() != null && user.getUserId() > 0 && user.getEnableStatus() == 1) {
                // 如果通过验证则返回true，拦截器返回true后，用户可以正常操作
                return true;
            }
        }

        // 如果不满足登录验证，则直接跳转到帐号登录页面
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<script>");
        out.println("window.open ('" + request.getContextPath() + "/local/login?userType=2','_self')");
        out.println("</script>");
        out.println("</html>");
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
