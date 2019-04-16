package com.sinosoft.ops.cimp.service.user;

import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.base.AttachmentVO;
import com.sinosoft.ops.cimp.vo.from.user.DeleteAttachmentViewModel;
import com.sinosoft.ops.cimp.vo.from.user.organization.BusinessUnitAddViewModel;
import com.sinosoft.ops.cimp.vo.from.user.organization.BusinessUnitModifyViewModel;
import com.sinosoft.ops.cimp.vo.from.user.organization.BusinessUnitOrgChangeViewModel;
import com.sinosoft.ops.cimp.vo.to.user.BusinessUnitListViewModel;
import com.sinosoft.ops.cimp.vo.user.organization.OrganizationSearchViewModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BusinessUnitService {

    /**
     * 查询业务部门列表
     * @return
     */
    PaginationViewModel<BusinessUnitListViewModel> findBusinessUnitList(OrganizationSearchViewModel searchViewModel);

    /**
     * 修改业务部门
     * @param modifyViewModel
     * @return
     */
    Boolean modifyBusinessUnit(BusinessUnitModifyViewModel modifyViewModel);

    /**
     * 新增业务部门
     * @param addViewModel
     * @return
     */
    Boolean addBusinessUnit(BusinessUnitAddViewModel addViewModel);

    /**
     * 删除业务部门
     * @param ids
     * @return
     */
    Boolean deleteBusinessUnit(List<String> ids);

    /**
     * 移入/移出业务部门
     * @param modifyViewModel
     * @return
     */
    Boolean changeBusinessUnitOrgList(BusinessUnitOrgChangeViewModel modifyViewModel);

    /**
     * 上传文件
     * @param files
     * @param pathName
     * @param businessUnitId
     * @return
     */
  //  List<AttachmentVO> uploadAttachment(List<MultipartFile> files, String pathName, String businessUnitId, String type);

    /**
     * 删除文件
     */
    Boolean deleteFile(DeleteAttachmentViewModel deleteViewModel);
}
