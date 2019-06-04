package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetConditionCategory;
import com.sinosoft.ops.cimp.repository.sheet.SheetConditionCategoryDao;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.List;

/**
 * @description: 条件分类数据层实现
 */
@Repository("sheetConditionCategoryDao")
public class SheetConditionCategoryDaoImpl extends BaseEntityDaoImpl<SheetConditionCategory> implements SheetConditionCategoryDao {

    public SheetConditionCategoryDaoImpl(EntityManagerFactory factory) {
        super(factory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SheetConditionCategory> getCategory() {

        return (List<SheetConditionCategory>) sessionFactory.getCurrentSession().createQuery("from SheetConditionCategory ")
                .list();
    }

    @Override
    public Collection<SheetConditionCategory> findByParentId(String parentid) {
        return sessionFactory.getCurrentSession().createQuery(" from SheetConditionCategory where parentId =:parentid order by ordinal").setParameter("parentid", parentid).list();
    }

    @Override
    public Collection<SheetConditionCategory> findByParentIdAndIds(String parentid, List<String> ids) {
        return sessionFactory.getCurrentSession().createQuery(" from SheetConditionCategory where parentId =:parentid and id in :ids order by ordinal").setParameter("parentid", parentid).setParameterList("ids", ids).list();
    }

    @Override
    public Collection<SheetConditionCategory> getRootData() {
        return sessionFactory.getCurrentSession().createQuery(" from SheetConditionCategory where parentId is null  order by ordinal").list();
    }

    @Override
    public Collection<SheetConditionCategory> getRootDataByIds(List<String> ids) {
        return sessionFactory.getCurrentSession().createQuery(" from SheetConditionCategory where parentId is null and id in :ids order by ordinal").setParameterList("ids", ids).list();
    }

    @Override
    public String save(SheetConditionCategory sheetConditionCategory) {

        return super.save(sheetConditionCategory).toString();
    }

    @Override
    public String getFisrtId() {
        return sessionFactory.getCurrentSession().createQuery("select id from SheetConditionCategory order by ordinal").setMaxResults(1).uniqueResult().toString();
    }

    @Override
    public void update(SheetConditionCategory sheetConditionCategory) {

        super.update(sheetConditionCategory);

    }

    @Override
    public SheetConditionCategory getById(String id) {

        return super.getById(id);
    }

    @Override
    public void deleteId(String Id) {
        sessionFactory.getCurrentSession().createQuery("delete from SheetConditionCategory where Id = :Id")
                .setParameter("Id", Id).executeUpdate();

    }

    @Override
    public Collection<SheetConditionCategory> findAllChildren() {
        return sessionFactory.getCurrentSession().createQuery(" from SheetConditionCategory").list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public SheetConditionCategory findNext(String id) {
        String hql = "FROM SheetConditionCategory T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetConditionCategory T3 " +
                "WHERE T3.status=0 AND ((T3.parentId IN (SELECT T4.parentId FROM SheetConditionCategory T4 WHERE T4.id=:id)) " +
                "OR (T3.parentId IS NULL AND EXISTS (SELECT 1 FROM SheetConditionCategory T9 WHERE T9.id=:id AND T9.parentId IS NULL))) " +
                "AND T3.ordinal IS NOT NULL AND " +
                "T3.ordinal IN (SELECT MIN(T5.ordinal) FROM SheetConditionCategory T5 WHERE T5.status=0 AND ((T5.parentId IN " +
                "(SELECT T7.parentId FROM SheetConditionCategory T7 WHERE id=:id)) " +
                "OR (T5.parentId IS NULL AND EXISTS (SELECT 1 FROM SheetConditionCategory T8 WHERE T8.id=:id AND T8.parentId IS NULL))) " +
                "AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
                "T5.ordinal>(SELECT T6.ordinal FROM SheetConditionCategory T6 WHERE T6.id=:id)))";
        List<SheetConditionCategory> list = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("id", id)
                .list();
        if (list.size() > 0) {
            return list.get(list.size() - 1);
        } else {
            return null;
        }
    }

    @Override
    public int updateOrdinal(String nextId, int ordinal, String userName) {
        String hql = "UPDATE SheetConditionCategory SET ordinal=:newOrdinal, lastModifiedBy=:userName, lastModifiedTime=SYSDATE WHERE id=:id";
        int cnt = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("id", nextId)
                .setParameter("userName", userName)
                .setParameter("newOrdinal", ordinal)
                .executeUpdate();
        return cnt;
    }

    @SuppressWarnings("unchecked")
    @Override
    public SheetConditionCategory findPrevious(String id) {
        String hql = "FROM SheetConditionCategory T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetConditionCategory T3 " +
                "WHERE T3.status=0 AND ((T3.parentId IN (SELECT T4.parentId FROM SheetConditionCategory T4 WHERE T4.id=:id)) " +
                "OR (T3.parentId IS NULL AND EXISTS (SELECT 1 FROM SheetConditionCategory T9 WHERE T9.id=:id AND T9.parentId IS NULL))) " +
                "AND T3.ordinal IS NOT NULL AND " +
                "T3.ordinal IN (SELECT MAX(T5.ordinal) FROM SheetConditionCategory T5 WHERE T5.status=0 AND ((T5.parentId IN " +
                "(SELECT T7.parentId FROM SheetConditionCategory T7 WHERE id=:id)) " +
                "OR (T5.parentId IS NULL AND EXISTS (SELECT 1 FROM SheetConditionCategory T8 WHERE T8.id=:id AND T8.parentId IS NULL))) " +
                "AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
                "T5.ordinal<(SELECT T6.ordinal FROM SheetConditionCategory T6 WHERE T6.id=:id)))";
        List<SheetConditionCategory> list = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("id", id)
                .list();
        if (list.size() > 0) {
            return list.get(list.size() - 1);
        } else {
            return null;
        }
    }

}
