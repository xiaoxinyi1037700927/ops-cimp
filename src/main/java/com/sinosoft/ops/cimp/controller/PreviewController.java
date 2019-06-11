package com.sinosoft.ops.cimp.controller;

import com.sinosoft.ops.cimp.exception.BusinessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/preview")
public class PreviewController extends BaseController {
    @GetMapping(value = {"/rmb"})
    public String index() throws BusinessException {
        return "preview/rmb";
    }
}
