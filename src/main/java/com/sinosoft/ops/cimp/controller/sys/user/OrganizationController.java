package com.sinosoft.ops.cimp.controller.sys.user;

import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.oraganization.Organization;
import com.sinosoft.ops.cimp.entity.sys.user.ProjectPosition;
import com.sinosoft.ops.cimp.entity.sys.user.organization.OrganizationPosition;
import com.sinosoft.ops.cimp.entity.sys.user.organization.OrganizationPositionService;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.mapper.user.OrganizationPositionViewMapper;
import com.sinosoft.ops.cimp.service.user.*;
import com.sinosoft.ops.cimp.swaggwegroup.RequiresAuthentication;
import com.sinosoft.ops.cimp.util.CachePackage.OrganizationCacheManager;
import com.sinosoft.ops.cimp.vo.from.user.organization.*;
import com.sinosoft.ops.cimp.vo.to.user.BusinessUnitListViewModel;
import com.sinosoft.ops.cimp.vo.to.user.OrganizationPositionViewModel;
import com.sinosoft.ops.cimp.vo.user.organization.OrganizationSearchViewModel;
import com.sinosoft.ops.cimp.vo.user.organization.OrganizationViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(description = "单位接口")
@RestController
@RequestMapping(value = "/organization")
public class OrganizationController extends BaseController {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationPositionService organizationPositionService;
    @Autowired
    private ProjectPositionService projectPositionService;
    @Autowired
    private BusinessUnitService businessUnitService;
    @Autowired
    private BusinessUnitOrgService businessUnitOrgService;


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

    @ApiOperation(value = "根据单位ID查询单位内职务")
    @ApiImplicitParam(name = "organizationId", paramType = "query", value = "单位ID", required = true)
    @PostMapping("/getPositionsByOrganizationId")
    @RequiresAuthentication
    public ResponseEntity<List<OrganizationPositionViewModel>> findPositionsByOrganizationId(@RequestParam String organizationId) throws BusinessException {
        List<OrganizationPosition> orgPositionLst
                = organizationPositionService.findByOrganizationId(organizationId);
        List<OrganizationPositionViewModel> positionViewModelLst = orgPositionLst.stream().map(x -> OrganizationPositionViewMapper.INSTANCE.organizationPositionToViewModel(x)).collect(Collectors.toList());
        return ok(positionViewModelLst);
    }

    /**
     * 根据职务id查询对应的职级信息
     *
     * @return
     */
    @ApiOperation(value = "根据职务id查询对应的职级信息")
    @PostMapping("/findRankInfoByPositionId")
    @RequiresAuthentication
    public ResponseEntity<OrganizationPositionViewModel> findRankInfoByProjectPositionId(String projectPositionId) throws BusinessException {
        OrganizationPositionViewModel organPositionViewModel = null;
        ProjectPosition projectPosition = projectPositionService.findById(projectPositionId);
        if (projectPosition != null) {
            OrganizationPosition organizationPosition = organizationPositionService.findById(projectPosition.getOrganizationPositionId());
            organPositionViewModel = OrganizationPositionViewMapper.INSTANCE.organizationPositionToViewModel(organizationPosition);
            organPositionViewModel.setProjectPositionId(projectPositionId);
        }
        return ok(organPositionViewModel);
    }

    @ApiOperation(value = "根据groupId查询关联职务职级列表")
    @PostMapping("/findRankInfosByGroupId")
    @RequiresAuthentication
    public ResponseEntity<List<OrganizationPositionViewModel>> findRankInfosByGroupId(String groupId) throws BusinessException {
        List<ProjectPosition> projectPositionList = projectPositionService.findByGroupId(groupId);
        List<OrganizationPositionViewModel> organPositionViewModelList = new ArrayList<>();
        OrganizationPositionViewModel organPositionViewModel;
        for (ProjectPosition projectPosition : projectPositionList) {
            OrganizationPosition organizationPosition = organizationPositionService.findById(projectPosition.getOrganizationPositionId());
            organPositionViewModel = OrganizationPositionViewMapper.INSTANCE.organizationPositionToViewModel(organizationPosition);
            organPositionViewModel.setProjectPositionId(projectPosition.getId());
            organPositionViewModelList.add(organPositionViewModel);
        }
        return ok(organPositionViewModelList);
    }

    @ApiOperation(value = "查询业务部门列表")
    @PostMapping("/findBusinessUnitList")
    @RequiresAuthentication
    public ResponseEntity<PaginationViewModel<BusinessUnitListViewModel>> findBusinessUnitList(@RequestBody OrganizationSearchViewModel searchViewModel) throws BusinessException {
        PaginationViewModel<BusinessUnitListViewModel> businessUnitList = businessUnitService.findBusinessUnitList(searchViewModel);
        return ok(businessUnitList);
    }

    @ApiOperation(value = "新增业务部门")
    @PostMapping("/addBusinessUnit")
    @RequiresAuthentication
    public ResponseEntity<String> addBusinessUnit(@RequestBody BusinessUnitAddViewModel addViewModel) throws BusinessException {
        Boolean flag = businessUnitService.addBusinessUnit(addViewModel);
        if (flag) return ok("操作成功！");
        return fail("操作异常！");
    }

    @ApiOperation(value = "删除业务部门")
    @PostMapping("/deleteBusinessUnit")
    @RequiresAuthentication
    public ResponseEntity<String> deleteBusinessUnit(@RequestBody BusinessUnitDeleteViewModel deleteViewModel) throws BusinessException {
        List<String> ids = deleteViewModel.getIds();
        if (ids == null && ids.size() == 0) {
            return fail("id不能为空");
        }
        boolean have = false;
        for (String id : ids) {
            Long count = businessUnitOrgService.countByBusinessUnitId(id);
            if (count > 0) have = true;
        }
        if (have) return fail("已有关联单位 请先删除关联单位！");

        Boolean flag = businessUnitService.deleteBusinessUnit(ids);
        if (flag) return ok("操作成功！");
        return fail("操作异常！");
    }

    @ApiOperation(value = "修改业务部门详情")
    @PostMapping("/changeBusinessUnitOrgList")
    @RequiresAuthentication
    public ResponseEntity<String> changeBusinessUnitOrgList(@RequestBody BusinessUnitOrgChangeViewModel modifyViewModel) throws BusinessException {
        Boolean flag = businessUnitService.changeBusinessUnitOrgList(modifyViewModel);
        if (flag) return ok("操作成功！");
        return fail("操作异常！");
    }

    @ApiOperation(value = "查询机构树相关部门配置页面")
    @PostMapping("/lstTreeNodeOfSuperBusiness")
    @RequiresAuthentication
    public ResponseEntity<OrganizationViewModel> lstTreeNodeOfSuperBusiness(@RequestBody OrganizationSearchViewModel searchViewModel) throws BusinessException {
        OrganizationViewModel viewModel = organizationService.lstTreeNodeOfSuperBusiness(searchViewModel);
        return ok(viewModel);
    }


//    @ApiOperation(value = "查询上级纪委列表")
//    @PostMapping("/findDisciplineUnitList")
//    @RequiresAuthentication
//    public ResponseEntity<PaginationViewModel<DisciplineUnitViewModel>> findDisciplineUnitList(@RequestBody DisciplineUnitSearchViewModel searchViewModel) throws BusinessException {
//        PaginationViewModel<DisciplineUnitViewModel> disciplineUnitList = disciplineUnitService.findDisciplineUnitList(searchViewModel);
//        return ok(disciplineUnitList);
//    }

//    @ApiOperation(value = "新增纪委部门")
//    @PostMapping("/addDisciplineUnit")
//    @RequiresAuthentication
//    public ResponseEntity<String> addDisciplineUnit(@RequestBody DisciplineUnitAddViewModel addViewModel) throws BusinessException {
//        boolean flag = disciplineUnitService.addDisciplineUnit(addViewModel);
//        if (flag) return ok("新增纪委部门成功！");
//        return fail("新增上级纪委异常！");
//    }

//    @ApiOperation(value = "修改纪委部门")
//    @PostMapping("/modifyDisciplineUnit")
//    @RequiresAuthentication
//    public ResponseEntity<String> modifyDisciplineUnit(@RequestBody DisciplineUnitModifyViewModel modifyViewModel) throws BusinessException {
//        boolean flag = disciplineUnitService.modifyDisciplineUnit(modifyViewModel);
//        if (flag) return ok("修改纪委部门成功!");
//        return fail("修改纪委部门异常！");
//    }
//
//    @ApiOperation(value = "根据id查询纪委信息")
//    @PostMapping("/findById")
//    @RequiresAuthentication
//    public ResponseEntity<DisciplineUnitViewModel> findById(String disciplineUnitId) throws BusinessException {
//        DisciplineUnitViewModel viewModel = disciplineUnitService.findDetailById(disciplineUnitId);
//        return ok(viewModel);
//    }



//    @ApiOperation(value = "上传文件")
//    @PostMapping("/uploadAttachment")
//    @RequiresAuthentication
//    public ResponseEntity<List<AttachmentVO>> uploadAttachment(HttpServletRequest request,
//                                                       @RequestParam("pathName") String pathName,
//                                                       @RequestParam(value = "businessUnitId", required = false) String businessUnitId,
//                                                       @RequestParam("type") String type) throws BusinessException {
//        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("files");
//        if (files.size() == 0) return fail("文件不能为空！");
//        if (StringUtils.isBlank(pathName)) return fail("文件存储路径标识不能为空！");
//        //获取文件存储标识
//        pathName = attachmentService.getFilePath(pathName);
//        List<AttachmentVO> attachmentVOList = businessUnitService.uploadAttachment(files, pathName, businessUnitId, type);
//        return JsonSuccess(attachmentVOList);
//    }

//    /**
//     * 删除文件
//     *
//     * @return
//     */
//    @ApiOperation(value = "删除文件")
//    @PostMapping("/deleteFile")
//    @RequiresAuthentication
//    public Result<String> deleteFile(@RequestBody DeleteAttachmentViewModel deleteViewModel) {
//        boolean flag = businessUnitService.deleteFile(deleteViewModel);
//        if (flag) return JsonSuccess("操作成功!");
//        return JsonError("操作异常！");
//    }
//
//    @ApiOperation(value = "根据名字查询组织")
//    @PostMapping("/findOrganizationByName")
//    @RequiresAuthentication
//    public Result<List<OrganizationViewModel>> findOrganizationByName(@RequestParam("name") String name
//            , @RequestParam(required = false) String permission) {
//        if (StringUtils.isEmpty(name)) return JsonError("名字不能为空");
//        //permission: 1 带权限;0 不带权限
//        //默认带权限
//        if (StringUtils.isEmpty(permission)) {
//            permission = "1";
//        }
//        List<OrganizationViewModel> organizationByName = organizationService.findOrganizationByName(name, permission);
//        return JsonSuccess(organizationByName);
//    }

}
