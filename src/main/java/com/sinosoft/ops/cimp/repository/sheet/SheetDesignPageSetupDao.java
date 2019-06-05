package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignPageSetup;

import java.util.UUID;

/**
 * Created by lixianfu on 2018/5/29.
 */
public interface SheetDesignPageSetupDao extends BaseEntityDao<SheetDesignPageSetup> {
	
    
    /**
     * 查询一个实体
     * @param designId 主键id
     * @return	SheetDesignPageSetup实体对象
     * @author lixianfu
     * @date:    2018年5月29日 下午11:46:53
     * @since JDK 1.7
     */
    SheetDesignPageSetup getByDesignId(UUID designId);

    /**
     * 保存一个实体
     * @param sheetDesignPageSetup 对象实体
     * @return      主键uuid
     */
    String create(SheetDesignPageSetup sheetDesignPageSetup);

    /**
     *  修改一个实体
     * @param sheetDesignPageSetup
     */
    void update(SheetDesignPageSetup sheetDesignPageSetup);
  
    /**
     * 删除一个实体
     * @param designId 主键id
     */
    void deleteByDesignId(UUID designId);
}
