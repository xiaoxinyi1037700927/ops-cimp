/**
 * @project:     IIMP
 * @title:          CodeSetType.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.entity.system;

import java.util.HashMap;
import java.util.Map;

/** 
 * @ClassName:  CodeSetType
 * @description: 代码类型
 * @author:        Nil
 * @date:            2017年10月21日 下午4:57:07
 * @version        1.0.0
 */
public enum CodeSetType {
	/**
	 * 国家标准
	 */
    NATIONAL_STANDARD((byte)1,"国家标准"),
	/**
	 * 地方标准
	 */
    PROVINCIAL_STANDARD((byte)2,"地方标准"),
    /**
     * 企业标准
     */
    COMPANY_STANDARD((byte)3,"企业标准"),    
	/**
	 * 自定义标准
	 */
    CUSTOM_STANDARD((byte)9,"自定义标准");
	
	private final byte value;
	private final String name;
	
	CodeSetType(final byte value,final String name){
		this.value = value;
		this.name = name;
	}

	public byte getValue() {
		return this.value;
	}
	
	public String getName() {
		return this.name;
	}
	
    /** 
     * 转为映射表
     * @return 映射表
     */
    public static Map<Byte, String> toMap() {
        Map<Byte, String> map = new HashMap<Byte, String>();
        for(JoinType e: JoinType.values()){
            map.put(e.getValue(), e.getName());
        }
        return map;
    }	
}