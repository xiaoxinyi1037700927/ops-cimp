package com.sinosoft.ops.cimp.service.sheet;


import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignPageSetup;

import java.util.UUID;

/**
 * Created by lixianfu on 2018/5/29.
 */
public interface SheetDesignPageSetupService extends BaseEntityService<SheetDesignPageSetup> {

    /**
     * 查询一个实体
     * @param designId      主键id
     * @return  SheetDesignPageSetup实体
     */
    SheetDesignPageSetup getByDesignId(UUID designId);

    /**
     * 保存一个实体
     * @param sheetDesignPageSetup 对象实体
     * @return      主键uuid
     */
    String save(SheetDesignPageSetup sheetDesignPageSetup);

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
