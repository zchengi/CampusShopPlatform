package com.cheng.o2o.web.local;

import com.cheng.o2o.dto.LocalAuthExecution;
import com.cheng.o2o.entity.LocalAuth;
import com.cheng.o2o.entity.PersonInfo;
import com.cheng.o2o.enums.LocalAuthStateEnum;
import com.cheng.o2o.exceptions.LocalAuthOperationException;
import com.cheng.o2o.service.LocalAuthService;
import com.cheng.o2o.util.CodeUtil;
import com.cheng.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cheng
 *         2018/4/17 13:16
 */
@Controller
@RequestMapping(value = "local", method = {RequestMethod.GET, RequestMethod.POST})
public class LocalAuthController {

    @Autowired
    private LocalAuthService localAuthService;

    /**
     * 将用户信息与平台帐号绑定
     *
     * @param request
     * @return
     */
    @PostMapping("/bindlocalauth")
    @ResponseBody
    private Map<String, Object> bindLocalAuth(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>(4);

        // 验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码有误!");
            return modelMap;
        }

        // 获取输入的帐号
        String username = HttpServletRequestUtil.getString(request, "username");
        // 获取输入的密码
        String password = HttpServletRequestUtil.getString(request, "password");
        // 从session种获取当前用户信息(用户一旦通过微信登录后，就能获取到用户的信息)
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");

        // 非空判断
        if (username != null && password != null && user != null && user.getUserId() != null) {
            // 创建 localAuth 对象并赋值
            LocalAuth localAuth = new LocalAuth();
            localAuth.setUsername(username);
            localAuth.setPassword(password);
            localAuth.setPersonInfo(user);

            // 绑定帐号
            LocalAuthExecution localAuthExecution = localAuthService.bindLocalAuth(localAuth);
            if (localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", localAuthExecution.getStateInfo());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名和密码不能为空!");
        }

        return modelMap;
    }

    @PostMapping("/changelocalpwd")
    @ResponseBody
    private Map<String, Object> changeLocalPwd(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>(4);
        // 验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码有误!");
            return modelMap;
        }

        // 获取帐号
        String username = HttpServletRequestUtil.getString(request, "username");
        // 获取原密码
        String password = HttpServletRequestUtil.getString(request, "password");
        // 获取新密码
        String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
        // 从session中获取当前用户信息(用户一旦通过微信登录之后，便能获取到用户的信息)
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");

        // 非空判断，要求帐号新旧密码以及当前的用户session非空，且新旧密码不相同
        if (username != null && password != null && newPassword != null &&
                user != null && user.getUserId() != null && !password.equals(newPassword)) {
            try {
                // 查看原先帐号，看看与输入的帐号是否一致，不一致则认为是非法操作
                LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
                if (localAuth == null || !localAuth.getUsername().equals(username)) {
                    // 不一致则直接退出
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "输入的帐号非本次登录的帐号!");
                    return modelMap;
                }

                // 修改密码
                LocalAuthExecution localAuthExecution = localAuthService.modifyLocalAuth(
                        user.getUserId(), username, password, newPassword);

                if (localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                    // 修改成功
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", localAuthExecution.getStateInfo());
                }
            } catch (LocalAuthOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入密码!");
        }
        return modelMap;
    }

    /**
     * 用户登录
     *
     * @param request
     * @return
     */
    @PostMapping("/logincheck")
    @ResponseBody
    private Map<String, Object> loginCheck(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>(4);
        // 获取是否需要进行验证码校验的标识符
        boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
        if (needVerify && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码有误!");
            return modelMap;
        }

        // 获取输入的帐号
        String username = HttpServletRequestUtil.getString(request, "username");
        // 获取输入的密码
        String password = HttpServletRequestUtil.getString(request, "password");
        // 非空校验
        if (username != null && password != null) {
            // 传入帐号和密码去获取平台帐号信息
            LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(username, password);
            if (localAuth != null) {
                // 如果能获取到帐号信息则登录成功
                modelMap.put("success", true);
                // 在session中设置用户信息
                request.getSession().setAttribute("user", localAuth.getPersonInfo());
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "用户名或密码错误!");
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名和密码均不能为空!");
        }

        return modelMap;
    }

    /**
     * 当用户点击登出按钮的时候注销session
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    @ResponseBody
    private Map<String, Object> logOut(HttpServletRequest request) {

        Map<String, Object> modeMap = new HashMap<>(4);

        // 将用户session清空
        request.getSession().setAttribute("user", null);
        modeMap.put("success", true);
        return modeMap;
    }
}
