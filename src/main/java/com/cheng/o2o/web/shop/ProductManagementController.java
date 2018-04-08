package com.cheng.o2o.web.shop;

import com.cheng.o2o.dto.ImageHolder;
import com.cheng.o2o.dto.ProductExecution;
import com.cheng.o2o.entity.Product;
import com.cheng.o2o.entity.ProductCategory;
import com.cheng.o2o.entity.ProductImg;
import com.cheng.o2o.entity.Shop;
import com.cheng.o2o.enums.ProductStateEnum;
import com.cheng.o2o.exceptions.ProductOperationException;
import com.cheng.o2o.service.ProductCategoryService;
import com.cheng.o2o.service.ProductService;
import com.cheng.o2o.util.CodeUtil;
import com.cheng.o2o.util.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cheng
 *         2018/4/6 18:39
 */
@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 支持上传商品详情图的最大数量
     */
    private static final int IMAGE_MAX_COUNT = 5;

    @PostMapping("/addproduct")
    @ResponseBody
    private Map<String, Object> addProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>(10);
        // 验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码!");
            return modelMap;
        }

        // 接收前端参数的变量的初始化，包括商品，缩略图，详情图片列表实现类
        ObjectMapper mapper = new ObjectMapper();
        Product product;
        String productStr = HttpServletRequestUtil.getString(request, "productStr");
        MultipartHttpServletRequest multipartRequest;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();

        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        try {
            // 若请求中存在文件流，则取出相关的文件(包括缩略图和详情图)
            if (multipartResolver.isMultipart(request)) {
                thumbnail = handleImage(request, thumbnail, productImgList);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "上传图片不能为空!");
                return modelMap;
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        try {
            // 尝试获取前端传过来的表单 string 流并将其转换为 Product 实体类
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            e.printStackTrace();
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        // 若 product 信息，缩略图以及详情图非空，则开始进行商品添加操作
        if (product != null && thumbnail != null && productImgList.size() > 0) {
            try {
                // 从 session 中获取当前店铺的 id 并赋值给 product，减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);

                // 执行添加操作
                ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
                if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (ProductOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息!");
        }

        return modelMap;
    }

    @PostMapping("/modifyproduct")
    @ResponseBody
    private Map<String, Object> modifyProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>(10);
        // 是商品编辑时候调用还是上下架操作的时候调用
        // 如果为前者则进行验证码判断，后者则跳过验证码判断
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        // 验证码判断
        if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码!");
            return modelMap;
        }

        // 接收前端参数的变量的初始化，包括商品，缩略图，详情图列表实体类
        ObjectMapper mapper = new ObjectMapper();
        Product product;
        String productStr = HttpServletRequestUtil.getString(request, "productStr");
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        // 如果请求中存在文件流，则取出相关的文件(包括缩略图和详情图)
        try {
            if (multipartResolver.isMultipart(request)) {
                thumbnail = handleImage(request, thumbnail, productImgList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        try {
            // 尝试获取前端传过来的表单 string 流并将其转换成 product 实体类
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        // 非空判断
        if (product != null) {
            try {
                // 从 session 中获取当前店铺的 id 并赋值给 product，减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                // 开始进行商品信息变更操作
                ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
                if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息!");
        }

        return modelMap;
    }

    /**
     * 图片处理
     *
     * @param request
     * @param thumbnail
     * @param productImgList
     * @return
     * @throws IOException
     */
    private ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> productImgList)
            throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 取出缩略图并构建 ImageHolder 对象
        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
        if (thumbnailFile != null) {
            thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
        }

        // 取出详情图列表并构建 List<ImageHolder> 列表对象，最多支持5张图片上传
        for (int i = 0; i < IMAGE_MAX_COUNT; i++) {
            CommonsMultipartFile productImgFile = (CommonsMultipartFile)
                    multipartRequest.getFile("productImg" + i);
            if (productImgFile != null) {
                // 如果取出的第i个详情图片文件流不为空，则讲其加入详情图列表
                ImageHolder productImg = new ImageHolder(
                        productImgFile.getOriginalFilename(), productImgFile.getInputStream());
                productImgList.add(productImg);
            } else {
                // 如果取出的第i个详情图片文件流为空，则终止循环
                break;
            }
        }
        return thumbnail;
    }

    /**
     * 通过商品id获取商品信息
     *
     * @param productId
     * @return
     */
    @GetMapping("/getproductbyid")
    @ResponseBody
    private Map<String, Object> getProductById(@RequestParam Long productId) {
        Map<String, Object> modelMap = new HashMap<>(10);
        // 非空判断
        if (productId > -1) {
            // 获取商品信息
            Product product = productService.getProductById(productId);
            // 获取该店铺下的商品类别列表
            List<ProductCategory> productCategoryList = productCategoryService
                    .getProductCategory(product.getShop().getShopId());
            modelMap.put("success", true);
            modelMap.put("product", product);
            modelMap.put("productCategoryList", productCategoryList);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Empty productId!");
        }

        return modelMap;
    }
}
