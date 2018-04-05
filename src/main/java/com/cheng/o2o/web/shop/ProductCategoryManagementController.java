package com.cheng.o2o.web.shop;

import com.cheng.o2o.dto.ProductCategoryExecution;
import com.cheng.o2o.dto.Result;
import com.cheng.o2o.entity.ProductCategory;
import com.cheng.o2o.entity.Shop;
import com.cheng.o2o.enums.ProductCategoryStateEnum;
import com.cheng.o2o.exceptions.ProductCategoryOperationException;
import com.cheng.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cheng
 *         2018/4/4 14:33
 */
@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @PostMapping("/addproductcategories")
    @ResponseBody
    private Map<String, Object> addProductCategories(@RequestBody List<ProductCategory> productCategoryList,
                                                     HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>(10);
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        for (ProductCategory pc : productCategoryList) {
            pc.setShopId(currentShop.getShopId());
        }

        if (productCategoryList != null && productCategoryList.size() > 0) {
            try {
                ProductCategoryExecution pe = productCategoryService.batchAddProductCategory(productCategoryList);
                if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (ProductCategoryOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请至少输入一个店铺商品类别!");
        }

        return modelMap;
    }

    @PostMapping("/removeproductcategory")
    @ResponseBody
    private Map<String, Object> removeProductCategory(Long productCategoryId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>(10);
        if (productCategoryId != null && productCategoryId > 0) {
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                ProductCategoryExecution pe = productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());
                if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (ProductCategoryOperationException pe) {
                modelMap.put("success", false);
                modelMap.put("errMsg", pe.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请至少删除一个商品类别!");
        }

        return modelMap;
    }

    @GetMapping("/getproductcategorylist")
    @ResponseBody
    private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {

        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if (currentShop != null && currentShop.getShopId() > 0) {
            List<ProductCategory> list = productCategoryService.getProductCategory(currentShop.getShopId());
            return new Result<>(true, list);
        } else {
            ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
            return new Result<>(false, ps.getState(), ps.getStateInfo());
        }
    }
}
