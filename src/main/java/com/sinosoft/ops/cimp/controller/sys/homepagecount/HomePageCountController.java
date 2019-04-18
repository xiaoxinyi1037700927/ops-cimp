package com.sinosoft.ops.cimp.controller.sys.homepagecount;


import com.sinosoft.ops.cimp.annotation.SystemLimitsApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.homepagecount.HomePageCountService;
import com.sinosoft.ops.cimp.service.sys.homepagecount.RoleHomePageCountService;
import com.sinosoft.ops.cimp.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.vo.from.sys.homepagecount.RoleHomePageCountModel;
import com.sinosoft.ops.cimp.vo.to.sys.homepagecount.HomePageCountQueryVO;
import com.sinosoft.ops.cimp.vo.to.sys.homepagecount.RoleHomePageCountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页统计配置管理
 */
@SystemLimitsApiGroup
@Api(description = "首页统计配置管理")
@RestController
@RequestMapping("/sys/homePageCount")
public class HomePageCountController extends BaseController {
    @Autowired
    private HomePageCountService homePageCountService;
    @Autowired
    private RoleHomePageCountService roleHomePageCountService;


//    @ApiOperation(value = "查询统计基础项")
//    @PostMapping("/findAllHomePageCountList")
//    @RequiresAuthentication
//    public Result<List<HomePageCountVO>> findAllHomePageCountList() {
//
//        List<HomePageCountVO> allHomePageCountVOList = homePageCountService.findAllHomePageCountVOList();
//
//        return JsonSuccess(allHomePageCountVOList);
//    }


//    @ApiOperation(value = "新增/修改角色统计中间配置项")
//    @PostMapping("/addRoleCount")
//    @RequiresAuthentication
//    public Result<Boolean> addRoleCount(@RequestBody RoleHomePageCountModel model) {
//
//        List<RoleHomePageCount> roleHomePageCountList = roleHomePageCountService.addRoleCount(model);
//
//        return JsonSuccess(true);
//    }
//
//    @ApiOperation(value = "删除角色统计中间配置项")
//    @PostMapping("/deleteRoleCountByRoleCountIdList")
//    @RequiresAuthentication
//    public Result<Boolean> deleteRoleCountByRoleCountIdList(@RequestBody List<String> roleCountIdList) {
//
//        boolean b = roleHomePageCountService.deleteRoleCountByRoleCountIdList(roleCountIdList);
//
//        return JsonSuccess(b);
//    }
//
//    @ApiOperation(value = "查询角色配置项")
//    @PostMapping("/findRoleCountByRoleId")
//    @RequiresAuthentication
//    public Result<List<RoleHomePageCountVO>> findRoleCountByRoleId(@RequestParam String roleId) {
//        if (StringUtils.isEmpty(roleId)) {
//            return JsonError("roleId不能为空！");
//        }
//
//        List<RoleHomePageCountVO> roleHomePageCountVOList = roleHomePageCountService.findRoleCountByRoleId(roleId);
//
//        return JsonSuccess(roleHomePageCountVOList);
//    }
//
//
//    @ApiOperation(value = "查询角色对应的统计项统计信息")
//    @PostMapping("/findRoleCountQueryByRoleIdList")
//    @RequiresAuthentication
//    public Result<List<HomePageCountQueryVO>> findRoleCountQueryByRoleIdList(@RequestBody List<String> roleIdList) {
//        if (roleIdList != null && roleIdList.size() == 0) {
//            return JsonError("roleIdList不能为空！");
//        }
//
//        List<HomePageCountQueryVO> homePageCountQueryVOList = homePageCountService.findRoleHomePageCountQueryByRoleIdList(roleIdList);
//
//        return JsonSuccess(homePageCountQueryVOList);
//    }


    @ApiOperation(value = "新增/修改角色统计信息")
    @PostMapping("/addOrUpdateRoleCount")
    @RequiresAuthentication
    public ResponseEntity<Boolean> addOrUpdateRoleCount(@RequestBody RoleHomePageCountModel model) throws BusinessException {

        roleHomePageCountService.addOrUpdateRoleCount(model);

        return ok(true);
    }

    @ApiOperation(value = "删除角色统计信息")
    @PostMapping("/deleteRoleCountByRoleCountIdList")
    @RequiresAuthentication
    public ResponseEntity<Boolean> deleteRoleCountByRoleCountIdList(@RequestBody List<String> roleCountIdList) throws BusinessException {

        boolean b = roleHomePageCountService.deleteRoleCountByRoleCountIdList(roleCountIdList);

        return ok(b);
    }

    @ApiOperation(value = "查询角色配置项")
    @PostMapping("/findRoleCountByRoleId")
    @RequiresAuthentication
    public ResponseEntity<List<RoleHomePageCountVO>> findRoleCountByRoleId(@RequestParam String roleId) throws BusinessException {
        if (StringUtils.isEmpty(roleId)) {
            return fail("roleId不能为空！");
        }

        List<RoleHomePageCountVO> roleHomePageCountVOList = roleHomePageCountService.findRoleCountByRoleId(roleId);

        return ok(roleHomePageCountVOList);
    }


    @ApiOperation(value = "查询角色对应的统计项统计信息")
    @PostMapping("/findRoleCountQueryByRoleIdList")
    @RequiresAuthentication
    public ResponseEntity<List<HomePageCountQueryVO>> findRoleCountQueryByRoleIdList(@RequestBody List<String> roleIdList) throws BusinessException {
        if (roleIdList != null && roleIdList.size() == 0) {
            return fail("roleIdList不能为空！");
        }
        try {
            List<HomePageCountQueryVO> homePageCountQueryVOList = homePageCountService.findRoleHomePageCountQueryByRoleIdList(roleIdList);
            return ok(homePageCountQueryVOList);
        } catch (Exception e) {
            System.out.println(e);
            return fail("统计信息报错，请检查语句是否正确！或联系管理员！");
        }
    }

}
