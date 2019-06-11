package com.sinosoft.ops.cimp.service.sheet.impl;

import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignSql;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignSqlDao;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignSqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * 
 * @ClassName:  SheetDesignSqlServiceImpl
 * @description: 表格设计SQL 服务实现类
 * @author:        sunch
 * @date:            2018年6月6日 下午1:51:51
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Service
public class SheetDesignSqlServiceImpl extends BaseEntityServiceImpl<SheetDesignSql> implements SheetDesignSqlService {

	@Autowired
	private SheetDesignSqlDao sheetDesignSqlDao;

	@Override
	@Transactional
	public Integer getMaxOrdinal() {
		return sheetDesignSqlDao.getMaxOrdinal();
	}
}
