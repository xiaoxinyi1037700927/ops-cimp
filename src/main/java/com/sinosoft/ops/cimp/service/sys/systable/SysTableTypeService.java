package com.sinosoft.ops.cimp.service.sys.systable;


import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableTypeAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableTypeModifyModel;

import java.util.List;

public interface SysTableTypeService {

    boolean addSysTableType(SysTableTypeAddModel sysTableTypeAddModel);

    boolean upSysTableType(SysTableTypeModifyModel sysTableTypeModifyModel);

    boolean delSysTableType(String id);

    List<com.sinosoft.ops.cimp.vo.to.sys.systable.SysTableTypeModel> getAllSysTableType();

}
