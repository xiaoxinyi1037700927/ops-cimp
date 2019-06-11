/**
 * @project:     IIMP
 * @title:          InputType.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.entity.infostruct;

import java.util.HashMap;
import java.util.Map;

/** 
 * @ClassName:  InputType
 * @description: 信息输入类型
 * @author:        Nil
 * @date:            2017年10月21日 下午4:57:07
 * @version        1.0.0
 */
public enum InputType {
    /**
     * 文本
     */
    TEXT("text","文本"),
    /**
     * 日期
     */
    DATE("date","日期"),
    /**
     * 年份
     */
    YEAR("year","年份"),
    /**
     * 数字
     */
    NUMBER("number","数字"),    
    /**
     * 下拉列表
     */
    COMBOX("combox","下拉列表"),
    /**
     * 下拉树
     */
    COMBOX_TREE("comboxtree","下拉树"),
    /**
     * 代码选择窗口（单选）
     */
    CODE("code","代码选择窗口（单选）"),
    /**
     * 代码选择窗口（多选）
     */
    MULTI_SELECT_CODE("multi_select_code","代码选择窗口（多选）"),
    /**
     * 多行文本
     */
    TEXT_AREA("textarea","多行文本"),
    /**
     * 复选框
     */
    CHECK_BOX("checkbox","复选框"),
    /**
     * 单选框
     */
    RADIO("radio","单选框"),
    /**
     * 图片
     */
    IMAGE("image","图片"),
    /**
     * 密码
     */
    PASSWORD("password","密码"),
    /**
     * 隐藏域
     */
    HIDDEN("hidden","隐藏域"),
    /**
     * 附件
     */
    ATTACHMENT("attachment","附件"),
    /**
     * 树选择窗口（如单位树、机构树等）（多选）
     */
    TREE("tree","树选择窗口（如机构树等）（多选）"),
    /**
     * 树选择窗口（如单位树、机构树等）（单选）
     */
    SINGLE_SELECT_TREE("single_select_tree","树选择窗口（如机构树等）（单选）");
	
	private final String value;
	private final String name;
	
	InputType(final String value,final String name){
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
        for(InputType e: InputType.values()){
            map.put(e.getValue(), e.getName());
        }
        return map;
    }
}