package com.sinosoft.ops.cimp.repository.sheet.impl;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.common.model.PageableQueryParameter;
import com.sinosoft.ops.cimp.common.model.PageableQueryResult;
import com.sinosoft.ops.cimp.entity.sheet.GatherInfo;
import com.sinosoft.ops.cimp.entity.sheet.Sheet;
import com.sinosoft.ops.cimp.entity.sheet.SheetGatherParameter;
import com.sinosoft.ops.cimp.repository.sheet.SheetDao;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 表格数据访问接口实现
 *
 * @author wangft
 *
 */
@Repository("sheetDao")
public class SheetDaoImpl extends BaseEntityDaoImpl<Sheet> implements SheetDao {

	public SheetDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Sheet> getByDesignId(UUID designId, Map<String, Object> params) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Sheet.class);
		criteria.add(Restrictions.eq("designId", designId));
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			criteria.add(Restrictions.eq(entry.getKey(), entry.getValue()));
		}
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Sheet> getByReportNo(String sheetNo, int version, Map<String, Object> params) {
		StringBuilder hqlBuilder = new StringBuilder("from Sheet a , SheetDesign d where a.designId=d.id");
		hqlBuilder.append(" and ( d.sheetNo=:sheetNo and d.version=:version )");
		hqlBuilder.append(" and exists ( select 1 from SheetParameterBind b where a.id = b.sheetId ");
		for (int i = 0; i < params.size(); i++) {
			hqlBuilder.append(" and ( b.parameterName=:parameterName");
			hqlBuilder.append(i);
			hqlBuilder.append(" and b.parameterValue=:parameterValue");
			hqlBuilder.append(i);
			hqlBuilder.append(" )");
		}
		hqlBuilder.append(" )");
		Query query = sessionFactory.getCurrentSession().createQuery(hqlBuilder.toString());
		int i = 0;
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			query.setParameter("parameterName" + i, entry.getKey());
			query.setParameter("parameterValue" + i, entry.getValue());
			i++;
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Sheet> getByCategoryId(UUID categoryId) {
		return sessionFactory.getCurrentSession().createQuery("from Sheet d join SheetSheetCategory c on d.id=c.sheetId where d.status=0 and c.categoryId = :categoryId")
				.setParameter("categoryId", categoryId)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getOnlyByCategoryId(UUID categoryId) {
		List<Object> ids=sessionFactory.getCurrentSession().createQuery("select d.id from Sheet d join SheetSheetCategory c on d.id=c.sheetId where d.status=0 and c.categoryId = :categoryId")
				.setParameter("categoryId", categoryId).setMaxResults(1).list();
		if(ids.size()>0)
		{
			return ids.get(0);
		}
		else
		{
			return null;
		}
	}

	@Override
	public PageableQueryResult findByPage(PageableQueryParameter queryParameter) {
		PageableQueryResult result = new PageableQueryResult(queryParameter.getPageNo(),queryParameter.getPageSize());
		StringBuilder sb = new StringBuilder(" from Sheet d join Sheet_Sheet_Category c on d.id=c.sheet_Id left join Sheet_Design b on d.design_Id=b.id left join Sheet_Parameter p on c.sheet_Id=p.sheet_Id where (1=1)");
		if(queryParameter!=null){
			String conStr = "";
			Map<String, Object> pramsMap = queryParameter.getParameters();
			Set pramsKeys = pramsMap.keySet();
			for (Object aKey : pramsKeys) {
				if ("categoryId".equals(aKey.toString())) {
					Object value = queryParameter.getParameters().get(aKey.toString());
					if (value != null && value instanceof Collection) {
						conStr += " and c." + "category_Id" + " in (:" + aKey.toString() + ")";
					} else {
						conStr += " and c." + "category_Id" + "=:" + aKey.toString();
					}
				} else if ("type".equals(aKey.toString())) {
					conStr += " and b." + aKey.toString() + " in (:" + aKey.toString() + ")";
				} else if ("name".equals(aKey.toString())) {
					conStr += " and d." + aKey.toString() + " LIKE :" + aKey.toString();
				} else if ("designName".equals(aKey.toString())) {
					conStr += " and b.name LIKE :" + aKey.toString();
				} else if ("releaseComName".equals(aKey.toString())) {
					conStr += " and b." + "release_Com_Name" + " LIKE :" + aKey.toString();
				} else if ("dataFillType".equals(aKey.toString())) {
					conStr += " and b." + "data_Fill_Type" + "=:" + aKey.toString();
				} else if ("hasData".equals(aKey.toString())) {
					conStr += " and ((1=:" + aKey.toString() + " and exists (select 1 from SheetData a where a.sheetId=d.id and a.status=0))" +
							" or (0=:" + aKey.toString() + " and not exists (select 1 from SheetData a where a.sheetId=d.id and a.status=0)))";
				} else if("parameterId".equals(aKey.toString())){
					conStr += " and p.parameter_id=:"+aKey.toString();
				}else if("depIds".equals(aKey.toString())){
					if((byte)1==(byte)pramsMap.get("parameterId")){
						conStr += " and exists (select 1 from dep_b001 e where e.dep_id in (:depIds) and p.parameter_value like e.TREE_LEVEL_CODE||'%' or p.parameter_value is null) ";
					}else{
						conStr += " and exists (select 1 from party_d001 e where e.party_id in (:depIds) and p.parameter_value like e.TREE_LEVEL_CODE||'%' or p.parameter_value is null) ";
					}
				}else {
					conStr += " and d." + aKey.toString() + "=:" + aKey.toString();
				}
			}
			sb.append(conStr).append(" ORDER BY b.sheet_no,c.category_Id,d.ordinal ");
		}
		if(queryParameter.getTotalCount()==-1){
			result.setTotalCount(this.getTotalCountWithSql(new StringBuilder("select count(*) ").append(sb.toString()).toString(), queryParameter.getParameters()));
		}
		String fields = "select d.id aaa,d.name bbb,b.type ccc,b.name ddd,b.data_Fill_Type eee,b.release_Com_Name fff,c.category_Id ggg,d.summary hhh,d.audit_Status iii,d.being_Edited jjj,d.archived jjjj" +
				",(select count(*) from Sheet_Data a where a.sheet_Id=d.id and a.status=0) kkk,b.id lll,p.description mmm,d.inner_Check_Status nnn,d.outer_Check_Status ooo,d.flg";
		result.setData((Collection<?>) this.findByPageWithSql(calculateFirstResult(queryParameter.getPageNo(),queryParameter.getPageSize()), queryParameter.getPageSize(), new StringBuilder(fields).append(sb.toString()).toString(), queryParameter.getParameters()));
		return result;
//    	PageableQueryResult result = new PageableQueryResult(queryParameter.getPageNo(),queryParameter.getPageSize());
//        StringBuilder sb = new StringBuilder("from Sheet d join SheetSheetCategory c on d.id=c.sheetId left join SheetDesign b on d.designId=b.id left join SheetParameter p on c.sheetId=p.sheetId where (p.parameterId='1')");
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
//        			conStr += " and b." + aKey.toString() + " in (:" + aKey.toString() + ")";
//            	} else if ("name".equals(aKey.toString())) {
//            		conStr += " and d." + aKey.toString() + " LIKE :" + aKey.toString();
//            	} else if ("designName".equals(aKey.toString())) {
//            		conStr += " and b.name LIKE :" + aKey.toString();
//            	} else if ("releaseComName".equals(aKey.toString())) {
//            		conStr += " and b." + aKey.toString() + " LIKE :" + aKey.toString();
//            	} else if ("dataFillType".equals(aKey.toString())) {
//                	conStr += " and b." + aKey.toString() + "=:" + aKey.toString();
//            	} else if ("hasData".equals(aKey.toString())) {
//            		conStr += " and ((1=:" + aKey.toString() + " and exists (select 1 from SheetData a where a.sheetId=d.id and a.status=0))" +
//            				" or (0=:" + aKey.toString() + " and not exists (select 1 from SheetData a where a.sheetId=d.id and a.status=0)))";
//            	} else if("releaseComId".equals(aKey.toString())){
//            		conStr += " and (b.releaseComId in :releaseComId or b.releaseComId is null)";
//            	}else {
//                	conStr += " and d." + aKey.toString() + "=:" + aKey.toString();
//            	}
//            }
//            sb.append(conStr).append(" ORDER BY c.categoryId,d.ordinal ");
//        }
//        if(queryParameter.getTotalCount()==-1){
//            result.setTotalCount(this.getTotalCountWithHql(new StringBuilder("select count(*) ").append(sb.toString()).toString(), queryParameter.getParameters()));
//        }
//        String fields = "select d.id,d.name,b.type,b.name,b.dataFillType,b.releaseComName,c.categoryId,d.summary,d.auditStatus,d.beingEdited" +
//        		",(select count(*) from SheetData a where a.sheetId=d.id and a.status=0),b.id,p.description ";
//        result.setData((Collection<?>) this.findByPageWithHql(calculateFirstResult(queryParameter.getPageNo(),queryParameter.getPageSize()), queryParameter.getPageSize(), new StringBuilder(fields).append(sb.toString()).toString(), queryParameter.getParameters()));
//        return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	public long getTotalCountWithHql(String totalCountHql, Map<String, Object> params) {
		Query<?> query = sessionFactory.getCurrentSession().createQuery(totalCountHql);
		if(params!=null&&!params.isEmpty()){
			prepareQueryParam(query,params);
		}
		return ((Long)query.uniqueResult()).longValue();
	}

	@SuppressWarnings("deprecation")
	@Override
	public Collection<?> findByPageWithHql(int firstResult, int maxResults, String hql, Map<String, Object> params) {
		Query<?> query = sessionFactory.getCurrentSession().createQuery(hql);
		if(params!=null&&!params.isEmpty()){
			prepareQueryParam(query,params);
		}
		List list = query.setFirstResult(firstResult)
				.setMaxResults(maxResults)
				.list();
		List resultList = new ArrayList<Map<String, Object>>();
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				Object[] objs = (Object[]) obj;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", (UUID)objs[0]);
				map.put("name", (String)objs[1]);
				map.put("type", (Byte)objs[2]);
				map.put("designName", (String)objs[3]);
				map.put("dataFillType", (Byte)objs[4]);
				map.put("releaseComName", (String)objs[5]);
				map.put("categoryId", (UUID)objs[6]);
				map.put("summary", (Boolean)objs[7]);
				map.put("auditStatus", (Byte)objs[8]);
				map.put("beingEdited", (Boolean)objs[9]);
				map.put("dataCount", (Long)objs[10]);
				map.put("designId", (UUID)objs[11]);
				map.put("fillCom", (String)objs[12]);
				resultList.add(map);
			}
		}
		return resultList;
	}

	/**
	 * 为查询对象准备查询参数
	 * @param query 查询对象
	 * @param params 参数集
	 */
	@SuppressWarnings("deprecation")
	protected void prepareQueryParam(Query<?> query,final Map<String,Object> params){
		for (String key : params.keySet()) {
			Object value = params.get(key);
			if("name".equals(key)){
				query.setParameter(key, "%" + value + "%");
			} else if("designName".equals(key)){
				query.setParameter(key, "%" + value + "%");
			} else if("releaseComName".equals(key)){
				query.setParameter(key, "%" + value + "%");
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
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		String hql = "UPDATE Sheet SET ordinal=ordinal+1, lastModifiedBy=:userName, lastModifiedTime=SYSDATE " +
				"WHERE id IN (SELECT sheetId from SheetSheetCategory WHERE categoryId=:categoryId) AND ordinal IS NOT NULL AND ordinal>:upOrdinal";
		int cnt = session.createQuery(hql)
				.setParameter("categoryId", categoryId)
				.setParameter("upOrdinal", upOrdinal)
				.setParameter("userName", userName)
				.executeUpdate();
		session.flush();
		session.getTransaction().commit();
		return cnt > 0 ? true : false;
	}

	@Override
	public int getMaxOrdinal(UUID categoryId) {
		String hql = "FROM Sheet WHERE id IN (SELECT sheetId from SheetSheetCategory WHERE categoryId=:categoryId)";
		@SuppressWarnings("unchecked")
		Collection<Sheet> coll = sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("categoryId", categoryId)
				.list();
		int max = 0;
		if (coll != null && coll.size() > 0) {
			for (Sheet item : coll) {
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
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		String hql = "UPDATE Sheet SET ordinal=ordinal-1, lastModifiedBy=:userName, lastModifiedTime=sysdate " +
				"WHERE id IN (SELECT sheetId from SheetSheetCategory WHERE categoryId=:categoryId) AND ordinal IS NOT NULL AND ordinal>:thisOrdinal";
		int cnt = session.createQuery(hql)
				.setParameter("categoryId", categoryId)
				.setParameter("thisOrdinal", thisOrdinal)
				.setParameter("userName", userName)
				.executeUpdate();
		session.flush();
		session.getTransaction().commit();
		return cnt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Sheet findPrevious(UUID id, UUID categoryId) {
		String hql = "FROM Sheet T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM Sheet T3 " +
				"WHERE T3.status=0 AND T3.id IN (SELECT T4.sheetId FROM SheetSheetCategory T4 WHERE T4.categoryId=:categoryId) AND T3.ordinal IS NOT NULL AND " +
				"T3.ordinal IN (SELECT MAX(T5.ordinal) FROM Sheet T5 WHERE T5.status=0 AND T5.id IN " +
				"(SELECT T7.sheetId FROM SheetSheetCategory T7 WHERE categoryId=:categoryId) AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal<(SELECT T6.ordinal FROM Sheet T6 WHERE T6.id=:id)))";
		List<Sheet> list = sessionFactory.getCurrentSession().createQuery(hql)
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
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		String hql = "UPDATE Sheet SET ordinal=:newOrdinal, lastModifiedBy=:userName, lastModifiedTime=sysdate WHERE id=:id";
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
	public Sheet findNext(UUID id, UUID categoryId) {
		String hql = "FROM Sheet T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM Sheet T3 " +
				"WHERE T3.status=0 AND T3.id IN (SELECT T4.sheetId FROM SheetSheetCategory T4 WHERE T4.categoryId=:categoryId) AND T3.ordinal IS NOT NULL AND " +
				"T3.ordinal IN (SELECT MIN(T5.ordinal) FROM Sheet T5 WHERE T5.status=0 AND T5.id IN " +
				"(SELECT T7.sheetId FROM SheetSheetCategory T7 WHERE categoryId=:categoryId) AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal>(SELECT T6.ordinal FROM Sheet T6 WHERE T6.id=:id)))";
		List<Sheet> list = sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("id", id)
				.setParameter("categoryId", categoryId)
				.list();
		if (list.size() > 0) {
			return list.get(list.size() - 1);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map> getSummaryData(Map queryParameter) {
		List<String> lstDesignIds= (List)queryParameter.get("design_id");
		List<String> lstCategoryIds=(List)queryParameter.get("category_id");
		List<String> ids=(List)queryParameter.get("id");
		String containSelf=queryParameter.get("containSelf").toString();
		List<Map> lst = new ArrayList<>();
		if(containSelf.equals("1"))
		{
			lst = sessionFactory.getCurrentSession().createSQLQuery("select a.design_id,b.type, b.name, num, ids\n" +
					"  from (select design_id, count(1) num,  LISTAGG(TO_CHAR(RAWTONHEX(sheet.id)),',') WITHIN GROUP (ORDER BY SHEET.ORDINAL) as ids\n" +
					"          from sheet\n" +
					"          left join sheet_sheet_category\n" +
					"            on sheet.id = sheet_sheet_category.sheet_id\n" +
					"         where sheet.flg=5 and design_id in (:design_id) \n" +
					"         and sheet_sheet_category.category_id in (:category_id)\n" +
					"         group by design_id) a\n" +
					"  left join sheet_design b\n" +
					"    on a.design_id = b.id\n")
					.setParameterList("design_id", lstDesignIds)
					.setParameterList("category_id", lstCategoryIds)
					.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
		}
		else
		{
			lst = sessionFactory.getCurrentSession().createSQLQuery("select a.design_id,b.type, b.name, num, ids\n" +
					"  from (select design_id, count(1) num,  LISTAGG(TO_CHAR(RAWTONHEX(sheet.id)),',') WITHIN GROUP (ORDER BY SHEET.ORDINAL) as ids\n" +
					"          from sheet\n" +
					"          left join sheet_sheet_category\n" +
					"            on sheet.id = sheet_sheet_category.sheet_id\n" +
					"         where sheet.flg=5 and design_id in (:design_id) and sheet.id not in (:id)\n" +
					"         and sheet_sheet_category.category_id in (:category_id)\n" +
					"         group by design_id) a\n" +
					"  left join sheet_design b\n" +
					"    on a.design_id = b.id\n")
					.setParameter("id", ids)
					.setParameterList("design_id", lstDesignIds)
					.setParameterList("category_id", lstCategoryIds)
					.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
		}

		for(Map map:lst)
		{
			map.put("DESIGN_ID",toUUIDStringByRaw(map.get("DESIGN_ID")));
		}

		return  lst;
	}

	@Override
	public Sheet calculate(String sheetNo, int version, Map<String, Object> params, UUID userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sheet calculate(String sheetNo, Map<String, Object> params, UUID userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sheet sum(String sheetNo, int version, Map<String, Object> params, UUID userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sheet sum(String sheetNo, Map<String, Object> params, UUID userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean check(int reportNo, int version, Map<String, Object> params, UUID userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean check(int reportNo, Map<String, Object> params, UUID userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean check(String id, UUID userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<GatherInfo> getGatherInfos(UUID id, Integer rowNo, Integer columnNo) {
		return sessionFactory.getCurrentSession().createSQLQuery("select sgi.gather_sheet_id gatherSheetId,sc.name categoryName,s.name sheetName,sd.string_value stringValue from sheet_gather_info sgi,sheet_sheet_category ssc,sheet_category sc,sheet_data sd,sheet s where "
				+ " sgi.gather_sheet_id = ssc.sheet_id and ssc.category_id=sc.id and sgi.gather_sheet_id=sd.sheet_id and sgi.gather_sheet_id=s.id"
				+ " and sgi.sheet_id=:id and sd.row_no = :rowNo and sd.column_no = :columnNo")
				.addEntity(GatherInfo.class)
				.setParameter("id", id)
				.setParameter("rowNo", rowNo)
				.setParameter("columnNo", columnNo)
				.list();
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public Collection<Map<String, Object>> getParameterInfoByDesignId(UUID designId) {
		return sessionFactory.getCurrentSession().createSQLQuery("select p.name name,p.name_cn nameCN from sheet_design_parameter s,sys_parameter p where s.parameter_id=p.id and s.design_id=:designId")
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setParameter("designId", designId)
				.list();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public Collection<SheetGatherParameter> getGatherParameterValue(UUID sheetId) {
		return sessionFactory.getCurrentSession().createSQLQuery("select sp.name name,p.description description,s.gather_sheet_id gatherSheetId,rownum rowNo from sys_parameter sp,sheet_parameter p,sheet_gather_info s where sp.id=p.parameter_id and p.sheet_id=s.gather_sheet_id and s.sheet_id=:sheetId")
				.addEntity(SheetGatherParameter.class)
				.setParameter("sheetId", sheetId)
				.list();
//		return sessionFactory.getCurrentSession().createSQLQuery("select s.sheet_id sheetId,p.id pid,s.gather_sheet_id gatherSheetId from sys_parameter sp,sheet_parameter p,sheet_gather_info s where sp.id=p.parameter_id and p.sheet_id=s.gather_sheet_id and s.sheet_id=:sheetId")
//				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
//				.setParameter("sheetId", sheetId)
//				.list();
//		Collection<SheetGatherParameter> s = sessionFactory.getCurrentSession().createSQLQuery("select s.sheet_id sheetId,p.id pid,s.gather_sheet_id gatherSheetId from sys_parameter sp,sheet_parameter p,sheet_gather_info s where sp.id=p.parameter_id and p.sheet_id=s.gather_sheet_id and s.sheet_id=:sheetId")
//				.addEntity(SheetGatherParameter.class)
//				.setParameter("sheetId", sheetId)
//				.list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Sheet> getByDesignId(UUID designId) {
		return sessionFactory.getCurrentSession().createQuery("from Sheet where designId=:designId")
				.setParameter("designId", designId)
				.list();

	}

	@Override
	public List<Map> getRefSituation(String id) {
		List<Map> list= sessionFactory.getCurrentSession().createSQLQuery(" select a.name,a.DESCRIPTION,a.id SHEETID from sheet a\n" +
				"inner join sheet_gather_info b on a.id=b.SHEET_ID\n" +
				"where b.gather_sheet_id=:sheetId").setParameter("sheetId", UUID.fromString(id)).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();

		for (Map map : list) {
			map.put("SHEETID",toUUIDStringByRaw(map.get("SHEETID")));
		}
		return list;
	}

	@SuppressWarnings("deprecation")
	@Override
	public long getTotalCountWithSql(final String totalCountSql,final Map<String,Object> params){
		Query<?> query = sessionFactory.getCurrentSession().createNativeQuery(totalCountSql);
		if(params!=null&&!params.isEmpty()){
			prepareQueryParam(query,params);
		}
		return ((BigDecimal)query.uniqueResult()).longValue();
	}

	@SuppressWarnings("deprecation")
	@Override
	public Collection<?> findByPageWithSql(int firstResult, int maxResults, String hql, Map<String, Object> params) {
		Query<?> query = sessionFactory.getCurrentSession().createNativeQuery(hql);
		if(params!=null&&!params.isEmpty()){
			prepareQueryParam(query,params);
		}
		List list = query.setFirstResult(firstResult)
				.setMaxResults(maxResults)
				.list();
		List resultList = new ArrayList<Map<String, Object>>();
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				Object[] objs = (Object[]) obj;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", toUUIDStringByRaw(objs[0]));
				map.put("name", (String)objs[1]);
				map.put("type", objs[2]!=null?((BigDecimal)objs[2]).byteValue():null);
				map.put("designName", (String)objs[3]);
				map.put("dataFillType", objs[4]!=null?((BigDecimal)objs[4]).byteValue():null);
				map.put("releaseComName", (String)objs[5]);
				map.put("categoryId", toUUIDStringByRaw(objs[6]));
				map.put("summary", objs[7]!=null?((BigDecimal)objs[7]).intValue()==1?true:false:null);
				map.put("auditStatus", objs[8]!=null?((BigDecimal)objs[8]).byteValue():null);
				map.put("beingEdited", objs[9]!=null?((BigDecimal)objs[9]).intValue()==1?true:false:null);
				map.put("archived", objs[10]!=null?((BigDecimal)objs[10]).intValue()==1?true:false:null);
				map.put("haseData", objs[11]!=null?((BigDecimal)objs[11]).longValue()>0?true:false:null);
				map.put("designId", toUUIDStringByRaw(objs[12]));
				map.put("fillCom", (String)objs[13]);
				map.put("innerCheckStatus", objs[14]!=null?((BigDecimal)objs[14]).intValue()==1?true:false:null);
				map.put("outerCheckStatus", objs[15]!=null?((BigDecimal)objs[15]).intValue()==1?true:false:null);
				map.put("flg",objs[16]!=null?(BigDecimal)objs[16]:0);
				resultList.add(map);
			}
		}
		return resultList;
	}

	@Override
	public int checkLevel(String loginName,String sheetId,String type)
	{
		if(type.equals("1"))
		{
			List<String> lstOrg = sessionFactory.getCurrentSession().createNativeQuery("select B001001_A from sys_user\n" +
					"left join sys_user_org_un on sys_user.id=sys_user_org_un.user_id\n" +
					"left join dep_b001 on dep_b001.dep_id=sys_user_org_un.org_id\n" +
					"where login_name=:userName")
					.setParameter("userName", loginName)
					.list();

			String designDep =(String) sessionFactory.getCurrentSession().createNativeQuery("select PARAMETER_VALUE from sheet_parameter where PARAMETER_ID=1 and SHEET_ID=:sheetId")
					.setParameter("sheetId", UUID.fromString(sheetId))
					.uniqueResult();

			for(String org :lstOrg)
			{
				if(org.equals(designDep))
				{
					return 1;
				}
				if(designDep.contains(org))
				{
					return 2;
				}
			}
		}
		else
		{
			List<String> lstOrg = sessionFactory.getCurrentSession().createNativeQuery("select party_d001.D001001_A from sys_user\n" +
					"left join sys_user_org_up on sys_user.id=sys_user_org_up.user_id\n" +
					"left join party_d001 on party_d001.party_id=sys_user_org_up.org_id\n" +
					"where login_name=:userName")
					.setParameter("userName", loginName)
					.list();

			String designDep =(String) sessionFactory.getCurrentSession().createNativeQuery("select PARAMETER_VALUE from sheet_parameter where PARAMETER_ID=2 and SHEET_ID=:sheetId")
					.setParameter("sheetId", UUID.fromString(sheetId))
					.uniqueResult();

			for(String org :lstOrg)
			{
				if(org.equals(designDep))
				{
					return 1;
				}
				if(designDep.contains(org))
				{
					return 2;
				}
			}
		}
		return 3;
	}

	@Override
	public String getAncestorOrganizationName(String myId, String ancestorId) {
		StringBuilder sb=new StringBuilder("select '【'||b001001_a||'】'||b001001_b from (");
		sb.append("  select dep_id,b001001_a,b001001_b from DEP_B001 start with dep_id=:myId");
		sb.append("  connect by prior pptr = b001001_a");
		sb.append(" ) p where p.dep_id=:ancestorId");
		return (String) sessionFactory.getCurrentSession().createNativeQuery(sb.toString())
				.setParameter("myId", myId)
				.setParameter("ancestorId", ancestorId)
				.uniqueResult();
	}

	@Override
	public int archiveById(UUID categoryId, UUID designId, UUID userId, String organizationId) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		StringBuilder sb=new StringBuilder();
		sb.append("update SHEET set ARCHIVED=1,ARCHIVED_BY=:userId,ARCHIVED_ORGANIZATION_ID=:organizationId where ID in (");
		sb.append(" select SHEET_ID from SHEET_SHEET_CATEGORY where CATEGORY_ID in (");
		sb.append(" select ID from SHEET_CATEGORY start with ID=:categoryId");
		sb.append(" connect by prior ID=PARENT_ID )");
		sb.append(" ) and DESIGN_ID=:designId");
		int result = session.createNativeQuery(sb.toString())
				.setParameter("categoryId", categoryId)
				.setParameter("designId", designId)
				.setParameter("userId", userId)
				.setParameter("organizationId", organizationId)
				.executeUpdate();
		session.flush();
		session.getTransaction().commit();
		return result;
	}

	@Override
	public int unarchiveById(UUID categoryId, UUID designId, UUID userId) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		StringBuilder sb=new StringBuilder();
		sb.append("update SHEET set ARCHIVED=0,ARCHIVED_BY=:userId,ARCHIVED_ORGANIZATION_ID=NULL where ID in (");
		sb.append(" select SHEET_ID from SHEET_SHEET_CATEGORY where CATEGORY_ID in (");
		sb.append(" select ID from SHEET_CATEGORY start with ID=:categoryId");
		sb.append(" connect by prior ID=PARENT_ID )");
		sb.append(" ) and DESIGN_ID=:designId");
		int result = session.createNativeQuery(sb.toString())
				.setParameter("categoryId", categoryId)
				.setParameter("designId", designId)
				.setParameter("userId", userId)
				.executeUpdate();
		session.flush();
		session.getTransaction().commit();
		return result;
	}
}
