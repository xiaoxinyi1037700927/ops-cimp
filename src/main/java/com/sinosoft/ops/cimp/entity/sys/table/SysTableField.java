package com.sinosoft.ops.cimp.entity.sys.table;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统表字段
 */
@Entity
@Table(name = "SYS_TABLE_FIELD")
public class SysTableField implements Serializable {
    private static final long serialVersionUID = -131153731245506393L;
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID", length = 36)
    private String id;
    /**
     * 系统表ID
     */
    @Column(name = "SYS_TABLE_ID", length = 36)
    private String sysTableId;
    /**
     * 中文字段名
     */
    @Column(name = "NAME_CN", length = 120)
    private String nameCn;
    /**
     * 英文字段名
     */
    @Column(name = "NAME_EN", length = 120)
    private String nameEn;
    /**
     * 字段描述
     */
    @Column(name = "DESCRIPTION", length = 300)
    private String description;

    /**
     * 字段所属表名
     */
    @Column(name = "DB_TABLE_NAME", length = 30)
    private String dbTableName;

    /**
     * 数据库中的字段名
     */
    @Column(name = "DB_FIELD_NAME", length = 30)
    private String dbFieldName;

    /**
     * 是否主键
     */
    @Column(name = "IS_PK", length = 10)
    private String isPK;

    /**
     * 是否外键
     */
    @Column(name = "IS_FK", length = 10)
    private String isFK;

    /**
     * 数据库中的字段类型
     */
    @Column(name = "DB_FIELD_DATA_TYPE", length = 30)
    private String dbFieldDataType;

    /**
     * 是否逻辑删除
     */
    @Column(name = "LOGICAL_DELETE_FLAG", length = 10)
    private String logicalDeleteFlag;

    /**
     * 是否级联删除
     */
    @Column(name = "DELETE_CASCADE_FLAG", length = 10)
    private String deleteCascadeFlag;

    /**
     * 属性值修改是否监听
     */
    @Column(name = "ATTR_VALUE_MONITOR", length = 10)
    private String attrValueMonitor;

    /**
     * 是否可以搜索结果
     */
    @Column(name = "CAN_RESULT_FLAG", length = 10)
    private String canResultFlag;
    /**
     * 是否可以作为条件字段
     */
    @Column(name = "CAN_CONDITION_FLAG", length = 10)
    private String canConditionFlag;
    /**
     * 是否可以作为排序字段
     */
    @Column(name = "CAN_ORDER_FLAG", length = 10)
    private String canOrderFlag;

    /**
     * html代码
     */
    @Column(name = "DEFAULT_HTML", length = 4000)
    private String defaultHtml;
    /**
     * 默认脚本
     */
    @Column(name = "DEFAULT_SCRIPT", length = 4000)
    private String defaultScript;

    /**
     * 创建人
     */
    @Column(name = "CREATE_ID", length = 36)
    private String createId;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    /**
     * 修改人
     */
    @Column(name = "MODIFY_ID", length = 36)
    private String modifyId;

    /**
     * 修改时间
     */
    @Column(name = "MODIFY_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;


}
