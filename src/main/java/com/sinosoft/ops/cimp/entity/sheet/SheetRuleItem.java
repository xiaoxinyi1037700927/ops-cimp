package com.sinosoft.ops.cimp.entity.sheet;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @classname:  SheetRuleItem
 * @description: 表格规则项
 * 规则项被描述为“左函数（参数1，参数2，参数3） 逻辑运算符 右函数（参数1，参数2，参数3）”
 * 的形式，即：
 *   leftFunctionName(leftParameter1,leftParameter2,leftParameter3)
 *   relationalOperator
 *   rightFunctionName(rightParameter1,rightParameter2,rightParameter3)
 * “函数”最终会被翻译成与数据库类型相关的SQL函数，参考绝大多数函数，目前
 * 最多支持3个参数，从左至右顺序排列。参数的类型，可以是字段（表名.列名）、
 * 变量、参数、普通字符/数值等，由与之相应的参数类型（ParameterType）来记录。
 * 函数可以是“空函数（VOID）”，即不使用函数，此时有且只有一个“参数”。
 * “逻辑运算符”包括等于、不等于、大于...、为空、非空等。
 * @author:        Nil
 * @date:            2017年8月13日 下午2:07:41
 * @version        1.0.0
 */
@Entity
@Table(name = "SHEET_RULE_ITEM")
public class SheetRuleItem implements java.io.Serializable {
    private static final long serialVersionUID = -7960836635866449996L;
    // Fields
    /*** 标识 */
    private UUID id;
    /*** 组标识 */
    private UUID groupId;
    /*** 左函数名 */
    private String leftFunctionName;
    /*** 左参数1 */
    private String leftParameter1;
    /*** 左参数1类型 */
    private Byte leftParameter1Type;
    /*** 左参数2 */
    private String leftParameter2;
    /*** 左参数2类型 */
    private Byte leftParameter2Type;
    /*** 左参数3 */
    private String leftParameter3;
    /*** 左参数3类型 */
    private Byte leftParameter3Type;
    /*** 左是否去重 */
    private Boolean leftUsingDistinct;
    /*** 关系运算符 */
    private String relationalOperator;
    /*** 右函数名 */
    private String rightFunctionName;
    /*** 右参数1 */
    private String rightParameter1;
    /*** 右参数1类型 */
    private Byte rightParameter1Type;
    /*** 右参数2类型 */
    private String rightParameter2;
    /*** 右参数2类型 */
    private Byte rightParameter2Type;
    /*** 右参数3类型 */
    private String rightParameter3;
    /*** 右参数3类型 */
    private Byte rightParameter3Type;
    /*** 右是否去重 */
    private Boolean rightUsingDistinct;
    /*** 描述 */
    private String description;
    /*** 次序 */
    private Integer ordinal;
    /*** 状态 */
    private Byte status;
    /*** 创建时间 */
    private Timestamp createdTime;
    /*** 创建人 */
    private UUID createdBy;
    /*** 最后修改时间 */
    private Timestamp lastModifiedTime;
    /*** 最后修改人 */
    private UUID lastModifiedBy;

    // Constructors

    /** default constructor */
    public SheetRuleItem() {
    }

    /** minimal constructor */
    public SheetRuleItem(UUID id, UUID groupId, Byte status, Timestamp createdTime, Timestamp lastModifiedTime) {
        this.id = id;
        this.groupId = groupId;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetRuleItem(UUID id, UUID groupId, String leftFunctionName, String leftParameter1,
            Byte leftParameter1Type, String leftParameter2, Byte leftParameter2Type, String leftParameter3,
            Byte leftParameter3Type, Boolean leftUsingDistinct, String relationalOperator, String rightFunctionName,
            String rightParameter1, Byte rightParameter1Type, String rightParameter2, Byte rightParameter2Type,
            String rightParameter3, Byte rightParameter3Type, Boolean rightUsingDistinct, String description,
            Integer ordinal, Byte status, Timestamp createdTime, UUID createdBy, Timestamp lastModifiedTime,
            UUID lastModifiedBy) {
        this.id = id;
        this.groupId = groupId;
        this.leftFunctionName = leftFunctionName;
        this.leftParameter1 = leftParameter1;
        this.leftParameter1Type = leftParameter1Type;
        this.leftParameter2 = leftParameter2;
        this.leftParameter2Type = leftParameter2Type;
        this.leftParameter3 = leftParameter3;
        this.leftParameter3Type = leftParameter3Type;
        this.leftUsingDistinct = leftUsingDistinct;
        this.relationalOperator = relationalOperator;
        this.rightFunctionName = rightFunctionName;
        this.rightParameter1 = rightParameter1;
        this.rightParameter1Type = rightParameter1Type;
        this.rightParameter2 = rightParameter2;
        this.rightParameter2Type = rightParameter2Type;
        this.rightParameter3 = rightParameter3;
        this.rightParameter3Type = rightParameter3Type;
        this.rightUsingDistinct = rightUsingDistinct;
        this.description = description;
        this.ordinal = ordinal;
        this.status = status;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastModifiedTime = lastModifiedTime;
        this.lastModifiedBy = lastModifiedBy;
    }

    // Property accessors
    @Id

    @Column(name = "ID", unique = true, nullable = false)

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Column(name = "GROUP_ID", nullable = false)

    public UUID getGroupId() {
        return this.groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    @Column(name = "LEFT_FUNCTION_NAME", length = 30)

    public String getLeftFunctionName() {
        return this.leftFunctionName;
    }

    public void setLeftFunctionName(String leftFunctionName) {
        this.leftFunctionName = leftFunctionName;
    }

    @Column(name = "LEFT_PARAMETER1", length = 200)

    public String getLeftParameter1() {
        return this.leftParameter1;
    }

    public void setLeftParameter1(String leftParameter1) {
        this.leftParameter1 = leftParameter1;
    }

    @Column(name = "LEFT_PARAMETER1_TYPE", precision = 2, scale = 0)

    public Byte getLeftParameter1Type() {
        return this.leftParameter1Type;
    }

    public void setLeftParameter1Type(Byte leftParameter1Type) {
        this.leftParameter1Type = leftParameter1Type;
    }

    @Column(name = "LEFT_PARAMETER2", length = 200)

    public String getLeftParameter2() {
        return this.leftParameter2;
    }

    public void setLeftParameter2(String leftParameter2) {
        this.leftParameter2 = leftParameter2;
    }

    @Column(name = "LEFT_PARAMETER2_TYPE", precision = 2, scale = 0)

    public Byte getLeftParameter2Type() {
        return this.leftParameter2Type;
    }

    public void setLeftParameter2Type(Byte leftParameter2Type) {
        this.leftParameter2Type = leftParameter2Type;
    }

    @Column(name = "LEFT_PARAMETER3", length = 200)

    public String getLeftParameter3() {
        return this.leftParameter3;
    }

    public void setLeftParameter3(String leftParameter3) {
        this.leftParameter3 = leftParameter3;
    }

    @Column(name = "LEFT_PARAMETER3_TYPE", precision = 2, scale = 0)

    public Byte getLeftParameter3Type() {
        return this.leftParameter3Type;
    }

    public void setLeftParameter3Type(Byte leftParameter3Type) {
        this.leftParameter3Type = leftParameter3Type;
    }

    @Column(name = "LEFT_USING_DISTINCT", precision = 1, scale = 0)

    public Boolean getLeftUsingDistinct() {
        return this.leftUsingDistinct;
    }

    public void setLeftUsingDistinct(Boolean leftUsingDistinct) {
        this.leftUsingDistinct = leftUsingDistinct;
    }

    @Column(name = "RELATIONAL_OPERATOR", length = 30)

    public String getRelationalOperator() {
        return this.relationalOperator;
    }

    public void setRelationalOperator(String relationalOperator) {
        this.relationalOperator = relationalOperator;
    }

    @Column(name = "RIGHT_FUNCTION_NAME", length = 30)

    public String getRightFunctionName() {
        return this.rightFunctionName;
    }

    public void setRightFunctionName(String rightFunctionName) {
        this.rightFunctionName = rightFunctionName;
    }

    @Column(name = "RIGHT_PARAMETER1", length = 200)

    public String getRightParameter1() {
        return this.rightParameter1;
    }

    public void setRightParameter1(String rightParameter1) {
        this.rightParameter1 = rightParameter1;
    }

    @Column(name = "RIGHT_PARAMETER1_TYPE", precision = 2, scale = 0)

    public Byte getRightParameter1Type() {
        return this.rightParameter1Type;
    }

    public void setRightParameter1Type(Byte rightParameter1Type) {
        this.rightParameter1Type = rightParameter1Type;
    }

    @Column(name = "RIGHT_PARAMETER2", length = 200)

    public String getRightParameter2() {
        return this.rightParameter2;
    }

    public void setRightParameter2(String rightParameter2) {
        this.rightParameter2 = rightParameter2;
    }

    @Column(name = "RIGHT_PARAMETER2_TYPE", precision = 2, scale = 0)

    public Byte getRightParameter2Type() {
        return this.rightParameter2Type;
    }

    public void setRightParameter2Type(Byte rightParameter2Type) {
        this.rightParameter2Type = rightParameter2Type;
    }

    @Column(name = "RIGHT_PARAMETER3", length = 200)

    public String getRightParameter3() {
        return this.rightParameter3;
    }

    public void setRightParameter3(String rightParameter3) {
        this.rightParameter3 = rightParameter3;
    }

    @Column(name = "RIGHT_PARAMETER3_TYPE", precision = 2, scale = 0)

    public Byte getRightParameter3Type() {
        return this.rightParameter3Type;
    }

    public void setRightParameter3Type(Byte rightParameter3Type) {
        this.rightParameter3Type = rightParameter3Type;
    }

    @Column(name = "RIGHT_USING_DISTINCT", precision = 1, scale = 0)

    public Boolean getRightUsingDistinct() {
        return this.rightUsingDistinct;
    }

    public void setRightUsingDistinct(Boolean rightUsingDistinct) {
        this.rightUsingDistinct = rightUsingDistinct;
    }

    @Column(name = "DESCRIPTION")

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "ORDINAL", precision = 8, scale = 0)

    public Integer getOrdinal() {
        return this.ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    @Column(name = "STATUS", nullable = false, precision = 2, scale = 0)

    public Byte getStatus() {
        return this.status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Column(name = "CREATED_TIME", nullable = false, length = 11)

    public Timestamp getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @Column(name = "CREATED_BY")

    public UUID getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "LAST_MODIFIED_TIME", nullable = false, length = 11)

    public Timestamp getLastModifiedTime() {
        return this.lastModifiedTime;
    }

    public void setLastModifiedTime(Timestamp lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    @Column(name = "LAST_MODIFIED_BY")

    public UUID getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setLastModifiedBy(UUID lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

}