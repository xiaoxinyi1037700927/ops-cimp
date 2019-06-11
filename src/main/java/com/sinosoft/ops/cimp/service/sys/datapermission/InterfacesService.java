package com.sinosoft.ops.cimp.service.sys.datapermission;


import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfacesAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfacesDeleteModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfacesModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfacesSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.datapermission.InterfacesModel;

import java.util.List;

public interface InterfacesService {
    /**
     * 接口列表
     *
     * @param searchModel
     * @return
     */
    PaginationViewModel<InterfacesModel> listInterfaces(InterfacesSearchModel searchModel);

    /**
     * 添加接口
     *
     * @param addModel
     */
    void addInterfaces(InterfacesAddModel addModel);

    /**
     * 修改接口
     *
     * @param modifyModel
     */
    boolean modifyInterfaces(InterfacesModifyModel modifyModel);

    /**
     * 删除接口
     *
     * @param deleteModel
     */
    void deleteInterfaces(InterfacesDeleteModel deleteModel);

    /**
     * 获取配置类型
     *
     * @return
     */
    List<String> getConfigType();
}
