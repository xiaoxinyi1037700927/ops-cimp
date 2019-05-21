package com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.entity.sys.syscode.QSysCodeSet;
import com.sinosoft.ops.cimp.entity.sys.systable.QSysTable;
import com.sinosoft.ops.cimp.entity.sys.systable.QSysTableField;
import com.sinosoft.ops.cimp.entity.sys.systable.SysTable;
import com.sinosoft.ops.cimp.entity.sys.systable.SysTableField;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.FieldNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字段节点处理器
 */
@Component
public class FieldNodeProcessor extends NodeProcessor {
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
            throw new CombinedQueryParseException("非法表达式：" + expr);
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

        Type returnType = getReturnType(field);
        if (returnType == null) {
            throw new CombinedQueryParseException("未定义的字段类型：" + tableName + "." + fieldName);
        }

        return new FieldNode(table.getDbTableName(), table.getNameCn(), field.getDbFieldName(), field.getNameCn(), returnType.getCode(), field.getSysCodeSetName(), getCodeSetId(field.getSysCodeSetName()));
    }

    private Integer getCodeSetId(String sysCodeSetName) {
        if (sysCodeSetName == null) {
            return null;
        }

        QSysCodeSet qSysCodeSet = QSysCodeSet.sysCodeSet;
        return jpaQueryFactory.select(qSysCodeSet.id).from(qSysCodeSet)
                .where(qSysCodeSet.name.eq(sysCodeSetName)).fetchOne();
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
     * 根据字段类型获取节点返回类型
     *
     * @param field
     * @return
     */
    private Type getReturnType(SysTableField field) {
        String fieldType = field.getDbFieldDataType();
        if (StringUtils.isEmpty(fieldType)) {
            return null;
        }
        fieldType = fieldType.toLowerCase();

        if (StringUtils.isNotEmpty(field.getSysCodeSetName())) {
            return Type.CODE;
        } else if (fieldType.contains("char")) {
            return Type.STRING;
        } else if (fieldType.contains("number") || fieldType.contains("numeric")) {
            return Type.NUMBER;
        } else if (fieldType.contains("date") || fieldType.contains("timestamp")) {
            return Type.DATE;
        } else if (fieldType.contains("blob") || fieldType.contains("clob")) {
            return Type.LOB;
        }
        return null;
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
