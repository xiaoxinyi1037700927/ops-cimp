package com.sinosoft.ops.cimp.dao;

import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableModelInfo;
import com.sinosoft.ops.cimp.dto.sys.table.SysTableModelInfoDTO;
import com.sinosoft.ops.cimp.exception.BusinessException;

public interface SysTableInfoDao {

    /**
     * 表结构信息缓存的一级缓存名称
     */
    String SYS_TABLE_MODEL_INFO = "SYS_TABLE_MODEL_INFO_CACHE";

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
