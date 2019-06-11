package com.sinosoft.ops.cimp.service.sheet.impl;

import com.google.common.base.Throwables;
import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignPageSetup;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignPageSetupDao;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignPageSetupService;
import com.sinosoft.ops.cimp.util.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Created by lixianfu on 2018/5/29.
 */
@Service("sheetDesignPageSetupService")
public class SheetDesignPageSetupServiceImpl extends BaseEntityServiceImpl<SheetDesignPageSetup> implements SheetDesignPageSetupService {

    private static final Logger logger = LoggerFactory.getLogger(SheetDesignPageSetupService.class);

    @Autowired
    private SheetDesignPageSetupDao sheetDesignPageSetupDao;

    @Override
    @Transactional(readOnly=true)
    public SheetDesignPageSetup getByDesignId(UUID designId) {
        return sheetDesignPageSetupDao.getByDesignId(designId);
    }

    @Override
    @Transactional	
    public String save(SheetDesignPageSetup sheetDesignPageSetup) {
     
        return sheetDesignPageSetupDao.create(sheetDesignPageSetup);
    }

    @Override
    @Transactional
    public void update(SheetDesignPageSetup sheetDesignPageSetup) {
        try{
            SheetDesignPageSetup old = sheetDesignPageSetupDao.getByDesignId(sheetDesignPageSetup.getDesignId());
            BeanUtils.copyProperties(sheetDesignPageSetup,old); 
            sheetDesignPageSetupDao.update(old);
        }catch (Exception e){
            logger.error("sheetDesignPageSetupService error:{}", Throwables.getStackTraceAsString(e));
            throw  new RuntimeException("更新操作失败……");
        }

    }

    @Override
    @Transactional
    public void deleteByDesignId(UUID designId) {
        sheetDesignPageSetupDao.deleteByDesignId(designId);
    }
}
