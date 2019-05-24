package com.sinosoft.ops.cimp.controller;

import com.sinosoft.ops.cimp.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController extends BaseController {

    @Value("${system.app.name}")
    private String appName;
    @Value("${system.app.icon}")
    private String appIcon;

    @GetMapping(value = {"/", "/index"})
    public String index(ModelMap map) throws BusinessException {
        map.addAttribute("appName", appName);
        map.addAttribute("appIcon", appIcon);
        return "index";
    }
}
