package com.sinosoft.ops.cimp.service.sheet;

import com.sinosoft.ops.cimp.repository.sheet.impl.SheetDesignBind;

import java.util.List;
import java.util.Map;



/**
 * 
 * @ClassName:  SheetDesignBindService
 * @description: 绑定表数据行列
 * @author:        kanglin
 * @date:            2018年6月12日 下午2:13:10
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetDesignBindService {

	/**
	 * 
	 * 绑定一条表数据行列
	 * @param sheetDesignBind
	 * @return
	 * @author kanglin
	 * @date:    2018年6月12日 下午2:16:43
	 * @since JDK 1.7
	 */
	int updateRowCol(SheetDesignBind sheetDesignBind, String userId) throws Exception;
	int deleteRowCol(SheetDesignBind sheetDesignBind, String userId) throws Exception;
	List<Map<String, Object>> queryExcel(SheetDesignBind sheetDesignBind) throws Exception;
}
