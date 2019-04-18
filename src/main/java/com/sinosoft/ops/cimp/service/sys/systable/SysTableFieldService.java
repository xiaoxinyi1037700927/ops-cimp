package com.sinosoft.ops.cimp.service.sys.systable;


import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.systable.SysTableField;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableFieldAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableFieldModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableFieldSearchModel;

import java.util.List;

public interface SysTableFieldService {

    boolean delSysTableField(String id);

    boolean upSysTableField(SysTableFieldModifyModel sysTableFieldModifyModel);

    boolean addSysTableField(SysTableFieldAddModel sysTableFieldAddModel);

    PaginationViewModel<SysTableFieldModifyModel> findBySysTableFieldByPageOrName(SysTableFieldSearchModel sysTableFieldSearchModel);

    SysTableFieldModifyModel findById(String id);

    List<SysTableField> getSysTableFieldBySysTableId(String sysTableId);


}
