package com.cheng.o2o.web.wechat;

import com.cheng.o2o.dto.UserAccessToken;
import com.cheng.o2o.dto.WechatAuthExecution;
import com.cheng.o2o.dto.WechatUser;
import com.cheng.o2o.entity.PersonInfo;
import com.cheng.o2o.entity.WeChatAuth;
import com.cheng.o2o.enums.WechatAuthStateEnum;
import com.cheng.o2o.service.PersonInfoService;
import com.cheng.o2o.service.WechatAuthService;
import com.cheng.o2o.util.wechat.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取关注公众号之后的微信用户信息的接口，如果在微信浏览器里访问
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0db09a35eac8319e&
 * redirect_uri=http://193.112.56.145/o2o/wechatlogin/logincheck&role_type=1&
 * response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 * 则这里将会获取到code,之后再可以通过code获取到access_token 进而获取到用户信息
 *
 * @author cheng
 *         2018/4/13 19:47
 */
@Controller
@RequestMapping("/wechatlogin")
public class WechatLoginController {

    private static Logger logger = LoggerFactory.getLogger(WechatLoginController.class);
    private static final String FRONT_END = "1";
    private static final String SHOP_END = "2";

    @Autowired
    private PersonInfoService personInfoService;
    @Autowired
    private WechatAuthService wechatAuthService;

    @GetMapping("/logincheck")
    public String doGet(HttpServletRequest request, HttpServletResponse response) {

        logger.debug("wechat login get...");

        // 获取微信公众号传过来的 code，通过code可获取access_token，进而获取用户信息
        String code = request.getParameter("code");
        // 这个state可以用来传我们自定义的信息，方便程序调用，这里也可以不用
        String roleType = request.getParameter("state");
        logger.debug("wechat login code : " + code);

        WechatUser user;
        String openId;
        WeChatAuth weChatAuth;
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

            weChatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
        } else {
            throw new IllegalArgumentException("code cannot be null");
        }

        if (weChatAuth == null) {
            PersonInfo personInfo = WechatUtil.getPersonInfoFromRequest(user);
            weChatAuth = new WeChatAuth();
            weChatAuth.setOpenId(openId);
            personInfo.setUserType(FRONT_END.equals(roleType) ? 1 : 2);
            weChatAuth.setPersonInfo(personInfo);

            // 用户注册
            WechatAuthExecution we = wechatAuthService.register(weChatAuth);
            if (we.getState() == WechatAuthStateEnum.SUCCESS.getState()) {
                //  获取用户信息
                personInfo = personInfoService.getPersonInfoById(weChatAuth.getPersonInfo().getUserId());
                request.getSession().setAttribute("user", personInfo);
            } else {
                return null;
            }
        }

        if (FRONT_END.equals(roleType)) {
            // 若用户点击的是前端展示系统按钮则进入前端展示系统
            return "frontend/index";
        } else {
            return "shopadmin/shoplist";
        }
    }
}
