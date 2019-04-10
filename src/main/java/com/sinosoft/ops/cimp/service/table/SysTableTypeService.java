package com.sinosoft.ops.cimp.service.table;


import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableFieldModifyModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableTypeAddModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableTypeModifyModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableTypeSearchModel;
import com.sinosoft.ops.cimp.vo.to.table.SysTableTypeModel;

import java.util.List;

public interface SysTableTypeService {

    boolean addSysTableType(SysTableTypeAddModel sysTableTypeAddModel);

    boolean upSysTableType(SysTableTypeModifyModel sysTableTypeModifyModel);

    boolean delSysTableType(String id);

    List<SysTableTypeModifyModel> findSysTableTypeByCode(String code);

//    List<S>

    PaginationViewModel<SysTableTypeModifyModel> findSysTableTypeByPageOrName(SysTableTypeSearchModel sysTableTypeSearchModel);

    List<SysTableTypeModel> getAllSysTableType();

}
