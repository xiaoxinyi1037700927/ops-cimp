/**
 * @Project:      IIMP
 * @Title:          SheetSheetCategoryDaoImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet.impl;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetSheetCategory;
import com.sinosoft.ops.cimp.repository.sheet.SheetSheetCategoryDao;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.UUID;

/**
 * @ClassName:  SheetSheetCategoryDaoImpl
 * @description: 表格所属分类数据访问实现类
 * @author:        Ni
 * @date:            2018年5月25日 下午1:43:19
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository("sheetSheetCategoryDao")
public class SheetSheetCategoryDaoImpl extends BaseEntityDaoImpl<SheetSheetCategory> implements SheetSheetCategoryDao {

    public SheetSheetCategoryDaoImpl(EntityManagerFactory factory) {
        super(factory);
    }

    @Override
    public int deleteBySheetId(UUID sheetId) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.setFlushMode(FlushMode.MANUAL);
        int result = session.createQuery("delete from SheetSheetCategory where sheetId=:sheetId")
                .setParameter("sheetId", sheetId)
                .executeUpdate();
        session.flush();
        session.getTransaction().commit();
        return result;
    }

    @Override
    public int deleteByCategoryId(UUID categoryId) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.setFlushMode(FlushMode.MANUAL);
        int result = session.createQuery("delete from SheetSheetCategory where categoryId=:categoryId")
                .setParameter("categoryId", categoryId)
                .executeUpdate();
        session.flush();
        session.getTransaction().commit();
        return result;
    }

    @Override
    public int deleteBySheetIdAndCategoryId(UUID sheetId, UUID categoryId) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.setFlushMode(FlushMode.MANUAL);
        int result = session.createQuery("delete from SheetSheetCategory where sheetId=:sheetId and categoryId=:categoryId")
                .setParameter("sheetId", sheetId)
                .setParameter("categoryId", categoryId)
                .executeUpdate();
        session.flush();
        session.getTransaction().commit();
        return result;
    }

    @Override
    public SheetSheetCategory getBySheetId(UUID sheetId) {
        return (SheetSheetCategory) sessionFactory.getCurrentSession().createQuery("from SheetSheetCategory where sheetId=:sheetId")
                .setParameter("sheetId", sheetId)
                .uniqueResult();
    }


}
