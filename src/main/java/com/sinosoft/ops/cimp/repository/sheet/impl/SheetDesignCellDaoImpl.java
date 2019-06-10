/**
 * @Project:      IIMP
 * @Title:          SheetDesignCellDaoImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCell;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignCellDao;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignCellDaoImpl
 * @description: 表格设计单元格数据访问实现类
 * @author:        Ni
 * @date:            2018年5月25日 下午3:10:44
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository("sheetDesignCellDao")
public class SheetDesignCellDaoImpl  extends BaseEntityDaoImpl<SheetDesignCell> implements SheetDesignCellDao {

	public SheetDesignCellDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@SuppressWarnings("unchecked")//方法不建议使用
	@Override
	public Collection<SheetDesignCell> getByDesignId(UUID designId) {
		return sessionFactory.getCurrentSession().createQuery("from SheetDesignCell where designId = :designId")
				.setParameter("designId", designId)
				.list();
	}

	@Override
	public int deleteById(UUID Id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		int result = session.createQuery("delete from SheetDesignCell where designId=:designId")
				.setParameter("designId", Id)
				.executeUpdate();
		session.flush();
		session.getTransaction().commit();
		return result;
	}

	@Override
	public UUID saveDesignCell(SheetDesignCell sheetDesignCell) {

		return (UUID)save(sheetDesignCell);//直接强转
	}

	@Override
	public void updateDesignCell(SheetDesignCell sheetDesignCell) {

		update(sheetDesignCell);
	}

	@Override
	public SheetDesignCell getById(UUID Id) {

		return super.getById(Id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<SheetDesignCell> getByPosition(UUID designId, Integer rowNo, Integer columnNo) {
		return sessionFactory.getCurrentSession().createQuery("from SheetDesignCell where designId=:designId and rowNo=:rowNo and columnNo=:columnNo")
				.setParameter("designId", designId)
				.setParameter("rowNo", rowNo)
				.setParameter("columnNo", columnNo)
				.list();
	}

}
