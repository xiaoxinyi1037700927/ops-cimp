package com.sinosoft.ops.cimp.dao.domain.sys.table;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.entity.sys.table.SysTable;
import com.sinosoft.ops.cimp.entity.sys.table.SysTableField;
import com.sinosoft.ops.cimp.entity.sys.table.SysTableType;
import com.sinosoft.ops.cimp.exception.SystemException;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SysTableModelInfo implements Serializable {

    private static final long serialVersionUID = -4215738647736872121L;

    //存储表类型英文名
    private String tableTypeNameEn;
    //存储表类型中文名
    private String tableTypeNameCn;
    //所有表信息
    private List<SysTableInfo> tables;
    //所有属性（字段信息）
    private List<SysTableFieldInfo> tableFields;
    //<信息集英文名，存储表名称>
    private Map<String, String> tableNameEnAndSaveTableMap;
    //<信息集英文名，信息集下所有属性信息>
    private Map<String, List<SysTableFieldInfo>> tableNameEnAndFieldListMap;
    //<信息集英文名，信息集下所有属性英文名列表>
    private Map<String, List<String>> tableNameEnAndFieldNameListMap;
    //<信息集英文名，0.属性英文名,1.数据库存储字段名称,2.数据库存储类型>
    private Map<String, List<List<String>>> tableNameEnAndFieldMap;
    //状态，1为开，非1为关
    private static final String YES = "1";
    //主键属性字段
    private String primaryKey;
    //主键存储字段
    private String primaryKeySaveField;
    //主集属性
    private List<String> primaryTableFields;
    //子集信息的信息集名称和主键属性名<子集信息集英文名，主键属性名>
    private Map<String, String> subTableNameEnAndPrimaryKeyMap = Maps.newHashMap();
    //子集信息集名和属性名称
    private Map<String, List<String>> subTableFields = Maps.newHashMap();

    public SysTableModelInfo(SysTableType sysTableType, List<SysTable> sysTables, List<SysTableField> sysTableFields) {
        if (sysTableType == null) {
            throw new SystemException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE_100202, tableTypeNameEn);
        }
        if (sysTables == null) {
            throw new SystemException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE_100203, tableTypeNameEn);
        }
        if (sysTableFields == null) {
            throw new SystemException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE_100204, tableTypeNameEn);
        }
        //表类型名称
        this.tableTypeNameEn = sysTableType.getNameEn();
        this.tableTypeNameCn = sysTableType.getNameCn();
        //初始化表信息
        this.tables = initSysTableInfo(sysTables);
        this.tableFields = initSysTableFieldInfo(this.tables, sysTableFields);
        this.tableNameEnAndSaveTableMap = initTableNameEnAndSaveTableMap();
        this.tableNameEnAndFieldListMap = initTableNameEnAndFieldListMap();
        this.tableNameEnAndFieldNameListMap = initTableNameEnAndFieldNameListMap();
        this.tableNameEnAndFieldMap = initTableNameEnAndFieldMap();
        //初始化一对一主键和子集的主键字段
        this.initPrimaryKey();

    }

    private List<SysTableInfo> initSysTableInfo(List<SysTable> sysTables) {
        return sysTables.stream().map(sysTable -> {
            SysTableInfo sysTableInfo = new SysTableInfo();
            sysTableInfo.setId(sysTable.getId());
            sysTableInfo.setDbTableName(sysTable.getDbTableName());
            sysTableInfo.setDescription(sysTable.getDescription());
            sysTableInfo.setMasterTable(StringUtils.equalsIgnoreCase(sysTable.getIsMasterTable(), YES));
            sysTableInfo.setNameEn(sysTable.getNameEn());
            sysTableInfo.setNameCn(sysTable.getNameCn());
            sysTableInfo.setSort(sysTable.getSort() == null ? 0 : sysTable.getSort());
            sysTableInfo.setStatus(sysTable.getStatus());
            return sysTableInfo;
        }).collect(Collectors.toList());
    }

    private List<SysTableFieldInfo> initSysTableFieldInfo(List<SysTableInfo> tables, List<SysTableField> sysTableFields) {

        Map<String, List<SysTableField>> sysTableFieldMap = sysTableFields.stream().collect(Collectors.groupingBy(SysTableField::getSysTableId));
        List<SysTableFieldInfo> resultTableFields = Lists.newArrayList();

        tables.forEach(table -> {
            String tableId = table.getId();
            List<SysTableField> tableFields = sysTableFieldMap.get(tableId);
            List<SysTableFieldInfo> sysTableFieldInfos = tableFields.stream().map(
                    field -> {
                        SysTableFieldInfo sysTableFieldInfo = new SysTableFieldInfo();
                        sysTableFieldInfo.setId(field.getId());
                        sysTableFieldInfo.setAttrValueMonitor(field.getAttrValueMonitor());
                        sysTableFieldInfo.setCanConditionFlag(StringUtils.equalsIgnoreCase(field.getCanConditionFlag(), YES));
                        sysTableFieldInfo.setCanOrderFlag(StringUtils.equalsIgnoreCase(field.getCanOrderFlag(), YES));
                        sysTableFieldInfo.setCanResultFlag(StringUtils.equalsIgnoreCase(field.getCanResultFlag(), YES));
                        sysTableFieldInfo.setDbFieldDataType(field.getDbFieldDataType());
                        sysTableFieldInfo.setDbFieldName(field.getDbFieldName());
                        sysTableFieldInfo.setDbTableName(field.getDbTableName());
                        sysTableFieldInfo.setDefaultHtml(field.getDefaultHtml());
                        sysTableFieldInfo.setDefaultScript(field.getDefaultScript());
                        sysTableFieldInfo.setDeleteCascadeFlag(StringUtils.equalsIgnoreCase(field.getDeleteCascadeFlag(), YES));
                        sysTableFieldInfo.setDescription(field.getDescription());
                        sysTableFieldInfo.setFK(StringUtils.equalsIgnoreCase(field.getIsFK(), YES));
                        sysTableFieldInfo.setLogicalDeleteFlag(StringUtils.equalsIgnoreCase(field.getLogicalDeleteFlag(), YES));
                        sysTableFieldInfo.setNameCn(field.getNameCn());
                        sysTableFieldInfo.setNameEn(field.getNameEn());
                        sysTableFieldInfo.setPK(StringUtils.equalsIgnoreCase(field.getIsPK(), YES));
                        sysTableFieldInfo.setSort(field.getSort() == null ? 0 : field.getSort());
                        sysTableFieldInfo.setCodeSetName(field.getSysCodeSetName());
                        sysTableFieldInfo.setCodeSetType(field.getSysCodeSetType());
                        return sysTableFieldInfo;
                    }
            ).collect(Collectors.toList());
            table.setTableFields(sysTableFieldInfos);
            resultTableFields.addAll(sysTableFieldInfos);

        });
        return resultTableFields;
    }

    private Map<String, String> initTableNameEnAndSaveTableMap() {
        Map<String, String> result = Maps.newHashMap();

        this.tables.forEach(table -> {
            String nameEn = table.getNameEn();
            String dbTableName = table.getDbTableName();
            result.put(nameEn, dbTableName);
        });
        return result;
    }

    private Map<String, List<SysTableFieldInfo>> initTableNameEnAndFieldListMap() {
        Map<String, List<SysTableFieldInfo>> result = Maps.newHashMap();

        this.tables.forEach(table -> {
            String nameEn = table.getNameEn();
            List<SysTableFieldInfo> tableFields = table.getTableFields();
            result.put(nameEn, tableFields);
        });
        return result;
    }

    private Map<String, List<String>> initTableNameEnAndFieldNameListMap() {
        Map<String, List<String>> result = Maps.newHashMap();
        this.tableNameEnAndFieldListMap.forEach((tableNameEn, fields) -> {
            List<String> tableFields = fields.stream().map(SysTableFieldInfo::getNameEn).collect(Collectors.toList());
            result.put(tableNameEn, tableFields);
        });
        return result;
    }

    private Map<String, List<List<String>>> initTableNameEnAndFieldMap() {
        Map<String, List<List<String>>> result = Maps.newHashMap();

        this.tableNameEnAndFieldListMap.forEach((k, v) -> {
            List<List<String>> fieldNames = Lists.newArrayListWithCapacity(v.size());
            //<0.属性英文名,1.数据库存储字段名称,2.数据库存储类型>
            v.forEach(f -> {
                List<String> fieldList = Lists.newArrayListWithCapacity(3);
                String nameEn = f.getNameEn();
                String dbFieldName = f.getDbFieldName();
                String dbFieldDataType = f.getDbFieldDataType();
                fieldList.add(nameEn);
                fieldList.add(dbFieldName);
                fieldList.add(dbFieldDataType);
                fieldNames.add(fieldList);
            });
            result.put(k, fieldNames);
        });
        return result;
    }

    private void initPrimaryKey() {
        for (SysTableInfo table : this.tables) {
            boolean masterTable = table.isMasterTable();
            //如果是主集，主集中只允许有一个字段被标记为主键字段，如果是子集则子集与主集关联的字段为主集的主键字段
            List<SysTableFieldInfo> tableFields = table.getTableFields();
            if (masterTable) {
                //1.判断是否出现重复元素
                Map<String, Long> nameEnMap = tableFields.stream().collect(Collectors.groupingBy(SysTableFieldInfo::getNameEn, Collectors.counting()));
                List<String> errorFieldList = Lists.newArrayList();
                //当属性个数>1则需要报错
                nameEnMap.forEach((k, v) -> {
                    if (v > 1) errorFieldList.add(k);
                });
                if (errorFieldList.size() > 0) {
                    String errorField = nameEnMap.keySet().stream().collect(Collectors.joining("，"));
                    throw new SystemException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE_100205, tableTypeNameEn, errorField);
                }
                Optional<SysTableFieldInfo> primaryKeyField = tableFields.stream().filter(SysTableFieldInfo::isPK).findFirst();
                //主键字段必须存在
                if (primaryKeyField.isPresent()) {
                    SysTableFieldInfo sysTableFieldInfo = primaryKeyField.get();
                    this.primaryKey = sysTableFieldInfo.getNameEn();
                    this.primaryKeySaveField = sysTableFieldInfo.getDbFieldName();
                } else {
                    throw new SystemException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE_100207, tableTypeNameEn);
                }
                primaryTableFields = tableFields.stream().map(SysTableFieldInfo::getNameEn).collect(Collectors.toList());
            } else {
                Optional<SysTableFieldInfo> primaryKeyField = tableFields.stream().filter(SysTableFieldInfo::isPK).findFirst();
                final String tableNameEn = table.getNameEn();
                primaryKeyField.ifPresent(sysTableFieldInfo -> this.subTableNameEnAndPrimaryKeyMap.put(tableNameEn, sysTableFieldInfo.getNameEn()));
                List<String> subFields = tableFields.stream().map(SysTableFieldInfo::getNameEn).collect(Collectors.toList());
                subTableFields.put(tableNameEn, subFields);
            }
        }
    }

    public String getTableTypeNameEn() {
        return tableTypeNameEn;
    }

    public String getTableTypeNameCn() {
        return tableTypeNameCn;
    }

    public List<SysTableInfo> getTables() {
        return tables;
    }

    public List<SysTableFieldInfo> getTableFields() {
        return tableFields;
    }

    public Map<String, String> getTableNameEnAndSaveTableMap() {
        return tableNameEnAndSaveTableMap;
    }

    public Map<String, List<SysTableFieldInfo>> getTableNameEnAndFieldListMap() {
        return tableNameEnAndFieldListMap;
    }

    public Map<String, List<String>> getTableNameEnAndFieldNameListMap() {
        return tableNameEnAndFieldNameListMap;
    }

    public Map<String, List<List<String>>> getTableNameEnAndFieldMap() {
        return tableNameEnAndFieldMap;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public String getPrimaryKeySaveField() {
        return primaryKeySaveField;
    }

    public List<String> getPrimaryTableFields() {
        return primaryTableFields;
    }

    public Map<String, String> getSubTableNameEnAndPrimaryKeyMap() {
        return subTableNameEnAndPrimaryKeyMap;
    }

    public Map<String, List<String>> getSubTableFields() {
        return subTableFields;
    }
}
