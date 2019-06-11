/**
 * @project:     IIMP
 * @title:          JoinType.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.entity.system;

import java.util.HashMap;
import java.util.Map;

/** 
 * @ClassName:  JoinType
 * @description: 代码类型
 * @author:        Nil
 * @date:            2017年10月21日 下午4:57:07
 * @version        1.0.0
 */
public enum JoinType {
	/**
	 * 内连接
	 */
    INNER_JOIN((byte)1,"内连接"),
	/**
	 * 左外连接
	 */
    LEFT_OUTER_JOIN((byte)2,"左外连接"),
    /**
     * 右外连接
     */
    RIGHT_OUTER_JOIN((byte)3,"右外连接");
	
	private final byte value;
	private final String name;
	
	JoinType(final byte value,final String name){
		this.value = value;
		this.name = name;
	}

	public byte getValue() {
		return this.value;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static byte getValueByName(String name){
//		if(name.equalsIgnoreCase(JoinType.INNER_JOIN.getName())) {
//			return JoinType.INNER_JOIN.getValue();
//		} else if(name.equalsIgnoreCase(JoinType.LEFT_OUTER_JOIN.getName())) {
//			return JoinType.LEFT_OUTER_JOIN.getValue();
//		} else {
//			return JoinType.RIGHT_OUTER_JOIN.getValue();
//		}
		for( JoinType item:JoinType.values() ){
			if( item.getName().equalsIgnoreCase(name) ){
				return item.getValue();
			}
		}
		return JoinType.LEFT_OUTER_JOIN.getValue();
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