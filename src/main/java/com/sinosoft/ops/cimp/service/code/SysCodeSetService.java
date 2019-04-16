package com.sinosoft.ops.cimp.service.code;


import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.code.SysCodeSet;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeSetAddModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeSetModifyModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeSetSearchListModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeSetSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.code.SysCodeSetDisplayModel;
import com.sinosoft.ops.cimp.vo.to.sys.code.SysCodeSetModel;
import com.sinosoft.ops.cimp.vo.to.sys.code.SysCodeSetObtainModel;

import java.util.List;
import java.util.Optional;

public interface SysCodeSetService {

    boolean delSysCodeSetById(Integer id);

    boolean saveSysCodeSet(SysCodeSetAddModel sysCodeSetAddModel);

    boolean upSysCodeSet(SysCodeSetModifyModel sysCodeSetModifyModel);

    List<SysCodeSetModifyModel> findAllSysCodeSets();

    PaginationViewModel<SysCodeSetDisplayModel> getPageSysCodeSet(SysCodeSetSearchModel sysCodeSetSearchModel);

    SysCodeSetModifyModel getSysCodeById(Integer id);

    List<SysCodeSetObtainModel> getSysCodeSetAndSysCodeItem(SysCodeSetSearchListModel sysCodeSetSearchListModel);

    List<SysCodeSetModel> getSysCodeSet();

}
