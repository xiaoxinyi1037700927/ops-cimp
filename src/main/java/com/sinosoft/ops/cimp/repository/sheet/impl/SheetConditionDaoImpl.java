package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.common.model.PageableQueryParameter;
import com.sinosoft.ops.cimp.common.model.PageableQueryResult;
import com.sinosoft.ops.cimp.entity.sheet.SheetCondition;
import com.sinosoft.ops.cimp.entity.sheet.SheetConditionCategory;
import com.sinosoft.ops.cimp.repository.sheet.SheetConditionDao;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.util.*;

@Repository("sheetConditionDao")
public class SheetConditionDaoImpl extends BaseEntityDaoImpl<SheetCondition> implements SheetConditionDao {

    public SheetConditionDaoImpl(EntityManagerFactory factory) {
        super(factory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getName(long id) {
        Collection<String> list = sessionFactory.getCurrentSession().createSQLQuery("SELECT NAME FROM SHEET_CONDITION WHERE ID=:id")
                .setParameter("id", id)
                .list();
        if (list != null && list.size() > 0) {
            for (String aName : list) {
                return aName;
            }
        }
        return null;
    }


    @Override
    public List<SheetCondition> findAll() {
        Query query = sessionFactory.getCurrentSession().createQuery("FROM SheetCondition");
        return query.list();
    }

    @Override
    public List<SheetCondition> GetDataByDataSourceID(String DataSourceID) {
        Query query = sessionFactory.getCurrentSession().createQuery(String.format("FROM SheetCondition where DATA_SOURCE_ID='%S'", DataSourceID));
        return query.list();
    }

    @Override
    public List<SheetCondition> getConditionByCategoryId(String categoryId) {
        Query query = sessionFactory.getCurrentSession().createQuery(" FROM SheetCondition where categoryId=:categoryId order by ordinal").setParameter("categoryId", UUID.fromString(categoryId));
        return query.list();
    }

    @Override
    public List<SheetCondition> getConditionByDesignId(String designId) {
        Query query = sessionFactory.getCurrentSession().createQuery(" FROM SheetCondition where designId=:designId order by ordinal").setParameter("designId", UUID.fromString(designId));
        return query.list();
    }

    @Override
    public List<Map> getRefSituation(String id) {
        List<Map> list = sessionFactory.getCurrentSession().createSQLQuery("   select CONDITION_ID, b.id AS design_id,\n" +
                "       b.NAME,\n" +
                "       b.DESCRIPTION,\n" +
                "       SHEET_NO,\n" +
                "       d.name        category_name\n" +
                "          from sheet_design_condition a\n" +
                " inner join sheet_design b\n" +
                "    on a.design_id = b.id\n" +
                " inner join sheet_design_design_category c\n" +
                "    on b.id = c.design_id\n" +
                " inner join sheet_design_category d\n" +
                "    on c.category_id = d.id\n" +
                "         where CONDITION_ID = :id").setParameter("id", UUID.fromString(id)).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();

        for (Map map : list) {
            map.put("CONDITION_ID", toUUIDStringByRaw(map.get("CONDITION_ID")));
            map.put("DESIGN_ID", toUUIDStringByRaw(map.get("DESIGN_ID")));
        }
        return list;
    }

    @Override
    public SheetCondition GetConditionDataById(String id) {
        Query query = sessionFactory.getCurrentSession().createQuery("FROM SheetCondition where id=:id").setParameter("id", UUID.fromString(id));
        return (SheetCondition) query.list().get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public SheetCondition findNext(String id, String categoryId) {
        String hql = "FROM SheetCondition T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetCondition T3 " +
                "WHERE T3.status=0 AND T3.id IN (SELECT T4.id FROM SheetCondition T4 WHERE T4.categoryId=:categoryId) AND T3.ordinal IS NOT NULL AND " +
                "T3.ordinal IN (SELECT MIN(T5.ordinal) FROM SheetCondition T5 WHERE T5.status=0 AND T5.id IN " +
                "(SELECT T7.id FROM SheetCondition T7 WHERE categoryId=:categoryId) AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
                "T5.ordinal>(SELECT T6.ordinal FROM SheetCondition T6 WHERE T6.id=:id)))";
        List<SheetCondition> list = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("id", id)
                .setParameter("categoryId", categoryId)
                .list();
        if (list.size() > 0) {
            return list.get(list.size() - 1);
        } else {
            return null;
        }
    }


    @Override
    public int updateOrdinal(String nextId, int ordinal, String userName) {
        String hql = "UPDATE SheetCondition SET ordinal=:newOrdinal, lastModifiedBy=:userName, lastModifiedTime=SYSDATE WHERE id=:id";
        int cnt = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("id", nextId)
                .setParameter("userName", userName)
                .setParameter("newOrdinal", ordinal)
                .executeUpdate();
        return cnt;
    }


    @SuppressWarnings("unchecked")
    @Override
    public SheetCondition findPrevious(String id, String categoryId) {
        String hql = "FROM SheetCondition T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetCondition T3 " +
                "WHERE T3.status=0 AND T3.id IN (SELECT T4.id FROM SheetCondition T4 WHERE T4.categoryId=:categoryId) AND T3.ordinal IS NOT NULL AND " +
                "T3.ordinal IN (SELECT MAX(T5.ordinal) FROM SheetCondition T5 WHERE T5.status=0 AND T5.id IN " +
                "(SELECT T7.id FROM SheetCondition T7 WHERE categoryId=:categoryId) AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
                "T5.ordinal<(SELECT T6.ordinal FROM SheetCondition T6 WHERE T6.id=:id)))";
        List<SheetCondition> list = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("id", id)
                .setParameter("categoryId", categoryId)
                .list();
        if (list.size() > 0) {
            return list.get(list.size() - 1);
        } else {
            return null;
        }
    }


    @Override
    public PageableQueryResult findByPage(PageableQueryParameter queryParameter) {

        PageableQueryResult result = new PageableQueryResult(queryParameter.getPageNo(), queryParameter.getPageSize());
        String sqlitem = "Sheet_Condition_Category.Name CategoryName ,Sheet_Condition.id,condition_name conditionName,nvl(refnum,0) refNum";
        StringBuilder sb = new StringBuilder("select count(Sheet_Condition.id) from Sheet_Condition left join Sheet_Condition_Category on Sheet_Condition.Category_Id=Sheet_Condition_Category.id left join (select CONDITION_ID,count(1) refnum from sheet_design_condition group by CONDITION_ID) temp on sheet_condition.id=temp.CONDITION_ID where 1=1 ");
        Map<String, Object> pramsMap = queryParameter.getParameters();
        String conStr = "";
        String includeDown = pramsMap.get("includeDown").toString();
        Set pramsKeys = pramsMap.keySet();
        for (Object aKey : pramsKeys) {
            if ("refNumStart".equals(aKey.toString())) {
                conStr += " and refnum >=" + ":" + aKey.toString();
            } else if ("refNumEnd".equals(aKey.toString())) {
                conStr += " and refnum <=" + ":" + aKey.toString();
            } else if ("keyWords".equals(aKey.toString())) {
                conStr += " and condition_name LIKE :" + aKey.toString();
            } else if ("categoryId".equals(aKey.toString())) {
                if ("1".equals(includeDown)) {
                    //包含子目录
                    conStr += " and Sheet_Condition.category_Id in :category_Id";
                } else {
                    //不包含子目录
                    conStr += " and Sheet_Condition.category_Id = :category_Id";
                }
            }
        }
        sb.append(conStr).append(" ORDER BY Sheet_Condition.ordinal ");
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
        setPara(query, pramsMap);

        BigDecimal count = (BigDecimal) query.uniqueResult();

        if (!count.equals(0)) {
            query = sessionFactory.getCurrentSession().createSQLQuery(sb.toString().replace("count(Sheet_Condition.id)", sqlitem));
            setPara(query, pramsMap);
            int firstResult = Integer.parseInt(pramsMap.get("start").toString());
            int maxResults = Integer.parseInt(pramsMap.get("start").toString()) + Integer.parseInt(pramsMap.get("limit").toString());

            List<Map> list = query.setFirstResult(firstResult)
                    .setMaxResults(maxResults)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

            List<Map> listresult = new ArrayList<>();
            for (Map map : list) {
                Map temp = new HashMap();
                temp.put("categoryName", map.get("CATEGORYNAME"));
                temp.put("id", toUUIDStringByRaw(map.get("ID")));
                temp.put("conditionName", map.get("CONDITIONNAME"));
                temp.put("refNum", map.get("REFNUM"));
                listresult.add(temp);
            }
            result.setData(listresult);
        }
        result.setTotalCount(count.longValue());
        return result;
    }

    private void setPara(Query query, Map pramsMap) {
        String includeDown = pramsMap.get("includeDown").toString();
        Set pramsKeys = pramsMap.keySet();
        for (Object aKey : pramsKeys) {
            if ("categoryId".equals(aKey.toString())) {
                if ("1".equals(includeDown)) {
                    Collection<String> coll = new HashSet<>();
                    coll.add(UUID.fromString(pramsMap.get(aKey.toString()).toString()).toString());
                    coll = getChildCatigories(coll, UUID.fromString(pramsMap.get(aKey.toString()).toString()).toString());
                    query.setParameterList("category_Id", coll);
                } else {
                    query.setParameter("category_Id", UUID.fromString(pramsMap.get(aKey.toString()).toString()));
                }
            } else if ("refNumStart".equals(aKey.toString())) {
                query.setInteger(aKey.toString(), Integer.parseInt(pramsMap.get(aKey.toString()).toString()));
            } else if ("refNumEnd".equals(aKey.toString())) {
                query.setInteger(aKey.toString(), Integer.parseInt(pramsMap.get(aKey.toString()).toString()));
            } else if ("keyWords".equals(aKey.toString())) {
                query.setParameter(aKey.toString(), "%" + pramsMap.get(aKey.toString()) + "%");
            }
        }
    }

    //得到表子目录ID集合
    private Collection<String> getChildCatigories(Collection<String> coll, String upCatigoryId) {
        Collection<SheetConditionCategory> catColl = sessionFactory.getCurrentSession().createQuery("FROM SheetConditionCategory where parentId=:parentId")
                .setParameter("parentId", upCatigoryId)
                .list();

        if (catColl != null && catColl.size() > 0) {
            for (SheetConditionCategory aCat : catColl) {
                String aId = aCat.getId();
                coll.add(aId);
                coll = getChildCatigories(coll, aId);
            }
        }
        return coll;
    }
}
