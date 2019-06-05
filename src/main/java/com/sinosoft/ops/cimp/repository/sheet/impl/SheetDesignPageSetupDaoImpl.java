package com.sinosoft.ops.cimp.repository.sheet.impl;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignPageSetup;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignPageSetupDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.UUID;

/**
 * Created by lixianfu on 2018/5/29.
 */
@Repository("sheetDesignPageSetupDao")
public class SheetDesignPageSetupDaoImpl extends BaseEntityDaoImpl<SheetDesignPageSetup> implements SheetDesignPageSetupDao {

    public SheetDesignPageSetupDaoImpl(EntityManagerFactory factory) {
        super(factory);
    }

    @Override
    public SheetDesignPageSetup getByDesignId(UUID designId) {
        return getById(designId);  
    }

    @Override
    public String create(SheetDesignPageSetup sheetDesignPageSetup){
    
    	UUID uuid = (UUID) save(sheetDesignPageSetup);
        return uuid.toString();  
    }

    @Override
    public void deleteByDesignId(UUID designId) {
       deleteById(designId);
    }

    @Override
    public void update(SheetDesignPageSetup sheetDesignPageSetup){
        super.update(sheetDesignPageSetup);
    }
}
