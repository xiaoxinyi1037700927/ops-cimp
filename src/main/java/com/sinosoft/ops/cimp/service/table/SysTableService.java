package com.sinosoft.ops.cimp.service.table;


import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.table.SysTable;
import com.sinosoft.ops.cimp.vo.from.table.SysTableAddModel;
import com.sinosoft.ops.cimp.vo.to.table.SysTableModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableModifyModel;

import java.util.List;

public interface SysTableService {

    boolean addSysTable(SysTableAddModel sysTableAddModel);

    boolean delSysTable(String id);

    boolean upSysTable(SysTableModifyModel sysTableModifyModel);

    List<SysTableModel> findSysTableModels();

    List<SysTableModifyModel> findBySysTableTypeId(String SysTableTypeId);

    PaginationViewModel<SysTable> getSysTableByPage(String sysTableTypeId,String nameCn);

}
