package com.sinosoft.ops.cimp.config;

import com.sinosoft.ops.cimp.util.CachePackage.OrganizationCacheManager;
import com.sinosoft.ops.cimp.util.CachePackage.SysTableModelInfoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class InitCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitCache.class);


    @Autowired
    public InitCache() {
    }

    @Bean
    CommandLineRunner initSysCache() {
        long startTime1 = System.currentTimeMillis();
        LOGGER.debug("开始缓存单位...");
        OrganizationCacheManager.getSubject().getAllListByDB();
        long endTime1 = System.currentTimeMillis();
        LOGGER.debug("缓存单位结束...耗时：" + new BigDecimal((endTime1 - startTime1) / 1000).setScale(2, RoundingMode.HALF_UP).doubleValue() + "秒");

        //加载干部和单位模型到缓存
        SysTableModelInfoManager.getInstance().getSysTableModelInfo("CadreInfo");
        SysTableModelInfoManager.getInstance().getSysTableModelInfo("DepartmentInfo");
        SysTableModelInfoManager.getInstance().getSysTableModelInfoDTO("CadreInfo", "200");
        SysTableModelInfoManager.getInstance().getSysTableModelInfoDTO("DepartmentInfo", "200");

        return null;
    }
}
