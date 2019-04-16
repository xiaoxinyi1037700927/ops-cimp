package com.sinosoft.ops.cimp.service.table;


import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.table.SysTable;
import com.sinosoft.ops.cimp.vo.from.table.SysTableAddModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableSearchModel;
import com.sinosoft.ops.cimp.vo.to.table.SysTableModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableModifyModel;

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
