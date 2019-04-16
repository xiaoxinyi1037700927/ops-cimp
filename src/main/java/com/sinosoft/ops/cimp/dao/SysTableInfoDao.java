package com.sinosoft.ops.cimp.dao;

import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableModelInfo;
import com.sinosoft.ops.cimp.dto.sys.table.SysTableModelInfoDTO;
import com.sinosoft.ops.cimp.exception.BusinessException;

public interface SysTableInfoDao {
    /**
     * 根据表类型获取表结构
     *
     * @param tableTypeName 表类型英文名
     */
    SysTableModelInfo getTableInfo(String tableTypeName) throws BusinessException;

    /**
     * 根据表类型和项目编号获取表结构
     */
    SysTableModelInfoDTO getTableInfo(String tableTypeName, String prjCode) throws BusinessException;
}
