package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetConditionItem;
import com.sinosoft.ops.cimp.repository.sheet.SheetConditionItemDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("sheetConditionItemDao")
public class SheetConditionItemDaoImpl extends BaseEntityDaoImpl<SheetConditionItem> implements SheetConditionItemDao {

    @Override
    public List<SheetConditionItem> findAll() {
        Query query = sessionFactory.getCurrentSession().createQuery("FROM SheetConditionItem");
        return query.list();
    }

    @Override
    public List<SheetConditionItem> GetDataByConditionID(String conditionid) {
        Query query = sessionFactory.getCurrentSession().createQuery("FROM SheetConditionItem where CONDITION_ID=:conditionid order by CONDITION_NUM").setParameter("conditionid", conditionid);
        return query.list();
    }
}
