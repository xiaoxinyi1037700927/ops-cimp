package com.sinosoft.ops.cimp.service.cadre;

import com.sinosoft.ops.cimp.vo.to.cadre.CadreBasicInfoVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CadreService {

    CadreBasicInfoVO getCadreBasicInfo(String empId);

    byte[] getPhoto(String photoId);

    /**
     *
     */
    Map<String, Object> searchCadres(List<String> cadreTagIds, HashMap<String, Object> tableConditions);

    /**
     * 上传干部照片
     */
    boolean uploadPhoto(String empId, MultipartFile photo);
}
