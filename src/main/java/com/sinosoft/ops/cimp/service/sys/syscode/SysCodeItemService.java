package com.sinosoft.ops.cimp.service.sys.syscode;


import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeItemAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeItemModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeItemPageModel;

public interface SysCodeItemService {

    boolean delSysCodeItemById(Integer id);

    boolean saveSysCodeItem(SysCodeItemAddModel sysCodeItemAddModel);

    boolean upSysCodeItem(SysCodeItemModifyModel sysCodeItemModifyModel);

    PaginationViewModel<SysCodeItemModifyModel> getSysCodeItemBySearchModel(SysCodeItemPageModel sysCodeItemPageModel);

    SysCodeItemModifyModel getSysCodeItemById(Integer codeSetId);

}