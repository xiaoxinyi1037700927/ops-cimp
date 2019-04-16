package com.sinosoft.ops.cimp.export.common.bean;

import com.sinosoft.ops.cimp.util.ParticularUtils;

import java.io.Serializable;


/**
 * 简历属性 bean
 *
 * @author shixianggui
 * @Date: 20180306
 */
public class AttributeBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;// 属性名称, 例如: 姓名/性别/民族
    private int nameIndex;// 属性名称所在html文件的 index
    private int valueIndex;// 属性值所在 HTML 的 index

    // div 相关属性
    private String divId;
    private String position;
    private String left;
    private String leftLogicExpression;
    private String bottom;
    private String bottomLogicExpression;
    private String width;
    private String widthLogicExpression;
    private String height;
    private String heightLogicExpression;

    private String processType; // 处理类型

    public AttributeBean(String name, String divId, String leftLogicExpression, String bottomLogicExpression,
                         String widthLogicExpression, String heightLogicExpression, String processType) {
        super();
        this.name = name;
        this.divId = divId;
        this.leftLogicExpression = leftLogicExpression;
        this.bottomLogicExpression = bottomLogicExpression;
        this.widthLogicExpression = widthLogicExpression;
        this.heightLogicExpression = heightLogicExpression;
        this.processType = processType;

        this.nameIndex = -1;
        this.valueIndex = -1;
        this.position = "absolute";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public int getValueIndex() {
        return valueIndex;
    }

    public void setValueIndex(int valueIndex) {
        this.valueIndex = valueIndex;
    }

    public String getDivId() {
        return divId;
    }

    public void setDivId(String divId) {
        this.divId = divId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getLeftLogicExpression() {
        return leftLogicExpression;
    }

    public void setLeftLogicExpression(String leftLogicExpression) {
        this.leftLogicExpression = leftLogicExpression;
    }

    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }

    public String getBottomLogicExpression() {
        return bottomLogicExpression;
    }

    public void setBottomLogicExpression(String bottomLogicExpression) {
        this.bottomLogicExpression = bottomLogicExpression;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getWidthLogicExpression() {
        return widthLogicExpression;
    }

    public void setWidthLogicExpression(String widthLogicExpression) {
        this.widthLogicExpression = widthLogicExpression;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHeightLogicExpression() {
        return heightLogicExpression;
    }

    public void setHeightLogicExpression(String heightLogicExpression) {
        this.heightLogicExpression = heightLogicExpression;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getDivPosition() {
        return "position:" + position + "; ";
    }

    public String getDivExpression(String fieldName) {
        try {
            String logicExpression = ParticularUtils.trim(ParticularUtils.getFieldValue(this, fieldName + "LogicExpression", true), "");
            if ("".equals(logicExpression)) {
                return fieldName + ":" + ParticularUtils.getFieldValue(this, fieldName, true) + "px; ";
            }
            if (ParticularUtils.isRealNumber(logicExpression)) {
                return fieldName + ":" + logicExpression + "px; ";
            }
            return fieldName + ":" + ParticularUtils.calculateLogicExpression(this, logicExpression) + "px; ";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fieldName + ":0px; ";
    }

    @Override
    public String toString() {
        return "AttributeBean [name=" + name + ", nameIndex=" + nameIndex + ", valueIndex=" + valueIndex + ", divId="
                + divId + ", position=" + position + ", left=" + left + ", leftLogicExpression=" + leftLogicExpression
                + ", bottom=" + bottom + ", bottomLogicExpression=" + bottomLogicExpression + ", width=" + width
                + ", widthLogicExpression=" + widthLogicExpression + ", height=" + height + ", heightLogicExpression="
                + heightLogicExpression + ", processType=" + processType + "]" + '\n';
    }

}











