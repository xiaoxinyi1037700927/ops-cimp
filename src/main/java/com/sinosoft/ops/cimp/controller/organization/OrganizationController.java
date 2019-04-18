package com.sinosoft.ops.cimp.controller.organization;

import com.sinosoft.ops.cimp.config.annotation.OrganizationApiGroup;
import com.sinosoft.ops.cimp.config.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.oraganization.Organization;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.oraganization.OrganizationService;
import com.sinosoft.ops.cimp.util.CachePackage.OrganizationCacheManager;
import com.sinosoft.ops.cimp.vo.to.organization.OrganizationSearchViewModel;
import com.sinosoft.ops.cimp.vo.to.organization.OrganizationViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@OrganizationApiGroup
@Api(description = "单位接口")
@RestController
@RequestMapping(value = "/organization")
@SuppressWarnings("unchecked")
public class OrganizationController extends BaseController {

    @Autowired
    private OrganizationService organizationService;


    @ApiOperation(value = "查询机构树")
    @PostMapping("/lstTreeNode")
    @RequiresAuthentication
    public ResponseEntity<OrganizationViewModel> lstTreeNode(@RequestBody OrganizationSearchViewModel searchViewModel) throws BusinessException {
        OrganizationViewModel viewModel = organizationService.lstTreeNode(searchViewModel);
        return ok(viewModel);
    }

    @ApiOperation(value = "根据父节点查询下级组织")
    @PostMapping("/findOrganizationByParentId")
    @RequiresAuthentication
    public ResponseEntity<List<OrganizationViewModel>> findOrganizationByParentId(String parentId) throws BusinessException {
        List<OrganizationViewModel> organViewModelList = organizationService.findOrganizationByParentId(parentId);
        return ok(organViewModelList);
    }

    @ApiOperation(value = "根据机构ID查询机构信息")
    @PostMapping("/findOrganizationById")
    @RequiresAuthentication
    public ResponseEntity<Organization> findOrganizationById(@RequestParam String organizationId) throws BusinessException {
        Organization organization = OrganizationCacheManager.getSubject().getOrganizationById(organizationId);
        if (organization != null) {
            return ok(organization);
        }
        return null;
    }


    @ApiOperation(value = "根据名字查询组织")
    @PostMapping("/findOrganizationByName")
    @RequiresAuthentication
    public ResponseEntity<List<OrganizationViewModel>> findOrganizationByName(@RequestParam("name") String name
            , @RequestParam(required = false) String permission) throws BusinessException {
        if (StringUtils.isEmpty(name)) return fail("名字不能为空");
        //permission: 1 带权限;0 不带权限
        //默认带权限
        if (StringUtils.isEmpty(permission)) {
            permission = "1";
        }
        List<OrganizationViewModel> organizationByName = organizationService.findOrganizationByName(name, permission);
        return ok(organizationByName);
    }

}
