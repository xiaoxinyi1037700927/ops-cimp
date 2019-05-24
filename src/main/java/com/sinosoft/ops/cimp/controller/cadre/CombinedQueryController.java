package com.sinosoft.ops.cimp.controller.cadre;

import com.sinosoft.ops.cimp.annotation.BusinessApiGroup;
import com.sinosoft.ops.cimp.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.cadre.CombinedQueryService;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
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
    public ResponseEntity getFunctions() throws BusinessException {
        return ok(combinedQueryService.getFunctions());
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
    public ResponseEntity findAll() throws BusinessException {
        return ok(combinedQueryService.findAll());
    }


    @ApiOperation(value = "获取组合查询信息")
    @PostMapping("/getById")
    @RequiresAuthentication
    public ResponseEntity getCombinedQuery(@RequestParam String id) throws BusinessException {
        try {
            return ok(combinedQueryService.getCombinedQuery(id));
        } catch (CombinedQueryParseException e) {
            return fail(e.getMessage());
        }
    }

    @ApiOperation(value = "修改表达式")
    @PostMapping("/expreStr/modify")
    @RequiresAuthentication
    public ResponseEntity modifyExprStr(@RequestBody ExprStrModifyModel modifyModel) throws BusinessException {
        try {
            return ok(combinedQueryService.modifyExprStr(modifyModel));
        } catch (CombinedQueryParseException e) {
            return fail(e.getMessage());
        }
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
}
