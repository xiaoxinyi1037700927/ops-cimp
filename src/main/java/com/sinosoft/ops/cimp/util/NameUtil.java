/**
 * @Project:      IIMP
 * @Title:          NameUtil.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.util;

/**
 * @ClassName:  NameUtil
 * @Description: 名字工具类
 * @Author:        Nil
 * @Date:            2017年9月23日 上午10:15:12
 * @Version        1.0.0
 */
public class NameUtil {
    public static final char SEPARATOR = '_';
    
    /** 
     * 将名字转换为下划线命名（UnderScoreCase）风格的名字
     * @param name 要转换的名字
     * @return 下划线命名（UnderScoreCase）风格的名字
     */
    public static String toUnderScoreCase(String name){
        StringBuilder sb = new StringBuilder();
        boolean upperCase=false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            boolean nextUpperCase = true;
            if (i < (name.length() - 1)) {
                nextUpperCase = Character.isUpperCase(name.charAt(i + 1));
            }
            if ((i >= 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    if (i > 0) sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }
    
    /** 
     * 将名字转换为驼峰命名法（CamelCase）风格的名字
     * @param name 要转换的名字
     * @return 驼峰命名法（CamelCase）风格的名字
     */
    public static String toCamelCase(String name){
        name = name.toLowerCase();
        
        StringBuilder sb = new StringBuilder(name.length());
        boolean upperCase = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c==SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    /** 
     * 将名字转换为首字母大写驼峰命名法（CapitalizeCamelCase）风格的名字
     * @param name 要转换的名字
     * @return 首字母大写驼峰命名法（CapitalizeCamelCase）风格的名字
     */
    public static String toCapitalizeCamelCase(String name) {
        name = toCamelCase(name);
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
    
    public static void main(String[] args) {
        System.out.println(NameUtil.toUnderScoreCase("EmpC025").toUpperCase());
        System.out.println(NameUtil.toUnderScoreCase("SystemTagLibrary").toUpperCase());
        System.out.println(NameUtil.toUnderScoreCase("C001001A").toUpperCase());
        System.out.println("-------------------------");
        System.out.println(NameUtil.toCamelCase("EMP_C025"));
        System.out.println(NameUtil.toCamelCase("SYSTEM_TAG_LIBRARY"));
        System.out.println(NameUtil.toCamelCase("C001001_A"));
        System.out.println("-------------------------");
        System.out.println(NameUtil.toCapitalizeCamelCase("EMP_C025"));
        System.out.println(NameUtil.toCapitalizeCamelCase("SYSTEM_TAG_LIBRARY"));
        System.out.println(NameUtil.toCapitalizeCamelCase("C001001_A"));        
    }
}
