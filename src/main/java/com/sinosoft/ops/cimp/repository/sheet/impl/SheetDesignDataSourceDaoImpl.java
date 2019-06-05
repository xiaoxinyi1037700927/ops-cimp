package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignDataSource;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignDataSourceDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


/**
 * @ClassName:  SheetDesignDataSourceDaoImpl
 * @description: 表设计数据源访问接口实现类
 * @author:        kanglin
 * @date:            2018年6月5日 下午
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository("sheetDesignDataSourceDao")
public class SheetDesignDataSourceDaoImpl extends BaseEntityDaoImpl<SheetDesignDataSource> implements SheetDesignDataSourceDao {

	public SheetDesignDataSourceDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@Override
	public SheetDesignDataSource getById(UUID id) {
		return (SheetDesignDataSource) sessionFactory.getCurrentSession().createQuery("select new SheetDesignDataSource(s.id,s.designId,s.datasourceId,s.ordinal,s.status,s.createdTime,s.createdBy,s.lastModifiedTime,s.lastModifiedBy,"
				+ "d.sql,d.dataRange,d.name,s.startRowNo,s.endRowNo,s.startColumnNo,s.endColumnNo,s.sectionNo)" 
				+ " from SheetDesignDataSource s,SheetDataSource d where s.datasourceId=d.id and s.designId = :designId")
	            .setParameter("id", id)
	            .uniqueResult();
	}

	@Override
	public UUID save(SheetDesignDataSource SheetDesignDataSource) {
		
		return (UUID)super.save(SheetDesignDataSource);
	}

	@Override
	public void update(SheetDesignDataSource SheetDesignDataSource, UUID id) {
		
		super.update(SheetDesignDataSource);
	}

	@Override
	public void delete(UUID id) {
		super.deleteById(id);
	}

	@Override
	public Integer getMaxOrdinal() {
		String hql = "select coalesce(max(ordinal),0) from SheetDesignDataSource";
		List list = sessionFactory.getCurrentSession().createQuery(hql).getResultList();
		return (Integer) list.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<SheetDesignDataSource> getByDesignId(UUID designId) {
		return sessionFactory.getCurrentSession().createQuery("select new SheetDesignDataSource(s.id,s.designId,s.datasourceId,s.ordinal,s.status,s.createdTime,s.createdBy,s.lastModifiedTime,s.lastModifiedBy,"
				+ "d.sql,d.dataRange,d.name,s.startRowNo,s.endRowNo,s.startColumnNo,s.endColumnNo,d.categoryId,s.sectionNo)"
				+ " from SheetDesignDataSource s,SheetDataSource d where s.datasourceId=d.id and s.designId = :designId")
	            .setParameter("designId", designId)
	            .list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public SheetDesignDataSource getBingData(UUID designId,String sectionNo) {
		return (SheetDesignDataSource) sessionFactory.getCurrentSession().createQuery("from SheetDesignDataSource where designId = :designId and sectionNo=:sectionNo")
				.setParameter("designId", designId)
				.setParameter("sectionNo", sectionNo).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<SheetDesignDataSource> getByDesignIdDistinct(UUID designId) {
		return sessionFactory.getCurrentSession().createQuery("select distinct new SheetDesignDataSource( s.designId,s.datasourceId,"
				+ "d.dataRange,d.name,d.categoryId)"
				+ " from SheetDesignDataSource s,SheetDataSource d where s.datasourceId=d.id and s.designId = :designId")
	            .setParameter("designId", designId)
	            .list();
	}

	@Override
	public int deleteByDesignIdAndDataSourceId(UUID designId,UUID datasourceId) {
		return sessionFactory.getCurrentSession().createQuery("delete from SheetDesignDataSource where designId=:designId and datasourceId=:datasourceId")
			.setParameter("datasourceId", datasourceId)
			.setParameter("designId", designId)
			.executeUpdate();
	}
	
	@Override
	public int deleteByDesignId(UUID designId) {
		return sessionFactory.getCurrentSession().createQuery("delete from SheetDesignDataSource where designId=:designId")
			.setParameter("designId", designId)
			.executeUpdate();
	}
		
}
