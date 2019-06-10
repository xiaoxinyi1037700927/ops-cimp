/**
 * @project:     iimp-gradle
 * @title:          TargetDataSource.java
 * @copyright: ©2018 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.framework.spring.datasource.annotation;

import com.sinosoft.ops.cimp.framework.spring.datasource.DynamicDataSource;

import java.lang.annotation.*;



/**
 * @ClassName: TargetDataSource
 * @description: 目标数据源
 * @author:        Ni
 * @date:            2018年8月7日 上午10:25:42
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface TargetDataSource {
    String value() default DynamicDataSource.DEFAULT_DATASOURCE;
}
