package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignVariable;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignVariableDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.UUID;



/**
 * @ClassName:  SheetDesignVariableDaoImpl
 * @description: 表设计变量访问接口实现类
 * @author:        lixianfu
 * @date:            2018年6月4日 下午1:21:30
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository("SheetDesignVariableDao")
public class SheetDesignVariableDaoImpl extends BaseEntityDaoImpl<SheetDesignVariable> implements SheetDesignVariableDao {

	public SheetDesignVariableDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@Override
	public SheetDesignVariable getById(UUID id) {
		
		return super.getById(id);
	}

	@Override
	public UUID saveVariable(SheetDesignVariable SheetDesignVariable) {
		
		return (UUID)save(SheetDesignVariable);
	}

	@Override
	public void updateVariable(SheetDesignVariable SheetDesignVariable, UUID id) {
		
		update(SheetDesignVariable);
	}

	@Override
	public void deleteVariable(UUID id) {
		deleteById(id);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SheetDesignVariable> getByDesignId(UUID designId) {
		return sessionFactory.getCurrentSession().createQuery(" from SheetDesignVariable where  designId= :designId")
				.setParameter("designId", designId)
				.list();
	}

}
