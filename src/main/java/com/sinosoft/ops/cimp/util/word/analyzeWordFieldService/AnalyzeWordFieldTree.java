package com.sinosoft.ops.cimp.util.word.analyzeWordFieldService;


import com.sinosoft.ops.cimp.common.model.DefaultTreeNode;

/**
 * Copyright © 2018 eSunny Info. Tech Ltd. All rights reserved.
 * @Package: analyzeWordFieldService
 * @author: wft  
 * @date: 2018年6月6日 下午2:40:48
 * @Description: 
 */
public class AnalyzeWordFieldTree extends DefaultTreeNode implements java.io.Serializable {
    private static final long serialVersionUID = -3508026875668813825L;
    
    private String description;
    
    private String fieldType;
    
    private Integer ordinal;
    
    private String no;
	
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	
	public Integer getOrdinal() {
		return ordinal;
	}
	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	@Override
	public String toString() {
		return "AnalyzeWordFieldTree [description=" + description + ", id=" + id + ", code=" + code + ", text=" + text
				+ ", parentId=" + parentId + ", leaf=" + leaf + ", selectable=" + selectable + ", expanded=" + expanded
				+ ", checked=" + checked + ", nodeType=" + nodeType + ", data=" + data + ", children=" + children + "]";
	}
    
}
