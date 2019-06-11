package com.sinosoft.ops.cimp.service.sys.datapermission;


import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfaceTypeAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfaceTypeDeleteModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfaceTypeModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.datapermission.InterfaceTypeModel;

import java.util.List;

public interface InterfaceTypeService {

    /**
     * 角色数据权限列表
     *
     * @return
     */
    List<InterfaceTypeModel> listInterfaceType();

    /**
     * 添加角色数据权限
     *
     * @param addModel
     */
    void addInterfaceType(InterfaceTypeAddModel addModel);

    /**
     * 修改角色数据权限
     *
     * @param modifyModel
     */
    boolean modifyInterfaceType(InterfaceTypeModifyModel modifyModel);

    /**
     * 删除角色数据权限
     *
     * @param deleteModel
     */
    void deleteInterfaceType(InterfaceTypeDeleteModel deleteModel);

}
