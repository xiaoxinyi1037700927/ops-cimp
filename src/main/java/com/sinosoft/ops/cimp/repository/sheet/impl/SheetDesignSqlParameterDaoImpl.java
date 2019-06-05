package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignSqlParameter;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignSqlParameterDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.UUID;



/**
 * @ClassName:  SheetDesignSqlParameterDaoImpl
 * @description: 表设计SQL参数访问接口实现类
 * @author:        kanglin
 * @date:            2018年6月6日 下午
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository("SheetDesignSqlParameterDao")
public class SheetDesignSqlParameterDaoImpl extends BaseEntityDaoImpl<SheetDesignSqlParameter> implements SheetDesignSqlParameterDao {

	public SheetDesignSqlParameterDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@Override
	public SheetDesignSqlParameter getById(UUID id) {
		
		return super.getById(id);
	}

	@Override
	public UUID save(SheetDesignSqlParameter SheetDesignSqlParameter) {
		
		return (UUID)super.save(SheetDesignSqlParameter);
	}

	@Override
	public void update(SheetDesignSqlParameter SheetDesignSqlParameter, UUID id) {
		
		super.update(SheetDesignSqlParameter);
	}

	@Override
	public void delete(UUID id) {
		super.deleteById(id);
	}
		
}
