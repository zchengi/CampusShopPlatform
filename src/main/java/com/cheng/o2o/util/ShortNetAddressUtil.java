package com.cheng.o2o.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 长链接转短链接
 *
 * @author cheng
 *         2018/4/20 22:48
 */
public class ShortNetAddressUtil {

    public static int TIMEOUT = 30 * 1000;
    public static String ENCODING = "UTF-8";

    private static Logger logger = LoggerFactory.getLogger(ShortNetAddressUtil.class);


    /**
     * 根据传入的 url，通过访问百度短视频的接口，将其转为短的 url
     *
     * @param originURL
     * @return
     */
    public static String getShortURL(String originURL) {

        String tinyUrl = null;
        try {
            // 指定百度短视频的接口
            URL url = new URL("http://dwz.cn/create.php");
            // 建立连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置连接的参数
            // 使用连接进行输出
            connection.setDoOutput(true);
            // 使用连接进行输入
            connection.setDoInput(true);
            // 不使用缓存
            connection.setUseCaches(false);
            // 设置连接超时时间为 30s
            connection.setConnectTimeout(TIMEOUT);
            // 设置POST信息，这里为传入的原始 url
            String postData = URLEncoder.encode(originURL, "utf-8");
            // 输出原始的 url
            connection.getOutputStream().write(("url=" + postData).getBytes());
            // 连接百度短视频接口
            connection.connect();
            // 获取返回的字符串
            String responseStr = getResponseStr(connection);
            logger.info("response string : " + responseStr);
            // 在字符串里获取 tinyUrl，即短链接
            tinyUrl = getValueByKey(responseStr, "tinyurl");
            logger.info("tiny url : " + tinyUrl);
            // 关闭连接
            connection.disconnect();
        } catch (IOException e) {
            logger.error("GetShortURL error : " + e.toString());
            e.printStackTrace();
        }

        return tinyUrl;
    }

    /**
     * 通过 HttpConnection 获取返回的字符串
     *
     * @param connection
     * @return
     */
    private static String getResponseStr(HttpURLConnection connection) throws IOException {

        StringBuilder result = new StringBuilder();
        // 从连接种获取http状态码
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // 如果返回的状态码是 200，那么取出连接的输入流
            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, ENCODING));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                // 将消息逐行读入结果种
                result.append(inputLine);
            }
        }

        // 将结果转为 String 返回
        return result.toString();
    }

    /**
     * JSON 依据传入的 key 获取 value
     *
     * @param replyText
     * @param key
     * @return
     */
    private static String getValueByKey(String replyText, String key) {

        ObjectMapper mapper = new ObjectMapper();
        // 定义 json 节点
        JsonNode node;
        String targetValue = null;
        try {
            // 把调用返回的消息船转换成json对象
            node = mapper.readTree(replyText);
            // 依据 key 从json对象里获取对应的值
            targetValue = node.get(key).asText();
        } catch (IOException e) {
            logger.error("getValueByKey error : " + e.toString());
            e.printStackTrace();
        }

        return targetValue;
    }

    public static void main(String[] args) {
        String shortURL = getShortURL("https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login");
        System.out.println(shortURL);
    }
}
