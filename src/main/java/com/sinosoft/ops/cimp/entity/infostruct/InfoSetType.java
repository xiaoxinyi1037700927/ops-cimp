/**
 * @project:      IIMP
 * @title:          InfoSetType.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.entity.infostruct;

import java.util.HashMap;
import java.util.Map;

/** 
 * @ClassName:  InfoSetType
 * @description: 信息集类型
 * @author:        Nil
 * @date:            2017年10月21日 下午4:57:07
 * @version        1.0.0
 */
public enum InfoSetType {
	/**
	 * 系统表
	 */
	SYSTEM((byte)0,"系统表"),
	/**
	 * 业务表
	 */
	BUSINESS((byte)1,"业务表"),
	/**
	 * 其它
	 */
	OTHER((byte)127,"其它");
	
	private final byte value;
	private final String name;
	
	InfoSetType(final byte value,final String name){
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
        for(InfoSetType e: InfoSetType.values()){
            map.put(e.getValue(), e.getName());
        }
        return map;
    }
}