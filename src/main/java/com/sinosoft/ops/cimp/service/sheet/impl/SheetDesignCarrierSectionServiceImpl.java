/**
 * @Project:      IIMP
 * @Title:          SheetDesignCarrierServiceImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet.impl;

import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCarrierSection;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignCarrierSectionDao;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignCarrierSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @ClassName:  SheetDesignCarrierSectionServiceImpl
 * @Description: 表格设计载体数据区服务实现
 * @Version        1.0.0
 */
@Service("sheetDesignCarrierSectionService")
public class SheetDesignCarrierSectionServiceImpl extends BaseEntityServiceImpl<SheetDesignCarrierSection> implements SheetDesignCarrierSectionService {
	@SuppressWarnings("unused")
	@Autowired
	private SheetDesignCarrierSectionDao sheetDesignCarrierSectionDao;

	
}