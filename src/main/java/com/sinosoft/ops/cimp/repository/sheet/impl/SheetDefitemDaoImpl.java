package com.sinosoft.ops.cimp.repository.sheet.impl;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDefitem;
import com.sinosoft.ops.cimp.repository.sheet.SheetDefitemDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository("SheetDefitemDao")
public class SheetDefitemDaoImpl extends BaseEntityDaoImpl<SheetDefitem> implements SheetDefitemDao {

    public SheetDefitemDaoImpl(EntityManagerFactory factory) { super(factory);}

    @Override
    public List<SheetDefitem> findAll()
    {
        return sessionFactory.getCurrentSession().createQuery("from SheetDefitem").list();
    }

    @Override
    public List<SheetDefitem> getByInfoSetId(Integer infoSetId) {
        return sessionFactory.getCurrentSession().createQuery("from SheetDefitem where infoSetId=:infoSetId").setParameter("infoSetId",infoSetId).list();
    }


}
