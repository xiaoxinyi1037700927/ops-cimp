package com.sinosoft.ops.cimp.service.cadre;

import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.vo.from.cadre.CadreOrgModifyModel;
import com.sinosoft.ops.cimp.vo.from.cadre.CadreSearchModel;
import com.sinosoft.ops.cimp.vo.from.cadre.CadreSortInDepModifyModel;
import com.sinosoft.ops.cimp.vo.from.cadre.CadreStatusModifyModel;
import com.sinosoft.ops.cimp.vo.to.cadre.CadreBasicInfoVO;
import com.sinosoft.ops.cimp.vo.to.cadre.CadreDataVO;
import com.sinosoft.ops.cimp.vo.to.cadre.CadreSearchVO;
import com.sinosoft.ops.cimp.vo.to.cadre.CadreSortInDepModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CadreService {

    /**
     * 干部列表查询
     *
     * @param searchModel
     * @return
     * @throws BusinessException
     */
    CadreDataVO listCadre(CadreSearchModel searchModel) throws BusinessException;

    CadreBasicInfoVO getCadreBasicInfo(String empId);

    byte[] getPhoto(String empId);

    /**
     * 查询干部，根据干部标签和信息项选择进行查询
     */
    Map<String, Object> searchCadres(CadreSearchVO searchVO) throws BusinessException;


    /**
     * 上传干部照片
     */
    boolean uploadPhoto(String empId, MultipartFile photo);

    /**
     * 获取干部单位内排序
     */
    List<CadreSortInDepModel> getSortInDep(String orgId);

    /**
     * 修改干部在单位内排序
     */
    boolean modifySortInDep(CadreSortInDepModifyModel modifyModels) throws BusinessException;

    /**
     * 指定修改单位排序
     */
    boolean modifySortOrder(List<CadreSortInDepModifyModel> modifyModels);

    /**
     * 修改干部状态
     */
    boolean modifyStatus(CadreStatusModifyModel modifyModel);

    /**
     * 修改干部所属单位
     */
    boolean modifyOrganization(CadreOrgModifyModel modifyModel);

    /**
     * 查询干部身份证号是否重复
     */
    boolean cadreCardIdExist(String cardId);
}
