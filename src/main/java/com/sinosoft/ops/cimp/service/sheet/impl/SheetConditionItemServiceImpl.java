/**
 * @project: IIMP
 * @title: SysCodeSetServiceImpl.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet.impl;


import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetConditionItem;
import com.sinosoft.ops.cimp.repository.sheet.SheetConditionItemDao;
import com.sinosoft.ops.cimp.service.sheet.SheetConditionItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


/**
 * @description: 函数实现
 */
@Service("sheetConditionItemService")
public class SheetConditionItemServiceImpl extends BaseEntityServiceImpl<SheetConditionItem> implements SheetConditionItemService {

    @Autowired
    private SheetConditionItemDao sheetConditionItemDao;

    @Transactional
    @Override
    public List<SheetConditionItem> findAll() {
        return sheetConditionItemDao.findAll();
    }

    @Transactional
    @Override
    public List<SheetConditionItem> GetDataByConditionID(UUID conditionid) {
        return sheetConditionItemDao.GetDataByConditionID(conditionid);
    }

    @Transactional
    @Override
    public void save(SheetConditionItem sheetconditonitems) {
        sheetConditionItemDao.save(sheetconditonitems);
    }
}
