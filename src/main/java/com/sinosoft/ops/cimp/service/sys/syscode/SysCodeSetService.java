package com.sinosoft.ops.cimp.service.sys.syscode;


import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeSetAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeSetModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeSetSearchListModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeSetSearchModel;

import java.util.List;

public interface SysCodeSetService {

    boolean delSysCodeSetById(Integer id);

    boolean saveSysCodeSet(SysCodeSetAddModel sysCodeSetAddModel);

    boolean upSysCodeSet(SysCodeSetModifyModel sysCodeSetModifyModel);

    List<SysCodeSetModifyModel> findAllSysCodeSets();

    PaginationViewModel<com.sinosoft.ops.cimp.vo.to.sys.syscode.SysCodeSetDisplayModel> getPageSysCodeSet(SysCodeSetSearchModel sysCodeSetSearchModel);

    SysCodeSetModifyModel getSysCodeById(Integer id);

    List<com.sinosoft.ops.cimp.vo.to.sys.syscode.SysCodeSetObtainModel> getSysCodeSetAndSysCodeItem(SysCodeSetSearchListModel sysCodeSetSearchListModel);

    List<com.sinosoft.ops.cimp.vo.to.sys.syscode.SysCodeSetModel> getSysCodeSet();

}
