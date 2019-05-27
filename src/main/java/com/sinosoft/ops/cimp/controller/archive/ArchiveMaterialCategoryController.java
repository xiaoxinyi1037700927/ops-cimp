package com.sinosoft.ops.cimp.controller.archive;


import com.sinosoft.ops.cimp.annotation.ArchiveApiGroup;
import com.sinosoft.ops.cimp.common.BaseResultHttpStatus;
import com.sinosoft.ops.cimp.constant.UserRoleConstants;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.user.Role;
import com.sinosoft.ops.cimp.entity.user.User;
import com.sinosoft.ops.cimp.entity.user.UserRole;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.archive.ArchiveMaterialCategoryService;
import com.sinosoft.ops.cimp.service.archive.ArchiveMaterialService;
import com.sinosoft.ops.cimp.service.user.UserRoleService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 档案分类控制器
 */
@ArchiveApiGroup
@Api(description = "档案分类控制器")
@Controller("archiveMaterialCategoryController")
@RequestMapping("/materialCategory")
public class ArchiveMaterialCategoryController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ArchiveMaterialCategoryController.class);
    @Autowired
    private ArchiveMaterialCategoryService archiveMaterialCategoryService;
    @Autowired
    private ArchiveMaterialService archiveMaterialService;

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 档案分类 和 档案材料 树结构
     * ArchiveMaterialCategory 和 ArchiveMaterial树结构
     * @param request->id         档案分类的code
     * @param request->empId      人员ID
     * @param request->categoryId 档案分类ID
     */
    @ApiOperation(value = "档案分类 和 档案材料 树结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "档案分类的code", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "empId", value = "人员ID", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "archiveMaterialCategoryId", value = "档案分类ID", dataType = "String", required = true, paramType = "query")
    })
    @RequestMapping(value = "/getTree", method = RequestMethod.POST)
    public void getMaterialCategoryAndMaterial4Tree(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
        try {
            String code = request.getParameter("id");//id档案分类的code
            String empId = request.getParameter("empId");
            String userid = SecurityUtils.getSubject().getCurrentUser().getId();
            boolean flag = false;

            List<Role> roles = userRoleService.getRolesByUserId(userid);
            if (roles.size() > 0 && roles.stream().filter(temp -> temp.getCode().equals("13")).count() > 0) {
                flag = true;
            }

            List<HashMap<String, Object>> materialCategoryAndMaterialList = null;
            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            if (flag) {
                //	String user_empid=archiveMaterialCategoryService.findEmpidByUserID(userid.toUpperCase());
                String categoryId = request.getParameter("archiveMaterialCategoryId");//档案分类ID
                materialCategoryAndMaterialList = archiveMaterialCategoryService.getMaterialCategoryAndMaterial4Tree(code, empId, categoryId);

                //  List<HashMap<String,String>> materialCategorytexts=archiveMaterialCategoryService.findbyempower(empId,this.getCurrentUser().getId().toString());

            } else {
                fail("权限不足", BaseResultHttpStatus.FAIL);
            }
            if (materialCategoryAndMaterialList.size() > 0) {
                if (StringUtil.isEmptyOrNull(code)) {
                    resultMap.put("empId", empId);
                    resultMap.put("text", "档案");
                    resultMap.put("code", "VirtualRoot");
                    resultMap.put("pptr", "VirtualRoot");
                    resultMap.put("expanded", true);
                    resultMap.put("tip", "档案");
                    resultMap.put("children", materialCategoryAndMaterialList);
                    resultMap.put("root", resultMap);

                    ok(resultMap);

                } else {
                    ok(materialCategoryAndMaterialList);
                }
            }
        } catch (Exception e) {
            logger.error("查询失败！", e);
            fail("查询失败", BaseResultHttpStatus.FAIL);
        }
    }

    @ApiOperation("testrelated")
    @ApiImplicitParam(name = "empId",
            value = "empId",
            dataType = "String",
            required = true,
            paramType = "query")
    @RequestMapping(value = "/testrelated", method = RequestMethod.POST)
    public void testrelated(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
        String userid = "";
        String empId = request.getParameter("empId");

        if (SecurityUtils.getSubject().getCurrentUser().getId().toString().contains("-")) {
            userid = SecurityUtils.getSubject().getCurrentUser().getId().toString().replace("-", "");
        } else {
            userid = SecurityUtils.getSubject().getCurrentUser().getId().toString();
        }
        System.out.println(userid.toUpperCase());
        String user_empid = archiveMaterialCategoryService.findEmpidByUserID(userid.toUpperCase());
        System.out.println(user_empid);
        List<String> idcards = null;
        //获取登录人员家庭成员身份证
        if (user_empid != null) {
            idcards = archiveMaterialCategoryService.getIdCardsByEmpid(user_empid);
        }

        //登录人查看的人员的idcard
        String idcard = "";
        if (empId != null && empId != "") {
            idcard = archiveMaterialCategoryService.getidcardforone(empId);
        }
        System.out.println("useriduseriduserid===" + userid);

        if (user_empid == null) {
            String ms = "系统提示:请在<用户管理>中为此账户关联人员!";
            fail(ms, BaseResultHttpStatus.FAIL);
        } else if (user_empid != null && (empId.toUpperCase().contains("-") ? empId.toUpperCase().replace("-", "") : empId.toUpperCase()).equals(user_empid.toUpperCase())) {
            String ms = "系统提示:不能查询本人档案!";
            fail(ms, BaseResultHttpStatus.FAIL);
        } else if (idcards != null && idcard != null && idcard != "" && idcards.contains(idcard)) {
            String ms = "系统提示:不允许访问本人家庭成员及社会关系档案!";
            fail(ms, BaseResultHttpStatus.FAIL);
        } else {
            ok("查询成功");
        }
        ok("查询成功");
    }

}
