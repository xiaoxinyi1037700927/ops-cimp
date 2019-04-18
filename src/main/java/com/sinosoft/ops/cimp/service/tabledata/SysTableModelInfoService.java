package com.sinosoft.ops.cimp.service.tabledata;

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
     * 删除信息集数据（更新删除标识字段）
     */
    QueryDataParamBuilder deleteDataByFlag(QueryDataParamBuilder queryDataParam,String deleteType,String deleteFlagCode) throws BusinessException;

    /**
     * 删除信息集数据
     */
    QueryDataParamBuilder deleteData(QueryDataParamBuilder queryDataParam) throws BusinessException;

    /**
     * 删除信息集数据（恢复已删除）
     */
    QueryDataParamBuilder deleteDataRecover(QueryDataParamBuilder queryDataParam) throws BusinessException;


    /**
     * 删除回收站信息集数据
     */
    QueryDataParamBuilder deleteDataFinal(QueryDataParamBuilder queryDataParam) throws BusinessException;

}
