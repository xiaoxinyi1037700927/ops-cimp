package com.sinosoft.ops.cimp.controller.cadre;

import com.sinosoft.ops.cimp.annotation.BusinessApiGroup;
import com.sinosoft.ops.cimp.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.cadre.CombinedQueryService;
import com.sinosoft.ops.cimp.vo.from.cadre.combinedQuery.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@BusinessApiGroup
@Api(description = "干部组合查询接口")
@RestController
@RequestMapping(value = "/cadre/combinedQuery")
public class CombinedQueryController extends BaseController {

    private final CombinedQueryService combinedQueryService;

    public CombinedQueryController(CombinedQueryService combinedQueryService) {
        this.combinedQueryService = combinedQueryService;
    }

    @ApiOperation(value = "获取函数列表")
    @PostMapping("/functions")
    @RequiresAuthentication
    public ResponseEntity getFunctions(@RequestBody FunctionSearchModel searchModel) throws BusinessException {
        return ok(combinedQueryService.getFunctions(searchModel));
    }


    @ApiOperation(value = "获取运算符列表")
    @PostMapping("/operators")
    public ResponseEntity getOperators() throws BusinessException {
        return ok(combinedQueryService.getOperators());
    }

    @ApiOperation(value = "获取信息集")
    @PostMapping("/fields")
    @RequiresAuthentication
    public ResponseEntity getSysTableFields() throws BusinessException {
        return ok(combinedQueryService.getSysTableFields());
    }


    @ApiOperation(value = "获取所有组合查询信息")
    @PostMapping("/all")
    @RequiresAuthentication
    public ResponseEntity findAll(@RequestBody CombinedQuerySearchModel searchModel) throws BusinessException {
        return ok(combinedQueryService.findAll(searchModel));
    }


    @ApiOperation(value = "获取组合查询信息")
    @PostMapping("/getById")
    @RequiresAuthentication
    public ResponseEntity getCombinedQuery(@RequestParam String id) throws BusinessException {
        return ok(combinedQueryService.getCombinedQuery(id));
    }


    @ApiOperation(value = "添加表达式")
    @PostMapping("/expr/append")
    @RequiresAuthentication
    public ResponseEntity appendExpr(@RequestBody ExprAppendModel appendModel) throws BusinessException {
        return ok(combinedQueryService.appendExpr(appendModel));
    }


    @ApiOperation(value = "删除表达式")
    @PostMapping("/expr/delete")
    @RequiresAuthentication
    public ResponseEntity deleteExpr(@RequestBody ExprDeleteModel deleteModel) throws BusinessException {
        return ok(combinedQueryService.deleteExpr(deleteModel));
    }


    @ApiOperation(value = "添加函数")
    @PostMapping("/function/append")
    @RequiresAuthentication
    public ResponseEntity appendFunction(@RequestBody FunctionAppendModel appendModel) throws BusinessException {
        return ok(combinedQueryService.appendFunction(appendModel));
    }


    @ApiOperation(value = "删除函数")
    @PostMapping("/function/delete")
    @RequiresAuthentication
    public ResponseEntity deleteFunction(@RequestBody FunctionDeleteModel deleteModel) throws BusinessException {
        return ok(combinedQueryService.deleteFunction(deleteModel));
    }


    @ApiOperation(value = "修改表达式")
    @PostMapping("/expr/modify")
    @RequiresAuthentication
    public ResponseEntity modifyExpr(@RequestBody ExprModifyModel modifyModel) throws BusinessException {
        return ok(combinedQueryService.modifyExpr(modifyModel));
    }

    @ApiOperation(value = "编译表达式")
    @PostMapping("/exprStr/compile")
    @RequiresAuthentication
    public ResponseEntity compileExprStr(@RequestParam(required = false) String combinedQueryId, @RequestParam String exprStr) throws BusinessException {
        return ok(combinedQueryService.compileExprStr(combinedQueryId, exprStr));
    }

    @ApiOperation(value = "统计表达式数量")
    @PostMapping("/expr/statistics")
    @RequiresAuthentication
    public ResponseEntity statisticsExpr(@RequestParam String combinedQueryId) throws BusinessException {
        return ok(combinedQueryService.statisticsExpr(combinedQueryId));
    }

    @ApiOperation(value = "保存组合查询")
    @PostMapping("/save")
    @RequiresAuthentication
    public ResponseEntity saveCombinedQuery(@RequestBody ExprSaveModel saveModel) throws BusinessException {
        combinedQueryService.saveCombinedQuery(saveModel);
        return ok("保存成功");
    }

    @ApiOperation(value = "删除组合查询")
    @PostMapping("/delete")
    @RequiresAuthentication
    public ResponseEntity deleteCombinedQuery(@RequestParam String combinedQueryId) throws BusinessException {
        combinedQueryService.deleteCombinedQuery(combinedQueryId);
        return ok("删除成功");
    }

    @ApiOperation(value = "统计组合查询模板数量")
    @PostMapping("/combinedQuery/statistics")
    @RequiresAuthentication
    public ResponseEntity statisticsCombinedQuery() throws BusinessException {
        return ok(combinedQueryService.statisticsCombinedQuery());
    }
}
