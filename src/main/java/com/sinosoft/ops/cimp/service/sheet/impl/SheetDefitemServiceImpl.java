package com.sinosoft.ops.cimp.service.sheet.impl;


import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDefitem;
import com.sinosoft.ops.cimp.repository.sheet.SheetDefitemDao;
import com.sinosoft.ops.cimp.service.sheet.SheetDefitemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("SheetDefitemService")
public class SheetDefitemServiceImpl extends BaseEntityServiceImpl<SheetDefitem> implements SheetDefitemService {

    @Autowired
    SheetDefitemDao sheetDefitemDao;

    @Override
    public List<SheetDefitem> findAll() {
        return sheetDefitemDao.findAll();
    }

    @Override
    public List<SheetDefitem> getByInfoSetId(Integer infoSetId) {
        return sheetDefitemDao.getByInfoSetId(infoSetId);
    }
}
