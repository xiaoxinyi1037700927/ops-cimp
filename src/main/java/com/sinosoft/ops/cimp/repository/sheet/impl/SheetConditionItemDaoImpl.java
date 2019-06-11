package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetConditionItem;
import com.sinosoft.ops.cimp.repository.sheet.SheetConditionItemDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.UUID;

@Repository("sheetConditionItemDao")
public class SheetConditionItemDaoImpl extends BaseEntityDaoImpl<SheetConditionItem> implements SheetConditionItemDao {

    public SheetConditionItemDaoImpl(EntityManagerFactory factory) {
        super(factory);
    }

    @Override
    public List<SheetConditionItem> findAll() {
        Query query = sessionFactory.getCurrentSession().createQuery("FROM SheetConditionItem");
        return query.list();
    }

    @Override
    public List<SheetConditionItem> GetDataByConditionID(UUID conditionid) {
        Query query = sessionFactory.getCurrentSession().createQuery("FROM SheetConditionItem where CONDITION_ID=:conditionid order by CONDITION_NUM").setParameter("conditionid", conditionid);
        return query.list();
    }
}
