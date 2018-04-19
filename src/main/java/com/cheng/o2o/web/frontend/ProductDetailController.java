package com.cheng.o2o.web.frontend;

import com.cheng.o2o.entity.Product;
import com.cheng.o2o.service.ProductService;
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
 *         2018/4/19 12:25
 */
@Controller
@RequestMapping("/frontend")
public class ProductDetailController {

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
            modelMap.put("success", true);
            modelMap.put("product", product);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Empty productId");
        }

        return modelMap;
    }
}
