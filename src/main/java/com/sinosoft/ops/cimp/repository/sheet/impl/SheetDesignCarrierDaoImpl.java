/**
 * @project:      IIMP
 * @title:          SheetDesignCarrierDaoImpl.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCarrier;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignCarrierDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignCarrierDaoImpl
 * @description: 表格设计载体数据访问接口实现
 * @author:        Nil
 * @date:            2017年8月18日 下午1:05:52
 * @version        1.0.0
 */
@Repository("sheetDesignCarrierDao")
public class SheetDesignCarrierDaoImpl extends BaseEntityDaoImpl<SheetDesignCarrier> implements SheetDesignCarrierDao {

    public SheetDesignCarrierDaoImpl(EntityManagerFactory factory) {
        super(factory);
    }

    @Override
    public SheetDesignCarrier getByDesignId(UUID designId) {
        return (SheetDesignCarrier) sessionFactory.getCurrentSession().createQuery("from SheetDesignCarrier where designId = :designId")
            .setParameter("designId", designId)
            .uniqueResult();
    }

	@Override
	public int deleteByDesignId(UUID designId) {
        return sessionFactory.getCurrentSession().createQuery("delete SheetDesignCarrier s where s.designId = :designId")
                .setParameter("designId", designId)
                .executeUpdate();
	}

    @Override
    public Collection<UUID> getAllIds() {
        return  sessionFactory.getCurrentSession().createQuery("select id from SheetDesignCarrier")
                .list();
    }
}
