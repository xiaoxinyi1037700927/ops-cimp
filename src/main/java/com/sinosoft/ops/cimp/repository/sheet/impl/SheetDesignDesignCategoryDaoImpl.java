/**
 * @Project:      IIMP
 * @Title:          SheetDesignDesignCategoryDaoImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignDesignCategory;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignDesignCategoryDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.UUID;



/**
 * @ClassName:  SheetDesignDesignCategoryDaoImpl
 * @description: 表格设计所属设计分类数据访问实现类
 * @author:        Ni
 * @date:            2018年5月25日 下午1:43:19
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository("sheetDesignDesignCategoryDao")
public class SheetDesignDesignCategoryDaoImpl extends BaseEntityDaoImpl<SheetDesignDesignCategory> implements SheetDesignDesignCategoryDao {

    public SheetDesignDesignCategoryDaoImpl(EntityManagerFactory factory) {
        super(factory);
    }

    @Override
    public int deleteByDesignId(UUID designId) {
        return sessionFactory.getCurrentSession().createQuery("delete from SheetDesignDesignCategory where designId=:designId")
                .setParameter("designId", designId)
                .executeUpdate();
    }

    @Override
    public int deleteByCategoryId(UUID categoryId) {
        return sessionFactory.getCurrentSession().createQuery("delete from SheetDesignDesignCategory where categoryId=:categoryId")
                .setParameter("categoryId", categoryId)
                .executeUpdate();
    }

    @Override
    public int deleteByDesignIdAndCategoryId(UUID designId, UUID categoryId) {
        return sessionFactory.getCurrentSession().createQuery("delete from SheetDesignDesignCategory where designId=:designId and categoryId=:categoryId")
                .setParameter("designId", designId)
                .setParameter("categoryId", categoryId)
                .executeUpdate();
    }

    @Override
    public SheetDesignDesignCategory getByDesignId(UUID designId,UUID categoryId) {
        return (SheetDesignDesignCategory)sessionFactory.getCurrentSession().createQuery("from SheetDesignDesignCategory where designId=:designId and categoryId=:categoryId")
                .setParameter("designId", designId)
                .setParameter("categoryId", categoryId).uniqueResult();
    }

    @Override
    public SheetDesignDesignCategory getByDesignId(UUID designId) {
        return (SheetDesignDesignCategory)sessionFactory.getCurrentSession().createQuery("from SheetDesignDesignCategory where designId=:designId")
                .setParameter("designId", designId).uniqueResult();
    }
}
