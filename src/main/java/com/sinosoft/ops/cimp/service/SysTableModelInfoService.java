package com.sinosoft.ops.cimp.service;

import com.sinosoft.ops.cimp.dto.QueryDataParamBuilder;
import com.sinosoft.ops.cimp.exception.BusinessException;

public interface SysTableModelInfoService {

    /**
     * 保存信息集数据
     */
    QueryDataParamBuilder saveData(QueryDataParamBuilder queryDataParam) throws BusinessException;

    /**
     * 查询信息集数据
     */
    QueryDataParamBuilder queryData(QueryDataParamBuilder queryDataParam) throws BusinessException;

    /**
     * 修改信息集数据
     */
    QueryDataParamBuilder updateData(QueryDataParamBuilder queryDataParam) throws BusinessException;

    /**
     * 删除信息集数据
     */
    void deleteData(QueryDataParamBuilder queryDataParam) throws BusinessException;

}
