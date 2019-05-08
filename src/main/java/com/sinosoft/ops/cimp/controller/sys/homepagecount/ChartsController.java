package com.sinosoft.ops.cimp.controller.sys.homepagecount;

import com.sinosoft.ops.cimp.annotation.SystemLimitsApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.homepagecount.ChartsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SystemLimitsApiGroup
@Api(description = "图表统计数据")
@RestController
@RequestMapping(value = "/charts")
public class ChartsController extends BaseController {

    private final ChartsService chartsService;

    public ChartsController(ChartsService chartsService) {
        this.chartsService = chartsService;
    }

    /**
     * 获取干部统计图表数据
     */
    @ApiOperation(value = "获取干部统计图表数据")
    @PostMapping("/cadreStatistics")
    public ResponseEntity getCadreStatisticsData() throws BusinessException {
        return ok(chartsService.getCadreStatisticsData());
    }


}
