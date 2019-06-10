/**
 * @Project:      IIMP
 * @Title:          SheetDesignCarrierServiceImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet.impl;


import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetRule;
import com.sinosoft.ops.cimp.repository.sheet.SheetRuleDao;
import com.sinosoft.ops.cimp.service.sheet.SheetRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName:  SheetRuleServiceImpl
 * @Description: 表格设计载体数据区服务实现
 * @Version        1.0.0
 */
@Service("sheetRuleService")
public class SheetRuleServiceImpl extends BaseEntityServiceImpl<SheetRule> implements SheetRuleService {
	@SuppressWarnings("unused")
	@Autowired
	private SheetRuleDao sheetRuleDao;

	
}