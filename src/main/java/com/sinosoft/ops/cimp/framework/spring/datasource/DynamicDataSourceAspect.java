/**
 * @project:     iimp-gradle
 * @title:          DynamicDataSourceAspect.java
 * @copyright: ©2018 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.framework.spring.datasource;

import com.sinosoft.ops.cimp.framework.spring.datasource.annotation.TargetDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;



/**
 * @ClassName: DynamicDataSourceAspect
 * @description: 动态数据源切面类
 * @author:        Ni
 * @date:            2018年8月7日 上午11:22:44
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Aspect
@Component
@Order(-1)
public class DynamicDataSourceAspect implements Ordered {
    @Before("@annotation(targetDataSource)")
    public void doBefore(JoinPoint joinPoint, TargetDataSource targetDataSource){
        DynamicDataSourceContextHolder.setDataSource(targetDataSource.value());
    }
    
    @After("@annotation(targetDataSource)")
    public void doAfter(JoinPoint point, TargetDataSource targetDataSource){
        DynamicDataSourceContextHolder.clearDataSource();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
