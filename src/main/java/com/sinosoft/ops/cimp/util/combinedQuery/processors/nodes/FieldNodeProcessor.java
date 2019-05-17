package com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.entity.sys.systable.QSysTable;
import com.sinosoft.ops.cimp.entity.sys.systable.QSysTableField;
import com.sinosoft.ops.cimp.entity.sys.systable.SysTable;
import com.sinosoft.ops.cimp.entity.sys.systable.SysTableField;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.FieldNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字段节点处理器
 */
@Component
public class FieldNodeProcessor implements NodeProcessor {
    private static final Pattern pattern = Pattern.compile("^([\\u4e00-\\u9fa5_a-zA-Z0-9\\-]+)\\.([\\u4e00-\\u9fa5_a-zA-Z0-9\\-]+)$");

    private final JPAQueryFactory jpaQueryFactory;

    public FieldNodeProcessor(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    /**
     * 表达式是否满足节点
     *
     * @param expr
     * @return
     */
    @Override
    public boolean support(String expr) {
        Matcher matcher = pattern.matcher(expr);
        return matcher.matches();
    }

    /**
     * 节点是否匹配
     *
     * @param node
     * @return
     */
    @Override
    public boolean support(Node node) {
        return node instanceof FieldNode;
    }

    /**
     * 将表达式解析为节点
     *
     * @param expr
     * @return
     * @throws CombinedQueryParseException
     */
    @Override
    public Node parse(String expr) throws CombinedQueryParseException {
        Matcher matcher = pattern.matcher(expr);
        if (!matcher.matches()) {
            throw new CombinedQueryParseException("解析失败：" + expr);
        }

        String tableName = matcher.group(1);
        String fieldName = matcher.group(2);

        SysTable table = getSysTable(tableName);
        if (table == null) {
            throw new CombinedQueryParseException("未知的表名：" + tableName);
        }

        SysTableField field = getSysTableField(table.getId(), fieldName);
        if (field == null) {
            throw new CombinedQueryParseException("未知的字段名：" + fieldName);
        }

        return new FieldNode(expr, table.getDbTableName(), field.getDbFieldName(), field.getDbFieldDataType());
    }

    /**
     * 获取系统表
     *
     * @param tableName
     * @return
     */
    private SysTable getSysTable(String tableName) {
        QSysTable qSysTable = QSysTable.sysTable;
        return jpaQueryFactory.select(qSysTable).from(qSysTable)
                .where(qSysTable.nameCn.eq(tableName)
                        .or(qSysTable.nameEn.equalsIgnoreCase(tableName))
                        .or(qSysTable.dbTableName.equalsIgnoreCase(tableName))).fetchOne();
    }

    /**
     * 获取系统表字段
     *
     * @param sysTableId
     * @param fieldName
     * @return
     */
    private SysTableField getSysTableField(String sysTableId, String fieldName) {
        QSysTableField qSysTableField = QSysTableField.sysTableField;
        return jpaQueryFactory.select(qSysTableField).from(qSysTableField)
                .where(qSysTableField.sysTableId.eq(sysTableId).and(
                        qSysTableField.nameCn.eq(fieldName)
                                .or(qSysTableField.nameEn.equalsIgnoreCase(fieldName))
                                .or(qSysTableField.dbFieldName.equalsIgnoreCase(fieldName))
                )).fetchOne();
    }

    /**
     * 将节点push进堆栈中
     *
     * @param stack
     * @param node
     */
    @Override
    public Node pushNode(Deque<Node> stack, Node node) {
        stack.push(node);
        return null;
    }
}
