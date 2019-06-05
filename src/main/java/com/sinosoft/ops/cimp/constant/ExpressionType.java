/**
 * @Project:      IIMP
 * @Title:          ExpressionType.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.constant;

/**
 * @ClassName:  ExpressionType
 * @Description: 表达式类型
 * @Author:        Ni
 * @Date:            2017年8月9日 上午10:58:01
 * @Version        1.0.0
 */
public enum ExpressionType {
    /***表内计算式*/
    TABLE_IN_CALCULATION((byte)0,"表内计算式"),
    /***表内校核式*/
    TABLE_IN_CHECKING((byte)1,"表内校核式"),
	/***表外校核式*/
	TABILE_ABROAD_CALCULATION((byte)2,"表外计算式"),
	/***表外校核式*/
	TABILE_ABROAD_CHECKING((byte)3,"表外校核式");
	
    private final byte value;
    private final String name;
    
    ExpressionType(final byte value,final String name){
        this.value = value;
        this.name = name;
    }

    public byte getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }
    public static String getTypeName(Byte type) {
        for (ExpressionType expressionType : ExpressionType.values()) {
            if(expressionType.getValue() == type) {
                return expressionType.getName();
            }
        }
        return "";
    }
    public static Byte getTypeValue(String name) {
        for (ExpressionType expressionType : ExpressionType.values()) {
            if(expressionType.getName() == name) {
                return expressionType.getValue();
            }
        }
        return 0;
    }
}
