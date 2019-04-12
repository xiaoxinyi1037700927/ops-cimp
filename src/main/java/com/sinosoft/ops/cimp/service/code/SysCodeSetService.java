package com.sinosoft.ops.cimp.service.code;


import com.sinosoft.ops.cimp.vo.from.code.SysCodeSetAddModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeSetModifyModel;

import java.util.List;
import java.util.Optional;

public interface SysCodeSetService {

    boolean delSysCodeSetById(Integer id);

    boolean saveSysCodeSet(SysCodeSetAddModel sysCodeSetAddModel);

    boolean upSysCodeSet(SysCodeSetModifyModel sysCodeSetModifyModel);

    List<SysCodeSetModifyModel> findAllSysCodeSets();

    SysCodeSetModifyModel getSysCodeById(Integer id);
}
