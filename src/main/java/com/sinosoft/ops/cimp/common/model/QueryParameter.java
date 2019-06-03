package com.sinosoft.ops.cimp.common.model;

import java.util.Collection;
import java.util.Map;

/**
 * @version 1.0.0
 * @ClassName: QueryParameter
 * @description: 查询参数
 * @author: Nil
 * @date: 2017年10月6日 下午9:34:39
 * @since JDK 1.7
 */
public interface QueryParameter {
    /**
     * 获取查询条件
     *
     * @return 查询条件
     * @author Nil
     * @since JDK 1.7
     */
    AbstractCondition getCondition();

    /**
     * 获取参数表
     *
     * @return 参数表
     */
    Map<String, Object> getParameters();

    /**
     * 获取排序字段
     *
     * @return 排序字段
     */
    Map<String, String> getOrderBys();

    /**
     * 获取分组字段
     *
     * @return 分组字段
     */
    Collection<String> getGroupBys();
}
