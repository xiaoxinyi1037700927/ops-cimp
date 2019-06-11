/**
 * @Project:      IIMP
 * @Title:          SheetDesignCategoryDaoImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.common.model.TreeNode;
import com.sinosoft.ops.cimp.entity.sheet.SheetCategory;
import com.sinosoft.ops.cimp.entity.sheet.SheetSheetCategory;
import com.sinosoft.ops.cimp.repository.sheet.SheetCategoryDao;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.*;

/**
 * @ClassName:  SheetCategoryDaoImpl
 * @description: 表格分类数据访问实现类
 * @author:        kanglin
 * @date:            2018年5月25日 下午4:07:17
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository("sheetCategoryDao")
public class SheetCategoryDaoImpl extends BaseEntityDaoImpl<SheetCategory> implements SheetCategoryDao {

	public SheetCategoryDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@SuppressWarnings("unchecked")
	public Collection<SheetCategory> getChildren(UUID id) {
		Collection<SheetCategory> list = null;
		if (id != null) {
			list = sessionFactory.getCurrentSession().createQuery("from SheetCategory where status=0 and parentId=:id")
	                .setParameter("id", id)
	                .list();
		} else {
			list = sessionFactory.getCurrentSession().createQuery("from SheetCategory where status=0 and parentId is null")
	                .list();
		}
		if (list == null) {
			list = new ArrayList<SheetCategory>();
		}
		return list;
	}

//	public List<Map> getCategoryMessage(UUID parentId) {
//		List<Map> lst= sessionFactory.getCurrentSession().createNativeQuery("select sheet_category.id, sheet_category.name,case when maxflg=2 then '已上报' when maxflg=1 then '已接收' when maxflg=3 then '打回' else '未接收' end flg\n" +
//				"  from sheet_category\n" +
//				"  left join (select sheet_sheet_category.category_id,\n" +
//				"                    max(flg) maxflg,\n" +
//				"                    min(flg) minflg\n" +
//				"               from sheet\n" +
//				"              inner join sheet_sheet_category\n" +
//				"                 on sheet.id = sheet_sheet_category.sheet_id\n" +
//				"              group by sheet_sheet_category.category_id) temp\n" +
//				"    on sheet_category.id = temp.category_id\n" +
//				" where sheet_category.parent_id = :parentId\n")
//				.setParameter("parentId", parentId)
//				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
//				.list();
//		for(Map temp : lst)
//		{
//			temp.put("ID",toUUIDStringByRaw(temp.get("ID")));
//		}
//		return lst;
//	}
	
	@SuppressWarnings("unchecked")
	public List<Map> getCategoryMessage(UUID parentId) {
		List<Map> lst= sessionFactory.getCurrentSession().createNativeQuery("SELECT sheet_category.id,  sheet_category.name,  "
				+ "CASE    WHEN (EXISTS      (SELECT 1    FROM   (SELECT sheet.flg, sheet_sheet_category.category_id  FROM sheet  JOIN sheet_sheet_category  ON sheet.id=sheet_sheet_category.sheet_id ) A WHERE A.CATEGORY_ID=SHEET_CATEGORY.ID AND A.FLG          =0 )) THEN '未接收' "
				+ "  WHEN (EXISTS (SELECT 1 FROM (SELECT sheet.flg,  sheet_sheet_category.category_id FROM sheet JOIN sheet_sheet_category  ON sheet.id=sheet_sheet_category.sheet_id  ) A  WHERE A.CATEGORY_ID=SHEET_CATEGORY.ID  AND A.FLG          =3  )) THEN '打回' "
				+ " WHEN (EXISTS (SELECT 1 FROM (SELECT sheet.flg,  sheet_sheet_category.category_id  FROM sheet JOIN sheet_sheet_category ON sheet.id=sheet_sheet_category.sheet_id ) A  WHERE A.CATEGORY_ID=SHEET_CATEGORY.ID  AND A.FLG          =4  ))  THEN '撤回' "
				+ " WHEN (EXISTS (SELECT 1  FROM (SELECT sheet.flg,  sheet_sheet_category.category_id  FROM sheet  JOIN sheet_sheet_category ON sheet.id=sheet_sheet_category.sheet_id ) A WHERE A.CATEGORY_ID=SHEET_CATEGORY.ID AND A.FLG  =6 ))  THEN '审核中' "
				+ " WHEN (NOT EXISTS (SELECT 1  FROM (SELECT sheet.flg, sheet_sheet_category.category_id FROM sheet JOIN sheet_sheet_category  ON sheet.id=sheet_sheet_category.sheet_id ) A WHERE A.CATEGORY_ID=SHEET_CATEGORY.ID AND (A.FLG=0  OR A.FLG=3 OR A.FLG=4  OR A.FLG=6 OR A.FLG IS NULL)  )) AND (EXISTS (SELECT 1 FROM (SELECT sheet.flg,  sheet_sheet_category.category_id FROM sheet JOIN sheet_sheet_category  ON sheet.id=sheet_sheet_category.sheet_id  ) A  WHERE A.CATEGORY_ID=SHEET_CATEGORY.ID  AND A.FLG =1  )) THEN '已接收' "
				+ " WHEN ( NOT EXISTS (SELECT 1 FROM (SELECT sheet.flg,  sheet_sheet_category.category_id FROM sheet JOIN sheet_sheet_category ON sheet.id=sheet_sheet_category.sheet_id ) A  WHERE A.CATEGORY_ID=SHEET_CATEGORY.ID AND (A.FLG=0  OR A.FLG=3 OR A.FLG=4  OR A.FLG=6 OR A.FLG IS NULL) )) AND (EXISTS (SELECT 1 FROM (SELECT sheet.flg,  sheet_sheet_category.category_id FROM sheet JOIN sheet_sheet_category  ON sheet.id=sheet_sheet_category.sheet_id  ) A  WHERE A.CATEGORY_ID=SHEET_CATEGORY.ID  AND A.FLG =2  )) THEN '已上报' "
				+ " WHEN ( NOT EXISTS (SELECT 1 FROM (SELECT sheet.flg,  sheet_sheet_category.category_id FROM sheet JOIN sheet_sheet_category  ON sheet.id=sheet_sheet_category.sheet_id ) A WHERE A.CATEGORY_ID=SHEET_CATEGORY.ID AND (A.FLG=0  OR A.FLG=3 OR A.FLG=4  OR A.FLG=6 OR A.FLG IS NULL) )) AND (EXISTS (SELECT 1 FROM (SELECT sheet.flg,  sheet_sheet_category.category_id FROM sheet JOIN sheet_sheet_category  ON sheet.id=sheet_sheet_category.sheet_id  ) A  WHERE A.CATEGORY_ID=SHEET_CATEGORY.ID  AND A.FLG =5  )) THEN '审核' ELSE '未接收'  END flg "
				+ " FROM sheet_category where sheet_category.parent_id=:parentId order by sheet_category.ordinal")
				.setParameter("parentId", parentId)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.list();
		for(Map temp : lst)
		{
			temp.put("ID",toUUIDStringByRaw(temp.get("ID")));
		}
		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<SheetCategory> getRoot() {
        return sessionFactory.getCurrentSession().createQuery("from SheetCategory where status=0 and parentId is null")
                .list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<SheetCategory> getRootByTypeAndParam(String type,Byte param) {
		if(type == null){
			type = "0";
		}
		byte t = Byte.parseByte(type);
		int p = param.intValue();
		return sessionFactory.getCurrentSession().createQuery("from SheetCategory where type =:type and reportType=:reportType and status=0 and parentId is null order by ordinal")
				.setParameter("type", t)
				.setParameter("reportType", p)
                .list();
	}

    @SuppressWarnings("unchecked")
    @Override
    public Collection<SheetCategory> getByType(Byte type) {
        return sessionFactory.getCurrentSession().createQuery("from SheetCategory where type =:type order by ordinal")
                .setParameter("type", type)
                .list();
    }

	@Override
	public UUID save(SheetCategory sheetCategory) {
		return (UUID)super.save(sheetCategory);
	}

	@Override
	public boolean updateName(UUID id, String name, UUID userName) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		String hql = "UPDATE SheetCategory SET name=:name, lastModifiedBy=:userName, lastModifiedTime=SYSDATE WHERE id=:id";
        int cnt = session.createQuery(hql)
                .setParameter("id", id)
                .setParameter("name", name)
                .setParameter("userName", userName)
                .executeUpdate();
		session.flush();
		session.getTransaction().commit();
		return cnt > 0 ? true : false;
	}

	@Override
	public boolean addOrdinals(int upOrdinal, UUID parentId, UUID userName) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		String parentCon = "parentId=:parentId ";
		if (parentId == null) {
			parentCon = "(parentId IS NULL OR parentId='') ";
		}
		String hql = "UPDATE SheetCategory SET ordinal=ordinal+1, lastModifiedBy=:userName, lastModifiedTime=SYSDATE WHERE " + 
				parentCon + "AND ordinal IS NOT NULL AND ordinal>:upOrdinal";
		int cnt = -1;
		if (parentId == null) {
			cnt = session.createQuery(hql)
	                .setParameter("upOrdinal", upOrdinal)
	                .setParameter("userName", userName)
	                .executeUpdate();
		} else {
			cnt = session.createQuery(hql)
                .setParameter("parentId", parentId)
                .setParameter("upOrdinal", upOrdinal)
                .setParameter("userName", userName)
                .executeUpdate();
		}
		session.flush();
		session.getTransaction().commit();
		return cnt > 0 ? true : false;
	}

    @SuppressWarnings("unchecked")
	@Override
	public SheetCategory findPrevious(UUID id) {
		String hql = "FROM SheetCategory T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetCategory T3 " +
				"WHERE T3.status=0 AND ((T3.parentId IN (SELECT T4.parentId FROM SheetCategory T4 WHERE T4.id=:id)) " + 
				"OR (T3.parentId IS NULL AND EXISTS (SELECT 1 FROM SheetCategory T9 WHERE T9.id=:id AND T9.parentId IS NULL))) " +
				"AND T3.ordinal IS NOT NULL AND " + 
				"T3.ordinal IN (SELECT MAX(T5.ordinal) FROM SheetCategory T5 WHERE T5.status=0 AND ((T5.parentId IN " + 
				"(SELECT T7.parentId FROM SheetCategory T7 WHERE id=:id)) " + 
				"OR (T5.parentId IS NULL AND EXISTS (SELECT 1 FROM SheetCategory T8 WHERE T8.id=:id AND T8.parentId IS NULL))) " +
				"AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal<(SELECT T6.ordinal FROM SheetCategory T6 WHERE T6.id=:id)))";
		List<SheetCategory> list = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("id", id)
                .list();
		if (list.size() > 0) {
			return list.get(list.size() - 1);
		} else {
			return null;
		}
	}

	@Override
	public int updateOrdinal(UUID id, int newOrdinal, UUID userName) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		String hql = "UPDATE SheetCategory SET ordinal=:newOrdinal, lastModifiedBy=:userName, lastModifiedTime=SYSDATE WHERE id=:id";
        int cnt = session.createQuery(hql)
                .setParameter("id", id)
                .setParameter("userName", userName)
                .setParameter("newOrdinal", newOrdinal)
                .executeUpdate();
		session.flush();
		session.getTransaction().commit();
		return cnt;
	}

    @SuppressWarnings("unchecked")
	@Override
	public SheetCategory findNext(UUID id) {
		String hql = "FROM SheetCategory T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetCategory T3 " +
				"WHERE T3.status=0 AND ((T3.parentId IN (SELECT T4.parentId FROM SheetCategory T4 WHERE T4.id=:id)) " + 
				"OR (T3.parentId IS NULL AND EXISTS (SELECT 1 FROM SheetCategory T9 WHERE T9.id=:id AND T9.parentId IS NULL))) " +
				"AND T3.ordinal IS NOT NULL AND " + 
				"T3.ordinal IN (SELECT MIN(T5.ordinal) FROM SheetCategory T5 WHERE T5.status=0 AND ((T5.parentId IN " + 
				"(SELECT T7.parentId FROM SheetCategory T7 WHERE id=:id)) " + 
				"OR (T5.parentId IS NULL AND EXISTS (SELECT 1 FROM SheetCategory T8 WHERE T8.id=:id AND T8.parentId IS NULL))) " +
				"AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal>(SELECT T6.ordinal FROM SheetCategory T6 WHERE T6.id=:id)))";
		List<SheetCategory> list = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("id", id)
                .list();
		if (list.size() > 0) {
			return list.get(list.size() - 1);
		} else {
			return null;
		}
	}

    @SuppressWarnings("unchecked")
	@Override
	public Collection<SheetSheetCategory> findSheetByCategory(UUID categoryId) {
		String hql = "FROM SheetSheetCategory WHERE categoryId=:categoryId AND status=0";
		Collection<SheetSheetCategory> list = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("categoryId", categoryId)
                .list();
		return list;
	}

    @SuppressWarnings("unchecked")
	@Override
	public int minusOrdinals(UUID id, UUID parentId, UUID userName) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		String parentCon = "T1.parentId=:parentId ";
		if (parentId == null) {
			parentCon = "(T1.parentId IS NULL OR T1.parentId='') ";
		}
		String hql = "UPDATE SheetCategory T1 SET T1.ordinal=T1.ordinal-1, T1.lastModifiedBy=:userName, T1.lastModifiedTime=SYSDATE " +
				"WHERE T1.ordinal IS NOT NULL AND " + parentCon +
				"AND T1.ordinal>(SELECT T3.ordinal FROM SheetCategory T3 WHERE T3.id=:id)";
		int cnt = -1;
		if (parentId == null) {
        	cnt = session.createQuery(hql)
                .setParameter("id", id)
                .setParameter("userName", userName)
                .executeUpdate();
		} else {
        	cnt = session.createQuery(hql)
                    .setParameter("id", id)
                    .setParameter("userName", userName)
                    .setParameter("parentId", parentId)
                    .executeUpdate();
		}
		session.flush();
		session.getTransaction().commit();
		return cnt;
	}

    @SuppressWarnings("unchecked")
	@Override
	public Collection<SheetCategory> findAllByOrdinal() {
		String hql = "from SheetCategory where status=0 order by ordinal";
		Collection<SheetCategory> list = sessionFactory.getCurrentSession().createQuery(hql)
                .list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<SheetCategory> getByType(String type,Integer param) {
		String hql = "from SheetCategory where status=0 and type=:type and reportType=:param order by ordinal";
		Collection<SheetCategory> list = sessionFactory.getCurrentSession().createQuery(hql).setParameter("type",Byte.valueOf(type))
				.setParameter("param", param)
				.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<? extends TreeNode> getByTypeAndOrg(String type, Collection<String> organizationIds,Byte param) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id,NAME,DESCRIPTION,SCOPE,PARENT_ID,APP_ID,INFO_CATEGORY_ID,SYSTEM_DEFINED,ORDINAL,STATUS,CREATED_TIME,CREATED_BY,LAST_MODIFIED_TIME,LAST_MODIFIED_BY,TYPE,DESIGN_CATEGORY_ID,REPORT_ORG,REPORT_TYPE");
		sql.append(" from SHEET_CATEGORY where type=:type and ");
		if("0".equals(type)){
			if(param==1){
				sql.append(" exists (select 1 from DEP_B001 a where exists (select 1 from DEP_B001 b where b.dep_id in (:orgIds) and a.tree_level_code like b.tree_level_code||'%') and a.dep_id=SHEET_CATEGORY.report_org)");
			}else if(param==2){
				sql.append(" exists (select 1 from PARTY_D001 a where exists (select 1 from PARTY_D001 b where b.party_id in (:orgIds) and a.tree_level_code like b.tree_level_code||'%') and a.party_id=SHEET_CATEGORY.report_org)");
			}
		}else if("1".equals(type)){
			sql.append(" report_org in (:orgIds)");
		}
		sql.append("order by ordinal");
		return (Collection<? extends TreeNode>) sessionFactory.getCurrentSession().createNativeQuery(sql.toString())
				.addEntity(SheetCategory.class)
				.setParameter("type", type)
				.setParameterList("orgIds", organizationIds)
				.list();
		
	}

	@Override
	public String getTopOrgId(Collection<String> organizationIds,Integer reportType) {
		String sql = "";
		if(reportType==1){
			sql = "select a.dep_id from dep_b001 a where a.dep_id in (:orgIds) and rownum=1 and length(a.tree_level_code)=(select min(length(b.tree_level_code)) from dep_b001 b where b.dep_id in (:orgIds))";
		}else if(reportType==2){
			sql = "select a.party_id from party_d001 a where a.party_id in (:orgIds) and rownum=1 and length(a.tree_level_code)=(select min(length(b.tree_level_code)) from party_d001 b where b.party_id in (:orgIds))";
		}
		
		return (String)sessionFactory.getCurrentSession().createNativeQuery(sql)
		.setParameter("orgIds", organizationIds)
		.uniqueResult();
	}

	
}
