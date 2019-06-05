package com.sinosoft.ops.cimp.repository.sheet;

import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName:  SheetDesignBindDao
 * @description: 表数据绑定接口
 * @author:        kanglin
 * @date:            2018年6月12日 下午2:35:00
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetDesignBindDao {
	
	int updateDesignBind(String sql);
	
	boolean hasDatas(String sql);
	
	int dataCnt(String sql);
	
	List<Map<String, Object>> findBySQL(String sql);
}
