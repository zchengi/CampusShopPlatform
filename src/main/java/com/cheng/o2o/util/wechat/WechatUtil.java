package com.cheng.o2o.util.wechat;

import com.cheng.o2o.dto.UserAccessToken;
import com.cheng.o2o.dto.WechatUser;
import com.cheng.o2o.entity.PersonInfo;
import com.cheng.o2o.web.wechat.MyX509TrustManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

/**
 * 微信工具类
 *
 * @author cheng
 *         2018/4/13 23:28
 */
public class WechatUtil {

    /**
     * get 请求
     */
    private static final String GET_REQUEST_METHOD = "GET";
    /**
     * 公众号的appId
     */
    private static final String APP_ID = "wx6e0e8bdbe4936a17";
    /**
     * 公众号信息里的 appSecret
     */
    private static final String APP_SECRET = "2dce14ecc708569e98a947e835e44a8d";

    private static Logger logger = LoggerFactory.getLogger(WechatUtil.class);


    /**
     * 获取 UserAccessToken 实体类
     *
     * @param code
     * @return
     */
    public static UserAccessToken getUserAccessToken(String code) {

        // 公众号信息里的 appId
        logger.debug("appId : " + APP_ID);

        // 公众号信息里的 appSecret
        logger.debug("secret : " + APP_SECRET);

        // 根据传入的 code，拼接出访问微信定义好的接口的 URL
        String url = "https://api.weixin.qq.com/cgi-bin/token?" +
                "appid=" + APP_ID + "&secret=" + APP_SECRET + "&code=" + code + "&grant_type=authorization_code";

        // 向相应的 URL 发送请求获取 token json 字符串
        String tokenStr = httpsRequest(url, GET_REQUEST_METHOD, null);
        logger.debug("userAccessToken : " + tokenStr);

        UserAccessToken token = new UserAccessToken();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 将 json 字符串转换为相应对象
            token = objectMapper.readValue(tokenStr, UserAccessToken.class);
        } catch (IOException e) {
            logger.error("获取用户accessToken失败: " + e.getMessage());
            e.printStackTrace();
        }

        if (token == null) {
            logger.error("获取用户accessToken失败.");
            return null;
        }
        return token;
    }

    /**
     * 获取 wechatUser 实体类
     *
     * @param accessToken
     * @param openId
     * @return
     */
    public static WechatUser getUserInfo(String accessToken, String openId) {
        // 根据传入的 accessToken 以及 openId 拼接出访问微信定义的端口并获取用户的 URL
        String url = "";
        // 访问该 URL 获取用户信息 json 字符串
        String userStr = httpsRequest(url, GET_REQUEST_METHOD, null);
        logger.debug("user info : " + userStr);
        WechatUser user = new WechatUser();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            user = objectMapper.readValue(userStr, WechatUser.class);
        } catch (IOException e) {
            logger.error("获取用户信息失败: " + e.getMessage());
            e.printStackTrace();
        }

        if (user == null) {
            logger.error("获取用户信息失败.");
            return null;
        }
        return user;
    }

    /**
     * 发起 https 请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式(GET、POST)
     * @param outputStr     提交的数据
     * @return json 字符串
     */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {

        StringBuilder buffer = new StringBuilder();
        try {
            // 创建 SSLContext 对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new SecureRandom());

            // 从上述 SSLContext 对象中得到 SSLSocketFactory 对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);
            if (GET_REQUEST_METHOD.equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            httpUrlConn.disconnect();
            logger.debug("https buffer:" + buffer.toString());

        } catch (NoSuchAlgorithmException | NoSuchProviderException | KeyManagementException | IOException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }


    /**
     * 将 wechatUser 里的信息转换成 PersonInfo 的信息并返回 PersonInfo 实体类
     *
     * @param user
     * @return
     */
    public static PersonInfo getPersonInfoFromRequest(WechatUser user) {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName(user.getNickName());
        personInfo.setGender(user.getSex() + "");
        personInfo.setProfileImg(user.getHeadImgUrl());
        personInfo.setEnableStatus(1);
        return personInfo;
    }
}
