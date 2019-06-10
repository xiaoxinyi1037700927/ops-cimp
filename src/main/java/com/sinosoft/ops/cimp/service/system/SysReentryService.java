/**
 * @project:     iimp-gradle
 * @title:          SysReentryService.java
 * @copyright: ©2018 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.system;


import com.sinosoft.ops.cimp.common.service.BaseService;

/**
 * @ClassName:  SysReentryService
 * @description: 系统重入服务
 * @author:        Ni
 * @date:            2018年7月19日 下午3:10:13
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SysReentryService extends BaseService {
    /** 
     * 判断标识是否重入
     * @param id 标识
     * @return 是否重入
     * @author Ni
     * @date:    2018年7月19日 下午2:51:11
     * @since JDK 1.7
     */
    boolean isReentry(Object id);
}
