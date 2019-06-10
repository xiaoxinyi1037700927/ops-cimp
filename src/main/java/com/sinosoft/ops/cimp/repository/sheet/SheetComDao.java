package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.model.PageableQueryParameter;
import com.sinosoft.ops.cimp.common.model.PageableQueryResult;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSet;
import com.sinosoft.ops.cimp.entity.sheet.SheetInfo;
import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeItem;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;



/**
 * 
 * @ClassName:  SheetComDao
 * @description: Sheet 通用数据访问接口
 * @author:        sunch
 * @date:            2018年6月7日 下午6:29:15
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetComDao {

	List<SysCodeItem> getChildCodeItemList(Integer codeSetId, String parentCode);
	
	/**
	 * 根据groupId获得信息集
	 * @param groupId
	 * @return SysInfoSet
	 * @author y
	 * @date:    2018年6月12日 下午4:02:07
	 * @since JDK 1.7
	 */
	Collection<SysInfoSet> getInfoSet();
	/**
	 * 根据infoSetId获取信息项
	 * @param infoSetId
	 * @return SysInfoItem
	 * @author lixianfu
	 * @date:    2018年6月12日 下午4:02:07
	 * @since JDK 1.7
	 */
	Collection<SysInfoItem> getByInfoSetId(Integer infoSetId);

	Object getValueBySql(String sql);

	List<Map> getListBySql(String sql);

	PageableQueryResult getQueryDataByPage(String querySql, PageableQueryParameter queryParameter);

	List<Map<String, Object>> getQueryData(String querySql);

	Collection<SheetInfo> getSheetInfos(Set<String> sheetNos, String parameterId, String parameterValue);

	Collection<SheetInfo> getSheetInfos(Set<String> sheetNos, Map<String, Object> parameter, String sql);
}
