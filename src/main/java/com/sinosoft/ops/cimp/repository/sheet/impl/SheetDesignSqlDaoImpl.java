package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignSql;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignSqlDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;



/**
 * 
 * @ClassName:  SheetDesignSqlDaoImpl
 * @description:  表格设计SQL 实现类
 * @author:        sunch
 * @date:            2018年6月6日 下午1:43:31
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository
public class SheetDesignSqlDaoImpl extends BaseEntityDaoImpl<SheetDesignSql> implements SheetDesignSqlDao {

	public SheetDesignSqlDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@Override
	public Integer getMaxOrdinal() {
		String hql = "select coalesce(max(ordinal),0) from SheetDesignSql";
		List list = sessionFactory.getCurrentSession().createQuery(hql).getResultList();
		return (Integer) list.get(0);
	}

}
