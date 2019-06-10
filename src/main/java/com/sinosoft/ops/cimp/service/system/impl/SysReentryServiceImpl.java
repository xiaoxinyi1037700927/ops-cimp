/**
 * @project:     iimp-gradle
 * @title:          SysReentryServiceImpl.java
 * @copyright: ©2018 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.system.impl;


import com.sinosoft.ops.cimp.common.service.BaseServiceImpl;
import com.sinosoft.ops.cimp.service.system.SysReentryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: SysReentryServiceImpl
 * @description: 系统重入服务实现类
 * @author:        Ni
 * @date:            2018年7月19日 下午3:10:41
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Service("sysReentryService")
public class SysReentryServiceImpl extends BaseServiceImpl implements SysReentryService {
    private static final Map<Object,Long> registeredIds = new ConcurrentHashMap<Object,Long>();
    
    @Override
    public boolean isReentry(Object id) {
        if(registeredIds.containsKey(id)) {
            return true;
        }else {
            registeredIds.put(id, Long.valueOf(System.currentTimeMillis()));
            return false;
        }
    }
    
    @Scheduled(cron = "1/60 * * * * ?")
    public void cleanExpired() {
        long deadline = System.currentTimeMillis() - 60000; 
        for (Iterator<Map.Entry<Object, Long>> it = registeredIds.entrySet().iterator(); it.hasNext();){
            if(it.next().getValue().longValue() < deadline) {
                it.remove();
            }
        }
    }
}
