package com.cheng.o2o.web.frontend;

import com.cheng.o2o.entity.PersonInfo;
import com.cheng.o2o.entity.Product;
import com.cheng.o2o.service.ProductService;
import com.cheng.o2o.util.CodeUtil;
import com.cheng.o2o.util.HttpServletRequestUtil;
import com.cheng.o2o.util.ShortNetAddressUtil;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cheng
 *         2018/4/19 12:25
 */
@Controller
@RequestMapping("/frontend")
public class ProductDetailController {

    /**
     * 微信获取用户信息的api前缀
     */
    private static String urlPrefix = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0db09a35eac8319e&redirect_uri=";
    /**
     * 微信获取用户信息的api中间部分
     */
    private static String urlMiddle = "&role_type=1&response_type=code&scope=snsapi_userinfo&state=";
    /**
     * 微信获取用户信息的api后缀
     */
    private static String urlSuffix = "#wechat_redirect";
    /**
     * 微信回传给响应添加用户消费信息 url
     */
    private static String productMapUrl = "http://193.112.56.145/o2o/shopadmin/adduserproductmap";

    @Autowired
    private ProductService productService;

    /**
     * 根据商品id获取商品详情
     *
     * @param request
     * @return
     */
    @GetMapping("/listproductdetailpageinfo")
    @ResponseBody
    private Map<String, Object> listProductDetailPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>(4);
        // 获取前端传过来的 productId
        long productId = HttpServletRequestUtil.getLong(request, "productId");

        // 空值判断
        if (productId != -1) {
            // 根据 productId 获取商品信息，包含商品详情图列表
            Product product = productService.getProductById(productId);

            // 新增购买后增加积分的二维码
            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            if (user == null) {
                modelMap.put("needQRCode", false);
            } else {
                modelMap.put("needQRCode", true);
            }

            modelMap.put("product", product);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Empty productId");
        }

        return modelMap;
    }

    /**
     * 生成商品的消费凭证二维码，供操作员扫描，证明已消费
     *
     * @param request
     * @param response
     */
    @GetMapping("/generateqrcode4product")
    @ResponseBody
    private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response) {

        long productId = HttpServletRequestUtil.getLong(request, "productId");
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        // 空值判断
        if (productId != -1 && user != null && user.getUserId() != null) {
            long timeStamp = System.currentTimeMillis();

            String content = "{aaaproductIdaaa:" + productId + ",aaacreateTimeaaa:" + timeStamp + "}";
            try {
                // 将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标 url
                String longUrl = urlPrefix + productMapUrl + urlMiddle +
                        URLEncoder.encode(content, "UTF-8") + urlSuffix;
                // 将目标url转换成短的url
                String shortUrl = ShortNetAddressUtil.getShortURL(longUrl);
                // 调用二维码生成的工具类方法，传入短的 url，生成二维码
                BitMatrix qRCodeImg = CodeUtil.generateQRCodeStream(shortUrl, response);
                // 将二维码以图片流的形式输出到前端
                MatrixToImageWriter.writeToStream(qRCodeImg, "png", response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
