/**
 * @Project:      IIMP
 * @Title:          SheetType.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.entity.sheet;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName:  SheetType
 * @Description: 表格类型
 * @Author:        Nil
 * @Date:            2017年8月8日 下午4:47:21
 * @Version        1.0.0
 */
public enum SheetType {
	/***统计表*/
    STATISTICS((byte)0,"统计表"),
	/***花名册*/
	ROSTER((byte)1,"花名册"),
	/***登记表*/
	REGISTER((byte)2,"登记表"),
	/***台账*/
	ACCOUNT((byte)3,"台账"),
	/***分组花名册*/
	GROUP_ROSTER((byte)4,"分组花名册");
	
	private final byte value;
	private final String name;

	SheetType(final byte value,final String name) {
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
        for(SheetType e: SheetType.values()){
            map.put(e.getValue(), e.getName());
        }
        return map;
    }
}
