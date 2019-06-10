/**
 * @Project:      IIMP
 * @Title:          SheetDesignExpressionDaoImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet.impl;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetRule;
import com.sinosoft.ops.cimp.repository.sheet.SheetRuleDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

/**
 * @ClassName:  SheetRuleDaoImpl
 * @description: 表格规则访问实现类
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Transactional
@Repository("sheetRuleDao")
public class SheetRuleDaoImpl  extends BaseEntityDaoImpl<SheetRule> implements SheetRuleDao {
    public SheetRuleDaoImpl(EntityManagerFactory factory) {
        super(factory);
    }
}
