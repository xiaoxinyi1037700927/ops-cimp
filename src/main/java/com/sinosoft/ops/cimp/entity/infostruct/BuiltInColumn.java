/**
 * @project:     IIMP
 * @title:          BuiltInColumn.java
 * @copyright: ©2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.entity.infostruct;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName:  BuiltInColumn
 * @description: 系统内置字段
 * @author:        Nil
 * @date:            2017年11月28日 下午1:44:09
 * @version        1.0.0
 * @since            JDK 1.7
 */
public enum BuiltInColumn {
    CATEGORY_ID("CATEGORY_ID","CATEGORY_ID"),
    CLASS_CODE("CLASS_CODE","CLASS_CODE"),
    CODE("CODE","CODE"),
    CODE_TYPE("CODE_TYPE","CODE_TYPE"),
    CPTR("CPTR","CPTR"),
    CREATED_BY("CREATED_BY","创建人"),
    CREATED_STAMP("CREATED_STAMP","创建时间"),
    CREATED_TIME("CREATED_TIME","CREATED_TIME"),
    CREATED_TX_STAMP("CREATED_TX_STAMP","CREATED_TX_STAMP"),
    DEP_ID("DEP_ID","DEP_ID"),
    DESCRIPTION("DESCRIPTION","DESCRIPTION"),
    EMP_ID("EMP_ID","EMP_ID"),
    ICON("ICON","ICON"),
    LAST_MODIFIED_BY("LAST_MODIFIED_BY","最后修改人"),
    LAST_MODIFIED_TIME("LAST_MODIFIED_TIME","最后修改时间"),
    LAST_UPDATED_STAMP("LAST_UPDATED_STAMP","LAST_UPDATED_STAMP"),
    LAST_UPDATED_TX_STAMP("LAST_UPDATED_TX_STAMP","LAST_UPDATED_TX_STAMP"),
    ORDINAL("ORDINAL","次序"),
    PARTY_ID("PARTY_ID","PARTY_ID"),
    PPTR("PPTR","PPTR"),
    SEQID("SEQID","SEQID"),
    SPELL("SPELL","SPELL"),
    STATUS("STATUS","状态"),
    STATUS2("STATUS2","STATUS2"),
    SUB_ID("SUB_ID","SUB_ID"),
    TREE_LEVEL_CODE("TREE_LEVEL_CODE","TREE_LEVEL_CODE");
    
    private final String value;
    private final String name;
    
    BuiltInColumn(final String value,final String name){
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }
    
    public String getName() {
        return this.name;
    }
    
    /** 
     * 转为映射表
     * @return 映射表
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for(BuiltInColumn e: BuiltInColumn.values()){
            map.put(e.getValue(), e.getName());
        }
        return map;
    }
}
