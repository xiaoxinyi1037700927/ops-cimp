package com.sinosoft.ops.cimp.util.combinedQuery.processors.post;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;

public interface PostProcessor {

    /**
     * 处理器是否支持节点
     *
     * @param node
     * @return
     */
    boolean support(Node node);

    /**
     * 获取sql前节点处理
     *
     * @param node
     * @throws CombinedQueryParseException
     */
    void postProcessorBeforeGetSql(Node node) throws CombinedQueryParseException;

}
