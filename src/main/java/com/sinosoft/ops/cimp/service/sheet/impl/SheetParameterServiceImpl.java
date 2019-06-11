package com.sinosoft.ops.cimp.service.sheet.impl;


import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetParameter;
import com.sinosoft.ops.cimp.entity.system.SysParameter;
import com.sinosoft.ops.cimp.repository.sheet.SheetParameterDao;
import com.sinosoft.ops.cimp.service.sheet.SheetParameterService;
import com.sinosoft.ops.cimp.service.system.SysParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

@Service("sheetParameterService")
public class SheetParameterServiceImpl  extends BaseEntityServiceImpl<SheetParameter> implements SheetParameterService {

    @Autowired
    SheetParameterDao sheetParameterDao;

    @Autowired
    SysParameterService sysParameterService;

    @Override
    @Transactional(readOnly = true)
    public Collection<SheetParameter> getBySheetId(UUID sheetId)
    {
        Collection<SheetParameter> sheetParameters=sheetParameterDao.getBySheetId(sheetId);
        for(SheetParameter entity : sheetParameters)
        {
            SysParameter sysParameter = sysParameterService.getById(Integer.parseInt(entity.getParameterId()));
            entity.setReferenceCodeSet(sysParameter.getReferenceCodeSet());
            entity.setParameterName(sysParameter.getName());
            entity.setNameCn(sysParameter.getNameCn());
            entity.setValueType(sysParameter.getValueType());
        }

        return sheetParameters;
    }

	@Override
	@Transactional
	public void deleteBySheetId(UUID sheetId) {
		sheetParameterDao.deleteBySheetId(sheetId);
	}
}
