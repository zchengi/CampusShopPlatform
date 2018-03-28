package com.cheng.o2o.web.superadmin;

import com.cheng.o2o.entity.Area;
import com.cheng.o2o.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cheng
 *         2018/3/28 21:12
 */
@Controller
@RequestMapping("/superadmin")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @ResponseBody
    @GetMapping(value = "/listarea")
    private Map<String, Object> listArea() {

        Map<String, Object> modelMap = new HashMap<>(100);
        try {
            List<Area> list = areaService.getAreaList();
            modelMap.put("rows", list);
            modelMap.put("total", list.size());
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
        }

        return modelMap;
    }
}
