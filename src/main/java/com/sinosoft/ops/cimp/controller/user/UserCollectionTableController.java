package com.sinosoft.ops.cimp.controller.user;

import com.sinosoft.ops.cimp.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.annotation.SystemUserApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.user.UserCollectionTableService;
import com.sinosoft.ops.cimp.vo.from.user.userCollectionTable.UCTableAddModel;
import com.sinosoft.ops.cimp.vo.from.user.userCollectionTable.UCTableDeleteModel;
import com.sinosoft.ops.cimp.vo.from.user.userCollectionTable.UCTableSearchModel;
import com.sinosoft.ops.cimp.vo.to.user.userCollectionTable.UCTableModifyModel;
import com.sinosoft.ops.cimp.vo.to.user.userCollectionTable.UCTableViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SystemUserApiGroup
@Api(description = "用户收藏表 管理接口")
@RestController
@RequestMapping("/user/userCollectionTableController")
public class UserCollectionTableController extends BaseController {
    @Autowired
    private UserCollectionTableService userCollectionTableService;

    @ApiOperation(value = "查询用户收藏table分页列表")
    @PostMapping("/findUCTablePageList")
    @RequiresAuthentication
    public ResponseEntity<PaginationViewModel<UCTableViewModel>> findUCTablePageList(@RequestBody UCTableSearchModel searchModel) throws BusinessException {
        PaginationViewModel<UCTableViewModel> ucTablePageList = userCollectionTableService.findUCTablePageList(searchModel);
        return ok(ucTablePageList);
    }

    @ApiOperation(value = "根据id查询用户收藏table")
    @PostMapping("/findUCTableById")
    @RequiresAuthentication
    public ResponseEntity<UCTableModifyModel> findUCTableById(@RequestParam String id) throws BusinessException {
        UCTableModifyModel ucTableById = userCollectionTableService.findUCTableById(id);
        return ok(ucTableById);
    }

    @ApiOperation(value = "新增用户收藏table")
    @PostMapping("/saveUCTable")
    @RequiresAuthentication
    public ResponseEntity<Boolean> saveUCTable(@RequestBody UCTableAddModel addModel) throws BusinessException {
        Boolean result = userCollectionTableService.saveUCTable(addModel);
        if (result) {
            return ok(result);
        } else {
            return fail("新增失败！");
        }
    }

    @ApiOperation(value = "修改用户收藏table")
    @PostMapping("/modifyUCTable")
    @RequiresAuthentication
    public ResponseEntity<Boolean> modifyUCTable(@RequestBody UCTableViewModel modifyModel) throws BusinessException {
        Boolean result = userCollectionTableService.modifyUCTable(modifyModel);
        if (result) {
            return ok(result);
        } else {
            return fail("修改失败！");
        }
    }

    @ApiOperation(value = "删除用户收藏table")
    @PostMapping("/deleteUCTable")
    @RequiresAuthentication
    public ResponseEntity<Boolean> deleteUCTable(@RequestBody UCTableDeleteModel deleteModel) throws BusinessException {
        if (deleteModel.getIds() == null || deleteModel.getIds().size() == 0) {
            return fail("id不能为空！");
        }
        Boolean result = userCollectionTableService.deleteUCTable(deleteModel.getIds());
        if (result) {
            return ok(result);
        } else {
            return fail("删除失败！");
        }
    }

    @ApiOperation(value = "修改用户收藏table排序")
    @PostMapping("/changeUCTableSort")
    @RequiresAuthentication
    public ResponseEntity<Boolean> changeUCTableSort(@RequestBody UCTableDeleteModel deleteModel) throws BusinessException {
        if (deleteModel.getIds() == null || deleteModel.getIds().size() != 2) {
            return fail("参数有误！");
        }
        Boolean result = userCollectionTableService.changeUCTableSort(deleteModel.getIds());
        if (result) {
            return ok(result);
        } else {
            return fail("换位失败！");
        }
    }


}
