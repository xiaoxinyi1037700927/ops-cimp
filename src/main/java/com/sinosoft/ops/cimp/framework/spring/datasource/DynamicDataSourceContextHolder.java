/**
 * @project:     iimp-gradle
 * @title:          DynamicDataSourceContextHolder.java
 * @copyright: ©2018 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.framework.spring.datasource;

/**
 * @ClassName: DynamicDataSourceContextHolder
 * @description: 动态数据源上下文保持器
 * @author:        Ni
 * @date:            2018年8月7日 下午6:57:03
 * @version        1.0.0
 * @since            JDK 1.7
 */
public class DynamicDataSourceContextHolder {
    private static final ThreadLocal<String> holder = new ThreadLocal<String>();
    private static final ThreadLocal<String> realHolder = new ThreadLocal<String>();
    
    /** 
     * 设置数据源
     * @param dataSource 数据源
     * @author Ni
     * @date:    2018年8月7日 上午11:59:15
     * @since JDK 1.7
     */
    public static void setDataSource(String dataSource) {
        holder.set(dataSource);
    }

    /** 
     * 获取数据源
     * @return 数据源
     * @author Ni
     * @date:    2018年8月7日 上午11:58:42
     * @since JDK 1.7
     */
    public static String getDataSource() {
        return holder.get();
    }
    
    /** 
     * 设置数据源
     * @param dataSource 数据源
     * @author Ni
     * @date:    2018年8月7日 上午11:59:15
     * @since JDK 1.7
     */
    public static void setRealDataSource(String realDataSource) {
        realHolder.set(realDataSource);
    }

    /** 
     * 获取实际数据源（在标注为slave由系统自动分配时获取实际分配到的）
     * @return 数据源
     * @author Ni
     * @date:    2018年8月7日 上午11:58:42
     * @since JDK 1.7
     */
    public static String getRealDataSource() {
        return realHolder.get();
    }
 
    /** 
     * 清除数据源
     * @author Ni
     * @date:    2018年8月7日 上午11:58:59
     * @since JDK 1.7
     */
    public static void clearDataSource() {
        holder.remove();
        realHolder.remove();
    }    
}
