package com.sinosoft.ops.cimp.service.code;


import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeItemAddModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeItemModifyModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeItemPageModel;

import java.util.List;
import java.util.Optional;

public interface SysCodeItemService {

    boolean delSysCodeItemById(Integer id);

    boolean saveSysCodeItem(SysCodeItemAddModel sysCodeItemAddModel);

    boolean upSysCodeItem(SysCodeItemModifyModel sysCodeItemModifyModel);

    PaginationViewModel<SysCodeItemModifyModel> getSysCodeItemBySearchModel(SysCodeItemPageModel sysCodeItemPageModel);

    SysCodeItemModifyModel getSysCodeItemById(Integer codeSetId);

}
