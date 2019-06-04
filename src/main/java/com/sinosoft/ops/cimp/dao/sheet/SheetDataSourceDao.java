package com.sinosoft.ops.cimp.dao.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDataSource;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @version 1.0.0
 * @ClassName: SheetDataSourceDao
 * @description: 表设计数据源访问接口
 * @author: kanglin
 * @date: 2018年6月5日 下午
 * @since JDK 1.7
 */
public interface SheetDataSourceDao extends BaseEntityDao<SheetDataSource> {
    /**
     * 用id查询表设计数据源
     *
     * @param id
     * @return 返回一个SheetDataSource对象
     * @author kanglin
     * @date: 2018年6月5日 下午
     * @since JDK 1.7
     */
    SheetDataSource getById(UUID id);

    /**
     * 根据设计数据源保存
     *
     * @param SheetDataSource
     * @return UUID
     * @author kanglin
     * @date: 2018年6月5日 下午
     * @since JDK 1.7
     */
    UUID save(SheetDataSource SheetDataSource);

    /**
     * 根据设计数据源修改
     *
     * @param SheetDataSource
     * @author kanglin
     * @date: 2018年6月5日 下午
     * @since JDK 1.7
     */
    void update(SheetDataSource SheetDataSource, UUID id);

    /**
     * 根据设计数据源删除
     *
     * @param id
     * @author kanglin
     * @date: 2018年6月5日 下午
     * @since JDK 1.7
     */
    void delete(UUID id);

    //根据分类id获取list
    Collection<SheetDataSource> getByCategoryId(int categoryid);

    //取得引用情况
    List<Map> getRefSituation(String id);

    List<Map> getRefnum(Integer categoryid);
}
