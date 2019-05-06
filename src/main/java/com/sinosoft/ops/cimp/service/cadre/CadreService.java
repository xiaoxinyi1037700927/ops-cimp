package com.sinosoft.ops.cimp.service.cadre;

import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.vo.to.cadre.CadreBasicInfoVO;
import com.sinosoft.ops.cimp.vo.to.cadre.CadreSearchVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CadreService {

    CadreBasicInfoVO getCadreBasicInfo(String empId);

    byte[] getPhoto(String photoId);

    /**
     * 查询干部，根据干部标签和信息项选择进行查询
     */
    Map<String, Object> searchCadres(CadreSearchVO searchVO) throws BusinessException;


    /**
     * 上传干部照片
     */
    boolean uploadPhoto(String empId, MultipartFile photo);
}
