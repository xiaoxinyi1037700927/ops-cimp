/**
 * @project:      IIMP
 * @title:          SheetDesignCarrierDaoImpl.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet.impl;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetData;
import com.sinosoft.ops.cimp.repository.sheet.SheetDataDao;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName:  SheetDataDaoImpl
 * @description: 表格设计载体数据访问接口实现
 * @author:        kanglin
 * @date:            2017年8月18日 下午1:05:52
 * @version        1.0.0
 */
@Repository("sheetDataDao")
public class SheetDataDaoImpl extends BaseEntityDaoImpl<SheetData> implements SheetDataDao {

	public SheetDataDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

    @SuppressWarnings("unchecked")
	@Override
    public Collection<SheetData> getBySheetId(UUID sheetId) {
        return sessionFactory.getCurrentSession().createQuery("from SheetData s where s.sheetId = :sheetId")
            .setParameter("sheetId", sheetId)
            .list();
    }


    @SuppressWarnings("unchecked")
    @Override
    public Collection<SheetData> getBySheetId(String sheetId) {
        return sessionFactory.getCurrentSession().createQuery("from SheetData s where s.sheetId = :sheetId")
                .setParameter("sheetId", UUID.fromString(sheetId))
                .list();
    }
    
	@Override
	public void deleteBySheetId(UUID sheetId) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		session.createQuery("delete from SheetData where sheetId=:sheetId").
			setParameter("sheetId", sheetId).executeUpdate();
		session.flush();
		session.getTransaction().commit();
	}


	@SuppressWarnings("unchecked")
	@Override
	public Collection<Map<String, Object>> getBuildWordDatas(UUID sheetId) {
		return sessionFactory.getCurrentSession().createQuery("select new map(d.stringValue as bindValue,s.sectionNo as sectionNo,s.name as name) from SheetData as d join SheetDesignSection as s on d.sectionNo=s.sectionNo "
				+ "join SheetDesign as sd on s.designId=sd.id join Sheet as st on st.id=d.sheetId and st.designId=sd.id and st.id=d.sheetId where d.sheetId=:sheetId")
				.setParameter("sheetId",sheetId)
				.list();
	}
}
