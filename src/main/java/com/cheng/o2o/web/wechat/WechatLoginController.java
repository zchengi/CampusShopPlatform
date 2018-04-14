package com.cheng.o2o.web.wechat;

import com.cheng.o2o.dto.UserAccessToken;
import com.cheng.o2o.dto.WechatUser;
import com.cheng.o2o.util.wechat.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取关注公众号之后的微信用户信息的接口，如果在微信浏览器里访问
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd7f6c5b8899fba83
 * &redirect_uri=http://193.112.56.145/o2o/wechatlogin/logincheck&role_type=1
 * &response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 * 则这里将会获取到code,之后再可以通过code获取到access_token 进而获取到用户信息
 *
 * @author cheng
 *         2018/4/13 19:47
 */
@Controller
@RequestMapping("/wechatlogin")
public class WechatLoginController {

    private static Logger logger = LoggerFactory.getLogger(WechatLoginController.class);


    @GetMapping("/logincheck")
    public String doGet(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("wechat login get...");

        // 获取微信公众号传过来的 code，通过code可获取access_token，进而获取用户信息
        String code = request.getParameter("code");
        // 这个state可以用来传我们自定义的信息，方便程序调用，这里也可以不用
        // String roleType = request.getParameter("state");
        logger.debug("wechat login code : " + code);

        WechatUser user = null;
        String openId;
        if (code != null) {
            UserAccessToken token;
            // 通过 code 获取 access_token
            token = WechatUtil.getUserAccessToken(code);
            logger.debug("wechat login token : " + token.toString());

            // 通过 token 获取 accessToken
            String accessToken = token.getAccessToken();

            // 通过 token 获取 openId
            openId = token.getOpenId();

            // 通过 access_token 和 openId 获取用户昵称等信息
            user = WechatUtil.getUserInfo(accessToken, openId);
            logger.debug("wechat login user : " + user.toString());
            request.getSession().setAttribute("openId", openId);
        }

        /*
         *
         *      TODO
         * 获取到openId后，可以通过它去数据库判断该微信帐号是否在网站上有对应的帐号，
         * 如果没有这里可以自动创建上，实现微信与网站的无缝对接
         *
         */

        if (user != null) {
            // 获取到微信验证的信息后返回到指定的路由
            return "frontend/index";
        } else {
            return null;
        }
    }
}
