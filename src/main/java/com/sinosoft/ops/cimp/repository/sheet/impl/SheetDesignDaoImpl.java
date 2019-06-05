/**
 * @Project:      IIMP
 * @Title:          SheetDesignDaoImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet.impl;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.common.model.PageableQueryParameter;
import com.sinosoft.ops.cimp.common.model.PageableQueryResult;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesign;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignDao;
import com.sinosoft.ops.cimp.util.toSqlUtils;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName:  SheetDesignDaoImpl
 * @description: 表格设计数据访问实现类
 * @author:        Ni
 * @date:            2018年5月25日 下午1:43:19
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository("sheetDesignDao")
public class SheetDesignDaoImpl extends BaseEntityDaoImpl<SheetDesign> implements SheetDesignDao {

	public SheetDesignDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

    @SuppressWarnings("unchecked")
    @Override
    public Collection<SheetDesign> getByCategoryId(UUID categoryId) {
        return sessionFactory.getCurrentSession().createQuery("from SheetDesign d join SheetDesignDesignCategory c on d.id=c.designId where d.status=0 and c.categoryId = :categoryId")
            .setParameter("categoryId", categoryId)
            .list();
    }


	@SuppressWarnings("unchecked")
	@Override
	public Collection<UUID> getSheetCategoryId(UUID designId) {
    	String strDesignId=designId.toString().toUpperCase().replace("-","");
		return sessionFactory.getCurrentSession()
				.createNativeQuery("select sheet_category.id\n" +
				"from sheet_design\n" +
				"left join sheet_design_design_category\n" +
				"on sheet_design.id = sheet_design_design_category.design_id\n" +
				"left join sheet_category\n" +
				"on sheet_design_design_category.category_id =\n" +
				"sheet_category.design_category_id\n" +
				"where sheet_design_design_category.design_id =:designId\n" +
				"and sheet_category.id not in\n" +
				"(select sheet_sheet_category.category_id\n" +
				"from sheet\n" +
				"inner join sheet_sheet_category\n" +
				"on sheet.id = sheet_sheet_category.sheet_id\n" +
				"and sheet.design_id = :designId) \n")
				.setParameter("designId", strDesignId)
				.list();
	}

    
    @Override
    public PageableQueryResult findByPage(PageableQueryParameter queryParameter) {
        PageableQueryResult result = new PageableQueryResult(queryParameter.getPageNo(),queryParameter.getPageSize());
//        StringBuilder sb = new StringBuilder("from SheetDesign d join SheetDesignDesignCategory c on d.id=c.designId where ( 1 = 1 )");
//        if(queryParameter!=null){
//            String conStr = "";
//            Map<String, Object> pramsMap = queryParameter.getParameters();
//            Set pramsKeys = pramsMap.keySet();
//            for (Object aKey : pramsKeys) {
//            	if ("categoryId".equals(aKey.toString())) {
//            		Object value = queryParameter.getParameters().get(aKey.toString());
//            		if (value != null && value instanceof Collection) {
//            			conStr += " and c." + aKey.toString() + " in (:" + aKey.toString() + ")";
//            		} else {
//            			conStr += " and c." + aKey.toString() + "=:" + aKey.toString();
//            		}
//            	} else if ("type".equals(aKey.toString())) {
//        			conStr += " and d." + aKey.toString() + " in (:" + aKey.toString() + ")";
//            	} else if ("name".equals(aKey.toString())) {
//            		conStr += " and d." + aKey.toString() + " LIKE :" + aKey.toString();
//            	} else if ("releaseComName".equals(aKey.toString())) {
//            		conStr += " and d." + aKey.toString() + " LIKE :" + aKey.toString();
//            	} else if ("startTime".equals(aKey.toString())) {
//            		conStr += " and d.releaseTime>=:" + aKey.toString();
//            	} else if ("endTime".equals(aKey.toString())) {
//            		conStr += " and d.releaseTime<:" + aKey.toString();
//            	} else if("releaseComId".equals(aKey)){
//            		conStr += " and (d.releaseComId in :releaseComId or d.releaseComId is null) ";
//            	}else {
//                	conStr += " and d." + aKey.toString() + "=:" + aKey.toString();
//            	}
//            }
//            sb.append(conStr).append(" ORDER BY c.categoryId,d.ordinal ");
//        }
//        if(queryParameter.getTotalCount()==-1){
//            result.setTotalCount(this.getTotalCountWithHql(new StringBuilder("select count(*) ").append(sb.toString()).toString(), queryParameter.getParameters()));
//        }
//
//		Collection<SheetDesign> sheetDesigns = (Collection<SheetDesign>) this.findByPageWithHql(calculateFirstResult(queryParameter.getPageNo(),queryParameter.getPageSize()), queryParameter.getPageSize(), new StringBuilder("select d ").append(sb.toString()).toString(), queryParameter.getParameters());

//        UUID id, String name, String description, String sheetNo, Integer version,
//        Byte dataFillType, String instructions, Byte type, Integer top, Integer left, Integer right, Integer bottom,
//        Boolean editable, Integer ordinal, String appId, Boolean locked, Boolean archived, Byte status,
//        Timestamp createdTime, UUID createdBy, Timestamp lastModifiedTime, UUID lastModifiedBy,
//        Timestamp releaseTime, String releaseComId, String releaseComName, Timestamp expiredTime,
//        String releaseComIds, String releaseComNames, String lockPassword
        
//        StringBuilder sb = new StringBuilder("select d.id,d.name,d.description,d.sheet_no sheetNo,d.version version,"
//        		+ "d.data_fill_Type dataFillType,d.instructions instructions,d.type,d.top,d.left,d.right,d.bottom,"
//        		+ "d.editable,d.ordinal,d.app_Id appId,d.locked,d.archived,d.status,"
//        		+ "d.created_time createdTime,d.created_By createdBy,d.last_modified_time lastModifiedTime,d.last_modified_by lastModifiedBy,"
//        		+ "d.release_time releaseTime,d.release_com_id releaseComId,d.release_com_name releaseComName,d.expired_time expiredTime,"
//        		+ "d.release_com_ids releaseComIds,d.release_com_names releaseComNames,d.lock_password lockPassword"
//        		+ " from Sheet_Design d join Sheet_Design_Design_Category c on d.id=c.design_Id where ( 1 = 1 )");

		String strItem = toSqlUtils.getModelColumn(SheetDesign.class);

		StringBuilder sb = new StringBuilder("select ").append(strItem).append(" from Sheet_Design  join Sheet_Design_Design_Category on Sheet_Design.id=Sheet_Design_Design_Category.design_Id where ( 1 = 1 )");
        if(queryParameter!=null){
            String conStr = "";
            Map<String, Object> pramsMap = queryParameter.getParameters();
            if(!pramsMap.containsKey("depIds") && pramsMap.containsKey("sheetCategoryId"))
			{
				pramsMap.remove("sheetCategoryId");
			}
            Set pramsKeys = pramsMap.keySet();
            for (Object aKey : pramsKeys) {
            	if ("categoryId".equals(aKey.toString())) {
            		Object value = queryParameter.getParameters().get(aKey.toString());
            		if (value != null && value instanceof Collection) {
            			conStr += " and Sheet_Design_Design_Category." + "category_id" + " in (:" + aKey.toString() + ")";
            		} else {
            			conStr += " and Sheet_Design_Design_Category." + "category_id" + "=:" + aKey.toString();
            		}
            	} else if ("type".equals(aKey.toString())) {
        			conStr += " and Sheet_Design." + aKey.toString() + " in (:" + aKey.toString() + ")";
            	} else if ("name".equals(aKey.toString())) {
            		conStr += " and Sheet_Design." + aKey.toString() + " LIKE :" + aKey.toString();
            	} else if ("releaseComName".equals(aKey.toString())) {
            		conStr += " and Sheet_Design." + "release_com_name" + " LIKE :" + aKey.toString();

				} else if ("output".equals(aKey.toString())) {
					conStr += " and Sheet_Design.RELEASE_TIME is not null " ;
            	} else if ("startTime".equals(aKey.toString())) {
            		conStr += " and Sheet_Design.release_Time>=:" + aKey.toString();
            	} else if ("endTime".equals(aKey.toString())) {
            		conStr += " and Sheet_Design.release_Time<:" + aKey.toString();
            	} else if("sheetCategoryId".equals(aKey)){
				} else if("depIds".equals(aKey)){
            		if(pramsMap.containsKey("sheetCategoryId"))
					{
						conStr += " and (exists (select 1 from dep_b001 a where Sheet_Design.release_Com_Id=a.dep_id and exists (select 1 from dep_b001 b where b.dep_id in :depIds and a.tree_level_code like b.tree_level_code||'%')) or Sheet_Design.release_Com_Id is null or Sheet_Design.id in (select design_id from sheet left join sheet_sheet_category on sheet.id=sheet_sheet_category.sheet_id where sheet_sheet_category.category_id=:sheetCategoryId)) ";
					}
					else
					{
						conStr += " and (exists (select 1 from dep_b001 a where Sheet_Design.release_Com_Id=a.dep_id and exists (select 1 from dep_b001 b where b.dep_id in :depIds and a.tree_level_code like b.tree_level_code||'%')) or Sheet_Design.release_Com_Id is null) ";
					}

            	} else if("paramId".equals(aKey)){
            		conStr += " and (exists (select 1 from sheet_design_parameter where sheet_design.id = sheet_design_parameter.design_id and sheet_design_parameter.PARAMETER_ID=:paramId))";
            	}else {
                	conStr += " and Sheet_Design." + aKey.toString() + "=:" + aKey.toString();
            	}
            }
            sb.append(conStr).append(" ORDER BY Sheet_Design.sheet_no,Sheet_Design_Design_Category.category_Id,Sheet_Design.ordinal ");
        }
        if(queryParameter.getTotalCount()==-1){
        	result.setTotalCount(this.getTotalCountWithSql(new StringBuilder("select count(*) from (").append(sb.toString()).toString()+")", queryParameter.getParameters()));

        }
        Collection<SheetDesign> sheetDesigns = (Collection<SheetDesign>) this.findByPageWithSql(calculateFirstResult(queryParameter.getPageNo(),queryParameter.getPageSize()), queryParameter.getPageSize(), sb.toString(), queryParameter.getParameters());

		List<Map> lstmap = new ArrayList<>();

		Object value = queryParameter.getParameters().get("categoryId");
		if (value != null && value instanceof Collection) {
			lstmap = getRefnum((Collection<UUID>) queryParameter.getParameters().get("categoryId"));
		} else {
			lstmap = getRefnum(queryParameter.getParameters().get("categoryId").toString());
		}

		for(SheetDesign sheetDesign:sheetDesigns)
		{
			List<Map> temp = lstmap.stream().filter(item-> sheetDesign.getId().toString().equals(item.get("ID"))).collect(Collectors.toList());
			if (temp.size()>0 && temp.get(0).get("REFNUM")!=null)
			{
				sheetDesign.setRefNum(Integer.parseInt(temp.get(0).get("REFNUM").toString()));
			}
		}

        result.setData(sheetDesigns);
        return result;
    }
    
    public long getTotalCountByHql(String totalCountHql, Map<String, Object> params) {
        Query<?> query = sessionFactory.getCurrentSession().createQuery(totalCountHql);
        if(params!=null&&!params.isEmpty()){
            prepareQueryParam(query,params);
        }
        return ((BigDecimal)query.uniqueResult()).longValue();
    }
    

    /**
     * 为查询对象准备查询参数
     * @param query 查询对象
     * @param params 参数集
     */
    protected void prepareQueryParam(Query<?> query,final Map<String,Object> params){
        for (String key : params.keySet()) {
            Object value = params.get(key);
        	if("name".equals(key)){
                query.setParameter("name", "%" + value + "%");
        	} else if("releaseComName".equals(key)){
                query.setParameter("releaseComName", "%" + value + "%");
			} else if("output".equals(key)){
            } else if("startTime".equals(key)) {
                try {
					query.setParameter(key, currDateFormat((String)value));
				} catch (ParseException e) {
					query.setParameter(key, "");
				}
            } else if("endTime".equals(key)) {
                try {
					query.setParameter(key, nextDateFormat((String)value));
				} catch (ParseException e) {
					query.setParameter(key, "");
				}
        	} else if (value instanceof Collection<?>) {
                query.setParameterList(key, (Collection<?>)value);
            } else if(value instanceof Object[]) {
                query.setParameterList(key, (Object[])value);
            } else {
                query.setParameter(key, value);
            }
        }
    }
    
    private Date currDateFormat(String oldDateStr) throws ParseException {
	  	DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
	  	return df1.parse(oldDateStr);
	}

	@SuppressWarnings("deprecation")
	private Date nextDateFormat(String oldDateStr) throws ParseException {
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		Date date = df1.parse(oldDateStr);
		date.setDate(date.getDate() + 1);
	  	return date;
	}

	@Override
	public boolean addOrdinals(int upOrdinal, UUID categoryId, UUID userName) {
		String hql = "UPDATE SheetDesign SET ordinal=ordinal+1, lastModifiedBy=:userName, lastModifiedTime=SYSDATE " + 
				"WHERE id IN (SELECT designId from SheetDesignDesignCategory WHERE categoryId=:categoryId) AND ordinal IS NOT NULL AND ordinal>:upOrdinal";
		int cnt = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("categoryId", categoryId)
                .setParameter("upOrdinal", upOrdinal)
                .setParameter("userName", userName)
                .executeUpdate();
		return cnt > 0 ? true : false;
	}

	@Override
	public boolean checkSheetNo(String sheetNo) {
		Long cnt =(Long) sessionFactory.getCurrentSession().createQuery("select count(id) from SheetDesign where sheetNo =:sheetNo")
				.setParameter("sheetNo", sheetNo).uniqueResult();
		return cnt > 0 ? false : true;
	}

	@Override
	public Collection<String> getSecCondition(String designId,String sectionNo)
	{
		List<Map> lst = sessionFactory.getCurrentSession().createNativeQuery("select CONDITION_ID from sheet_design_condition where DESIGN_ID=:designId and SECTION_NO=:sectionNo")
				.setParameter("designId", UUID.fromString(designId)).setParameter("sectionNo", sectionNo).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();

		Collection<String> clt= new ArrayList<>();
		for(Map map:lst)
		{
			clt.add(toUUIDStringByRaw(map.get("CONDITION_ID")));
		}
		return clt;
	}

	@Override
	public Collection<String> getSecField(String designId,String sectionNo)
	{
		List<Map> lst = sessionFactory.getCurrentSession().createNativeQuery("SELECT FIELD_ID FROM Sheet_Design_Field_Binding where DESIGN_ID=:designId and SECTION_NO=:sectionNo")
				.setParameter("designId", UUID.fromString(designId)).setParameter("sectionNo", sectionNo).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();

		Collection<String> clt= new ArrayList<>();
		for(Map map:lst)
		{
			clt.add(toUUIDStringByRaw(map.get("FIELD_ID")));
		}
		return clt;
	}

	@Override
	public boolean checkSheetNo(String sheetNo,UUID id) {
		Long cnt =(Long) sessionFactory.getCurrentSession().createQuery("select count(id) from SheetDesign where sheetNo =:sheetNo and id<>:id")
				.setParameter("sheetNo", sheetNo).setParameter("id",id).uniqueResult();
		return cnt > 0 ? false : true;
	}

	@Override
	public int getMaxOrdinal(UUID categoryId) {
		String hql = "FROM SheetDesign WHERE id IN (SELECT designId from SheetDesignDesignCategory WHERE categoryId=:categoryId)";
		@SuppressWarnings("unchecked")
		Collection<SheetDesign> coll = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("categoryId", categoryId)
                .list();
		int max = 0;
		if (coll != null && coll.size() > 0) {
			for (SheetDesign item : coll) {
				if (item.getOrdinal() != null) {
					int aOrdinal = item.getOrdinal();
					if (aOrdinal > max) {
						max = aOrdinal;
					}
				}
			}
		}
		return max;
	}

	@Override
	public int minusOrdinals(int thisOrdinal, UUID categoryId, UUID userName) {
		String hql = "UPDATE SheetDesign SET ordinal=ordinal-1, lastModifiedBy=:userName, lastModifiedTime=SYSDATE " + 
				"WHERE id IN (SELECT designId from SheetDesignDesignCategory WHERE categoryId=:categoryId) AND ordinal IS NOT NULL AND ordinal>:thisOrdinal";
		int cnt = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("categoryId", categoryId)
                .setParameter("thisOrdinal", thisOrdinal)
                .setParameter("userName", userName)
                .executeUpdate();
		return cnt;
	}

    @SuppressWarnings("unchecked")
	@Override
	public SheetDesign findPrevious(UUID id, UUID categoryId) {
		String hql = "FROM SheetDesign T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetDesign T3 " +
				"WHERE T3.status=0 AND T3.id IN (SELECT T4.designId FROM SheetDesignDesignCategory T4 WHERE T4.categoryId=:categoryId) AND T3.ordinal IS NOT NULL AND " + 
				"T3.ordinal IN (SELECT MAX(T5.ordinal) FROM SheetDesign T5 WHERE T5.status=0 AND T5.id IN " + 
				"(SELECT T7.designId FROM SheetDesignDesignCategory T7 WHERE categoryId=:categoryId) AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal<(SELECT T6.ordinal FROM SheetDesign T6 WHERE T6.id=:id)))";
		List<SheetDesign> list = sessionFactory.getCurrentSession().createQuery(hql)
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
	public int updateOrdinal(UUID id, int newOrdinal, UUID userName) {
		String hql = "UPDATE SheetDesign SET ordinal=:newOrdinal, lastModifiedBy=:userName, lastModifiedTime=SYSDATE WHERE id=:id";
        int cnt = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("id", id)
                .setParameter("userName", userName)
                .setParameter("newOrdinal", newOrdinal)
                .executeUpdate();
		return cnt;
	}

    @SuppressWarnings("unchecked")
	@Override
	public SheetDesign findNext(UUID id, UUID categoryId) {
		String hql = "FROM SheetDesign T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetDesign T3 " +
				"WHERE T3.status=0 AND T3.id IN (SELECT T4.designId FROM SheetDesignDesignCategory T4 WHERE T4.categoryId=:categoryId) AND T3.ordinal IS NOT NULL AND " + 
				"T3.ordinal IN (SELECT MIN(T5.ordinal) FROM SheetDesign T5 WHERE T5.status=0 AND T5.id IN " + 
				"(SELECT T7.designId FROM SheetDesignDesignCategory T7 WHERE categoryId=:categoryId) AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal>(SELECT T6.ordinal FROM SheetDesign T6 WHERE T6.id=:id)))";
		List<SheetDesign> list = sessionFactory.getCurrentSession().createQuery(hql)
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
	public List<Map> getRefSituation(String id) {
		List<Map> list= sessionFactory.getCurrentSession().createSQLQuery(" select a.name,a.DESCRIPTION,a.ID,c.name category_name from sheet a\n" +
				"left join sheet_sheet_category b on a.id=b.sheet_id\n" +
				"left join sheet_category c on b.category_id=c.id\n" +
				"        where a.design_id=:designId").setParameter("designId", UUID.fromString(id)).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();

		for (Map map : list) {
			map.put("ID",toUUIDStringByRaw(map.get("ID")));
		}
		return list;
	}

	public List<Map> getRefnum(String categoryid) {
		List<Map> list= sessionFactory.getCurrentSession().createSQLQuery("                                   select a.id,refnum\n" +
				"                      from sheet_design a\n" +
				"                      left join sheet_design_design_category b on a.id=b.DESIGN_ID\n" +
				"                      left join (select design_id, count(1) refnum\n" +
				"                                   from sheet\n" +
				"                                  group by design_id) temp\n" +
				"                        on a.id = temp.design_id\n" +
				"                        where  b.category_id=:categoryid").setParameter("categoryid", UUID.fromString(categoryid)).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();

		for (Map map : list) {
			map.put("ID",toUUIDStringByRaw(map.get("ID")));
		}
		return list;
	}


	public List<Map> getRefnum(Collection<UUID> categoryids) {
		List<Map> list= sessionFactory.getCurrentSession().createSQLQuery("                                   select a.id,refnum\n" +
				"                      from sheet_design a\n" +
				"                      left join sheet_design_design_category b on a.id=b.DESIGN_ID\n" +
				"                      left join (select design_id, count(1) refnum\n" +
				"                                   from sheet\n" +
				"                                  group by design_id) temp\n" +
				"                        on a.id = temp.design_id\n" +
				"                        where  b.category_id  in :categoryid").setParameterList("categoryid", categoryids).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();

		for (Map map : list) {
			map.put("ID",toUUIDStringByRaw(map.get("ID")));
		}
		return list;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public Collection<String> getAllOrganizationByParent(Collection<String> organizationIds) {
		return sessionFactory.getCurrentSession().createSQLQuery("select a.dep_id from dep_b001 a where exists (select 1 from dep_b001 b where b.dep_id in :depIds and a.tree_level_code like b.tree_level_code||'%')")
				.setParameterList("depIds", organizationIds)
				.list();
	}
	
	@Override
    public long getTotalCountWithSql(final String totalCountSql,final Map<String,Object> params){
        Query<?> query = sessionFactory.getCurrentSession().createNativeQuery(totalCountSql);
        if(params!=null&&!params.isEmpty()){
            prepareQueryParam(query,params);
        }
        return ((BigDecimal)query.uniqueResult()).longValue();
    }
	
	@Override
    public Collection<?> findByPageWithSql(int firstResult, int maxResults, String sql, Map<String, Object> params) {
        Query<?> query = sessionFactory.getCurrentSession().createNativeQuery(sql).addEntity(SheetDesign.class);
        if(params!=null&&!params.isEmpty()){
            prepareQueryParam(query,params);
        }
        return query.setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .list();
    }
}
