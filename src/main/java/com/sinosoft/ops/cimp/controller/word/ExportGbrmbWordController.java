package com.sinosoft.ops.cimp.controller.word;

import com.sinosoft.ops.cimp.config.swagger2.BusinessApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@BusinessApiGroup
@Api(description = "导出干部任免表")
@Controller
@RequestMapping("/word/gbrmb")
public class ExportGbrmbWordController extends BaseController {

    @GetMapping("/generate")
    public void generateGbrmb(HttpServletRequest request, HttpServletResponse response) {

    }

    @GetMapping("/export")
    public void exportGbrmb(HttpServletRequest request, HttpServletResponse response) {

    }
}
