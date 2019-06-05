package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignTag;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignTagDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.UUID;


/**
 * @ClassName:  SheetDesignTagDaoImpl
 * @description: 表格设计标签数据接口实现类
 * @author:        lixianfu
 * @date:            2018年6月6日 下午3:03:52
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository("sheetDesignTagDao")
public class SheetDesignTagDaoImpl extends BaseEntityDaoImpl<SheetDesignTag> implements SheetDesignTagDao {

	public SheetDesignTagDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@Override
	public UUID saveDesignTag(SheetDesignTag sheetDesignTag) {
		System.out.println(sheetDesignTag.getName());
		return (UUID)save(sheetDesignTag);
	}

	@Override
	public SheetDesignTag getById(UUID id) {

		return super.getById(id);
	}

	@Override
	public void updateDesignTag(SheetDesignTag sheetDesignTag) {
		
		update(sheetDesignTag);
		
	}

	@Override
	public void deleteDesignTag(UUID id) {
		
		deleteById(id);
		
	}

}
