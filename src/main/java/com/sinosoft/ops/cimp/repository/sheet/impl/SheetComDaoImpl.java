package com.sinosoft.ops.cimp.repository.sheet.impl;


import com.sinosoft.ops.cimp.common.dao.BaseDaoImpl;
import com.sinosoft.ops.cimp.common.model.PageableQueryParameter;
import com.sinosoft.ops.cimp.common.model.PageableQueryResult;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSet;
import com.sinosoft.ops.cimp.entity.sheet.SheetInfo;
import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeItem;
import com.sinosoft.ops.cimp.repository.sheet.SheetComDao;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @ClassName:  SheetComDaoImpl
 * @description: Shee 通用数据访问实现类
 * @author:        sunch
 * @date:            2018年6月7日 下午6:31:40
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository
public class SheetComDaoImpl extends BaseDaoImpl implements SheetComDao {

	public SheetComDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SysCodeItem> getChildCodeItemList(Integer codeSetId, String parentCode){
		return (List<SysCodeItem>) sessionFactory.getCurrentSession().createQuery("from SysCodeItem i where i.codeSetId=:codeSetId and i.parentCode=:parentCode order by ordinal")
			.setParameter("codeSetId", codeSetId)
			.setParameter("parentCode", parentCode)
			.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public Collection<SysInfoSet> getInfoSet() {
		
		return (Collection<SysInfoSet>)sessionFactory
				.getCurrentSession().createQuery("from SysInfoSet order by name")
                .list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<SysInfoItem> getByInfoSetId(Integer infoSetId) {

		return (List<SysInfoItem>)sessionFactory.getCurrentSession().createQuery("from SysInfoItem where inputType!='hidden' and infoSetId = :infoSetId order by ordinal")
                .setParameter("infoSetId", infoSetId)
                .list();
	}
	@Override
	public Object getValueBySql(String sql) {
		return sessionFactory.getCurrentSession().createSQLQuery(sql).uniqueResult();
	}

	@Override
	public List<Map> getListBySql(String sql) {
		return sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	@Override
	public PageableQueryResult getQueryDataByPage(String querySql, PageableQueryParameter queryParameter) {
		PageableQueryResult result = new PageableQueryResult();
		result.setPageNo(queryParameter.getPageNo());
		result.setPageSize(queryParameter.getPageSize());
		result.setTotalCount(getTotalCountWithSql(querySql));
		List list = sessionFactory.getCurrentSession().createSQLQuery(querySql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
			.setFirstResult((queryParameter.getPageNo()-1)*queryParameter.getPageSize())
			.setMaxResults(queryParameter.getPageSize())
			.list();
		result.setData(list);
		return result;
	}
	
	public long getTotalCountWithSql(String querySql){
		return ((BigDecimal) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from ("+querySql+")").uniqueResult()).longValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getQueryData(String querySql) {
		return sessionFactory.getCurrentSession().createSQLQuery(querySql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public Collection<SheetInfo> getSheetInfos(Set<String> sheetNos, String parameterId, String parameterValue) {
		return sessionFactory.getCurrentSession().createSQLQuery("select DISTINCT s.id sheetId,s.design_id designId,sd.SHEET_NO sheetNo from sheet_design sd,sheet s,sheet_parameter sp where SHEET_NO in (:sheetNos) and sd.id=s.DESIGN_ID and  s.id=sp.SHEET_ID and sp.parameter_id=:parameterId and sp.parameter_value=:parameterValue")
					.addEntity(SheetInfo.class)
					.setParameterList("sheetNos", sheetNos)
					.setParameter("parameterId", parameterId)
					.setParameter("parameterValue", parameterValue)
					.list();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Collection<SheetInfo> getSheetInfos(Set<String> sheetNos, Map<String, Object> parameter, String sql) {
		 NativeQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.addEntity(SheetInfo.class)
				.setParameterList("sheetNos", sheetNos);
		 for (String parameterName : parameter.keySet()) {
			 query.setParameter(parameterName, parameter.get(parameterName));
		 }
		 return query.list();
	}

	
}
