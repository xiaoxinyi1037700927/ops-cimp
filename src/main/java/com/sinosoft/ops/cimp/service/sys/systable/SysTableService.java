package com.sinosoft.ops.cimp.service.sys.systable;


import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableSearchModel;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableModifyModel;

import java.util.List;

public interface SysTableService {

    boolean addSysTable(SysTableAddModel sysTableAddModel);

    boolean delSysTable(String id);

    boolean upSysTable(SysTableModifyModel sysTableModifyModel);

    boolean operatingDbTable(String sysTableId);

//    List<SysTableModel> findSysTableModels();

    List<SysTableModifyModel> findAllSysTable();

    List<SysTableModifyModel> findBySysTableTypeId(String SysTableTypeId);

    PaginationViewModel<SysTableModifyModel> getSysTableByPage(SysTableSearchModel sysTableSearchModel);



}
