package com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes;

import com.sinosoft.ops.cimp.dao.SysTableInfoDao;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableFieldInfo;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableInfo;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.FieldNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.OperatorNode;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;
import com.sinosoft.ops.cimp.util.combinedQuery.utils.CodeUtil;
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

    private final SysTableInfoDao sysTableInfoDao;
    private final CodeUtil codeUtil;

    public FieldNodeProcessor(SysTableInfoDao sysTableInfoDao, CodeUtil codeUtil) {
        this.sysTableInfoDao = sysTableInfoDao;
        this.codeUtil = codeUtil;
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

        SysTableInfo table = getTable(tableName);
        if (table == null) {
            throw new CombinedQueryParseException("未知的表名：" + tableName);
        }

        SysTableFieldInfo field = getSysTableField(table, fieldName);
        if (field == null) {
            throw new CombinedQueryParseException("未知的字段名：" + fieldName);
        }

        Type returnType = getReturnType(field);
        if (returnType == null) {
            throw new CombinedQueryParseException("未定义的字段类型：" + tableName + "." + fieldName);
        }

        return new FieldNode(table.getId(), table.getDbTableName(), table.getNameCn(), field.getId(), field.getDbFieldName(), field.getNameCn(), returnType.getCode(), field.getCodeSetName(), codeUtil.getCodeSetIdByName(field.getCodeSetName()));
    }

    /**
     * 获取系统表
     *
     * @param tableName
     * @return
     */
    private SysTableInfo getTable(String tableName) throws CombinedQueryParseException {
        try {
            for (SysTableInfo table : sysTableInfoDao.getTableInfo("cadreInfo").getTables()) {
                if (tableName.equals(table.getNameCn()) || tableName.equalsIgnoreCase(table.getNameEn()) || tableName.equalsIgnoreCase(table.getDbTableName())) {
                    return table;
                }
            }
            return null;
        } catch (BusinessException e) {
            throw new CombinedQueryParseException("获取字段信息失败！");
        }
    }

    /**
     * 获取系统表字段
     *
     * @param table
     * @param fieldName
     * @return
     */
    private SysTableFieldInfo getSysTableField(SysTableInfo table, String fieldName) {
        for (SysTableFieldInfo field : table.getTableFields()) {
            if (fieldName.equals(field.getNameCn()) || fieldName.equalsIgnoreCase(field.getNameEn()) || fieldName.equalsIgnoreCase(field.getDbFieldName())) {
                return field;
            }
        }
        return null;
    }

    /**
     * 根据字段类型获取节点返回类型
     *
     * @param field
     * @return
     */
    public Type getReturnType(SysTableFieldInfo field) {
        String fieldType = field.getDbFieldDataType();
        if (StringUtils.isEmpty(fieldType)) {
            return null;
        }
        fieldType = fieldType.toLowerCase();

        if (StringUtils.isNotEmpty(field.getCodeSetName())) {
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
     * @throws CombinedQueryParseException
     */
    @Override
    public Node pushNode(Deque<Node> stack, Node node) throws CombinedQueryParseException {
        if (stack.size() > 0 && stack.peek() instanceof OperatorNode && !stack.peek().isComplete()) {
            //如果前面是一个不完整的运算符节点，合并
            Node first = stack.pop();
            first.addSubNode(node);
            return first;
        }

        stack.push(node);
        return null;
    }

}
