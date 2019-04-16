package com.sinosoft.ops.cimp.service.table;


import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.table.SysTableField;
import com.sinosoft.ops.cimp.vo.from.table.SysTableFieldAddModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableFieldModifyModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableFieldSearchModel;

import java.util.List;

public interface SysTableFieldService {

    boolean delSysTableField(String id);

    boolean upSysTableField(SysTableFieldModifyModel sysTableFieldModifyModel);

    boolean addSysTableField(SysTableFieldAddModel sysTableFieldAddModel);

    PaginationViewModel<SysTableFieldModifyModel> findBySysTableFieldByPageOrName(SysTableFieldSearchModel sysTableFieldSearchModel);

    SysTableFieldModifyModel findById(String id);

    List<SysTableField> getSysTableFieldBySysTableId(String sysTableId);


}
