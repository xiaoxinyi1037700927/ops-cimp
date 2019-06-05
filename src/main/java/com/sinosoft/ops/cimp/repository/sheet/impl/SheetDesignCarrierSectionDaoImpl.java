/**
 * @Project:      IIMP
 * @Title:          SheetDesignExpressionDaoImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCarrierSection;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignCarrierSectionDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

/**
 * @ClassName:  SheetDesignCarrierSectionDaoImpl
 * @description: 表格设计载体数据区访问实现类
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Transactional
@Repository("sheetDesignCarrierSectionDao")
public class SheetDesignCarrierSectionDaoImpl  extends BaseEntityDaoImpl<SheetDesignCarrierSection> implements SheetDesignCarrierSectionDao {
    public SheetDesignCarrierSectionDaoImpl(EntityManagerFactory factory) {
        super(factory);
    }
}
