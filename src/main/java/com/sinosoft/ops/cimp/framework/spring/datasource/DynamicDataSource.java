/**
 * @project:     IIMP
 * @title:          DynamicDataSource.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.framework.spring.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/** 
 * @ClassName: DynamicDataSource
 * @description: 动态数据源
 * @author:        Ni
 * @date:            2018年8月7日 上午12:02:49
 * @version        1.0.0
 * @since            JDK 1.7
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    /*** 缺省数据源标识 */
    public static final String DEFAULT_DATASOURCE = "master";
    /*** 从数据源标识集 */
    private String[] slaveDataSources;
    /*** 当前从数据源标识索引 */
    private static AtomicInteger currentSlaveDataSourceIndex = new AtomicInteger(Integer.MIN_VALUE);
    /*** 是否正在更新 */
    private static AtomicBoolean isUpdating=new AtomicBoolean(false);
    
    @Override
    protected Object determineCurrentLookupKey() {
        String dataSource = DynamicDataSourceContextHolder.getDataSource();
        if (null == dataSource) {
            DynamicDataSourceContextHolder.setRealDataSource(DEFAULT_DATASOURCE);
            return DEFAULT_DATASOURCE;
        }
        //从数据源如果标注为slave则由系统自动分配
        if("slave".equalsIgnoreCase(dataSource)&&(slaveDataSources.length>0)) {
            int index = currentSlaveDataSourceIndex.incrementAndGet();
            while(index<0) {//小于0表示未初始化，初始化为随机值，避免多服务器时队首的数据源负载过大
                if(isUpdating.compareAndSet(false, true)) {
                    currentSlaveDataSourceIndex.set(new Random().nextInt(slaveDataSources.length)-1);
                    isUpdating.set(false);
                }
                index = currentSlaveDataSourceIndex.incrementAndGet();
            }
            while(index>=(slaveDataSources.length-1)) {//到达队尾，从头开始（最后一个保留）
                if(isUpdating.compareAndSet(false, true)) {
                    currentSlaveDataSourceIndex.set(-1);
                    isUpdating.set(false);
                }
                index = currentSlaveDataSourceIndex.incrementAndGet();
            }
            DynamicDataSourceContextHolder.setRealDataSource(slaveDataSources[index]);
            return slaveDataSources[index];
        }
        DynamicDataSourceContextHolder.setRealDataSource(dataSource);
        return dataSource;
    }
    
    public String[] getSlaveDataSources() {
        return slaveDataSources;
    }
    public void setSlaveDataSources(String[] slaveDataSources) {
        this.slaveDataSources = slaveDataSources;
    }
}
