package com.sinosoft.ops.cimp.service.cadre;

import com.sinosoft.ops.cimp.vo.to.cadre.CadreBasicInfoVO;

public interface CadreService {

    CadreBasicInfoVO getCadreBasicInfo(String empId);

    byte[] getPhoto(String photoId);
}
