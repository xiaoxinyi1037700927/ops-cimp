package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDataSource;
import com.sinosoft.ops.cimp.repository.sheet.SheetDataSourceDao;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName:  SheetDataSourceDaoImpl
 * @description: 表设计数据源访问接口实现类
 * @author:        kanglin
 * @date:            2018年6月5日 下午
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository("SheetDataSourceDao")
public class SheetDataSourceDaoImpl extends BaseEntityDaoImpl<SheetDataSource> implements SheetDataSourceDao {

	public SheetDataSourceDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@Override
	public SheetDataSource getById(UUID id) {

		return super.getById(id);
	}

	@Override
	public UUID save(SheetDataSource SheetDataSource) {

		return (UUID)super.save(SheetDataSource);
	}

	@Override
	public void update(SheetDataSource SheetDataSource, UUID id) {

		super.update(SheetDataSource);
	}

	@Override
	public void delete(UUID id) {
		super.deleteById(id);
	}

	//根据分类id获取list
	public Collection<SheetDataSource> getByCategoryId(int categoryId){
		return sessionFactory.getCurrentSession().createQuery(" from SheetDataSource where categoryId =:categoryId").setParameter("categoryId",categoryId).list();
	}

	@Override
	public List<Map> getRefSituation(String id) {
		List<Map> list= sessionFactory.getCurrentSession().createSQLQuery("   select DATASOURCE_ID, b.id AS design_id, b.NAME,b.DESCRIPTION, SHEET_NO,d.name category_name\n" +
				"  from sheet_design_data_source a\n" +
				" inner join sheet_design b\n" +
				"    on a.design_id = b.id\n" +
				"inner join sheet_design_design_category c on b.id=c.design_id\n" +
				"inner join sheet_design_category d on c.category_id=d.id\n" +
				" where a.DATASOURCE_ID = :dataSourceId").setParameter("dataSourceId", UUID.fromString(id)).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();

		for (Map map : list) {
			map.put("DATASOURCE_ID",toUUIDStringByRaw(map.get("DATASOURCE_ID")));
			map.put("DESIGN_ID",toUUIDStringByRaw(map.get("DESIGN_ID")));
		}
		return list;
	}

	@Override
	public List<Map> getRefnum(Integer categoryid) {
		List<Map> list= sessionFactory.getCurrentSession().createSQLQuery("               select a.id,refnum\n" +
				"                      from sheet_data_source a\n" +
				"                      left join (select datasource_id, count(1) refnum\n" +
				"                                   from sheet_design_data_source\n" +
				"                                  group by datasource_id) temp\n" +
				"                        on a.id = temp.datasource_id\n" +
				"                        where  a.category_id=:categoryid").setParameter("categoryid", categoryid).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();

		for (Map map : list) {
			map.put("ID",toUUIDStringByRaw(map.get("ID")));
		}
		return list;
	}

}
