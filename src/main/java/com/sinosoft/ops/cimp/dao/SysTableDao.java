package com.sinosoft.ops.cimp.dao;

import com.sinosoft.ops.cimp.dao.domain.DaoParam;
import com.sinosoft.ops.cimp.dao.domain.ResultParam;
import com.sinosoft.ops.cimp.exception.BusinessException;

public interface SysTableDao {

    /**
     * 根据表定义插入到对应表数据
     */
    int insertData(DaoParam daoParam) throws BusinessException;

    /**
     * 根据表定义更新对应表数据
     */
    void updateData(DaoParam daoParam) throws BusinessException;

    /**
     * 根据表定义删除对应数据
     */
    void deleteData(DaoParam daoParam) throws BusinessException;

    /**
     * 分页查询数据
     */
    ResultParam queryPageData(DaoParam daoParam) throws BusinessException;
}
