package com.sinosoft.ops.cimp.service.sheet;

import com.alibaba.fastjson.JSONArray;
import com.sinosoft.ops.cimp.common.model.DefaultTreeNode;
import com.sinosoft.ops.cimp.common.model.PageableQueryParameter;
import com.sinosoft.ops.cimp.common.model.PageableQueryResult;
import com.sinosoft.ops.cimp.common.model.TreeNode;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSet;
import com.sinosoft.ops.cimp.entity.sheet.*;
import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeItem;
import com.sinosoft.ops.cimp.esl1.SheetEvalueateListener;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sheet.impl.SheetComServiceImpl.SheetStatisticsStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 
 * @ClassName:  SheetComService
 * @description: Sheet 通用服务接口
 * @author:        sunch
 * @date:            2018年6月7日 下午3:55:29
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetComService {

	public List<SysCodeItem> getChildCodeItemList(Integer codeSetId, String parentCode);



	Collection<TreeNode> generateItemTree(Collection<SysCodeItem> itemList, String codeSetName);



	public List<DefaultTreeNode> conversionTree(List<SysCodeItem> list);
	/**
	 * 获得信息集服务层
	 * @return SysInfoSet
	 * @author lixianfu
	 * @date:    2018年6月12日 下午4:02:07
	 * @since JDK 1.7
	 */
	Collection<SysInfoSet> getInfoSet();

	/**
	 * 根据位置获取item树
	 * @param designId
	 * @param rowNo
	 * @param columnNo
	 */
	List<HashMap<String, Object>> getItemTreeByDataPosition(String designId, int rowNo, int columnNo);

	/**
	 * 根据数据块编号获取item树
	 * @param designId
	 * @param sectionNo
	 */
	List<HashMap<String, Object>> getItemTreeBySectionNo(String designId, String sectionNo);

	/**
	 * 根据infoSetId获取信息项业务层
	 * @param infoSetId
	 * @return SysInfoItem
	 * @author lixianfu
	 * @date:    2018年6月12日 下午4:02:07
	 * @since JDK 1.7
	 */
	Collection<SysInfoItem> getByInfoSetId(Integer infoSetId);


	//获得每个cell的数据
	public SheetData mergeEachOption(SheetDesignDataSource dataSource, SheetDesignField designField,
									 Collection<SheetDesignCondition> conditions, int row, int column, int betweenRow, int betweenColumn, JSONArray paramArray) throws BusinessException;

	//获得某个区间的数据
	List<SheetData> mergeListData(SheetDesignSection designSection, SheetDesignDataSource dataSource, SheetDesignField designField,
								  Collection<SheetDesignCondition> conditions, JSONArray paramArray) throws Exception;

	/**
	 * 
	 * 根据行号列号确定单元格所在的唯一数据源
	 * @param designDataSources	数据源集合
	 * @param row	行号
	 * @param column	列号
	 * @return	唯一数据源
	 */
	public SheetDesignDataSource getUniqueDataSource(Collection<SheetDesignDataSource> designDataSources, int row, int column);


	/**
	 * 
	 * 根据行号列号确定单元格所绑定的唯一数据项
	 * @param designFields	数据项集合
	 * @param row	行号
	 * @param column	列号
	 * @return	唯一数据项
	 */
	public SheetDesignField getUniqueDesignField(Collection<SheetDesignField> designFields, int row, int column);



	/**
	 * 
	 * 根据行号列号确定单元格所绑定的条件项集合
	 * @param designConditions	条件项集合
	 * @param row	行号
	 * @param column	列号
	 * @return	条件项集合
	 */
	public Collection<SheetDesignCondition> getDesignConditions(List<SheetDesignCondition> designConditions, int row,
                                                                int column);


	/**
	 * 
	 * 公式运算和数据校核
	 * @param sheetDatas
	 * @param
	 * @param expressions 
	 * @return 
	 * @throws BusinessException 
	 */
	public List<Map<String, Object>> formulaCaculation(List<SheetData> sheetDatas, Map<String, Integer> range, Collection<SheetDesignExpression> expressions) throws BusinessException;



	public void exportQueryData(String[] fieldsDes, List<Map<String, Object>> result, HttpServletResponse response) throws Exception;



	public SheetDesignDataSource getUniqueDataSource(Collection<SheetDesignDataSource> designDataSources, String sectionNo);



	public SheetDesignField getUniqueDesignField(Collection<SheetDesignField> designFields, String sectionNo);



	public Collection<SheetDesignCondition> getDesignConditions(List<SheetDesignCondition> designConditions,
                                                                String sectionNo);



	String getQuerySql(SheetDesignDataSource dataSource, SheetDesignField designField, Collection<SheetDesignCondition> conditions, JSONArray paramArray, Map mapformat)  throws BusinessException;



	Object getValueBySql(String querySql);


	/**
	 * 填充数据到Pdf并响应到客户端
	 * @param content
	 * @param collection
	 * @param response
	 */
	public void fillData2Pdf(byte[] content, Collection<Map<String, Object>> collection, HttpServletResponse response) throws Exception;

	/**
	 * 填充数据到Word并响应到客户端
	 * @param content
	 * @param collection
	 * @param response
	 * @throws Exception 
	 */
	public void fillData2Word(byte[] content, Collection<Map<String, Object>> datas, HttpServletResponse response, String name) throws Exception;

	Map<String, Object> getFormulaCaculationData(List<Map<String, Object>> sheetDatas, Map<String, Integer> range,
                                                 Collection<SheetDesignExpression> expressions, Integer rowNo, Integer columnNo);



	public SheetDesignSection getUniqueSection(Collection<SheetDesignSection> sections, Integer rowNo, Integer columnNo);


	void exportFormulaCaculationData(List<Map<String, Object>> list, HttpServletResponse response) throws IOException;


	/**
	 * 
	 * 根据绝对位置获取唯一数据块
	 * @param sections
	 * @param ctrlRowNo
	 * @param ctrlColumnNo
	 * @return SheetDesignSection
	 */
	public SheetDesignSection getUniqueSectionByCtrl(Collection<SheetDesignSection> sections, Integer ctrlRowNo,
                                                     Integer ctrlColumnNo);


	/**
	 * excel报表查询反查
	 */
	PageableQueryResult getQueryDataByPage(Collection<SheetDesignDataSource> designDataSources,
										   Collection<SheetDesignField> designFields, List<SheetDesignCondition> designConditions, PageableQueryParameter queryParameter, Integer rowNo,
										   Integer columnNo, String[] fields, JSONArray paramArray)  throws BusinessException ;

	/**
	 * word报表查询反查
	 */
	PageableQueryResult getQueryDataByPage(Collection<SheetDesignDataSource> designDataSources,
                                           Collection<SheetDesignField> designFields, List<SheetDesignCondition> designConditions,
                                           PageableQueryParameter queryParameter, UUID designId, String sectionNo, String[] fields, JSONArray paramArray)
			throws BusinessException;

	/**
	 * 
	 * excel获取所有查询反查数据
	 * @param designDataSources
	 * @param designFields
	 * @param designConditions
	 * @param rowNo
	 * @param columnNo
	 * @param fields
	 * @param paramArray
	 * @return
	 * @throws BusinessException
	 * @author sunch
	 * @date:    2018年8月23日 上午10:39:47
	 * @since JDK 1.7
	 */
	List<Map<String, Object>> getQueryData(Collection<SheetDesignDataSource> designDataSources, Collection<SheetDesignField> designFields,
                                           List<SheetDesignCondition> designConditions, Integer rowNo, Integer columnNo, String[] fields,
                                           JSONArray paramArray)  throws BusinessException ;

	/**
	 * 
	 * word获取所有反查数据
	 * @param designDataSources
	 * @param designFields
	 * @param designConditions
	 * @param sectionNo
	 * @param fields
	 * @param paramArray
	 * @return
	 * @throws BusinessException
	 * @author sunch
	 * @date:    2018年8月23日 上午10:40:41
	 * @since JDK 1.7
	 */
	List<Map<String, Object>> getQueryData(Collection<SheetDesignDataSource> designDataSources,
                                           Collection<SheetDesignField> designFields, List<SheetDesignCondition> designConditions, UUID designId, String sectionNo,
                                           String[] fields, JSONArray paramArray) throws BusinessException;


	/**
	 * 
	 * excel报表查看sql描述
	 * @param designDataSources
	 * @param designFields
	 * @param designConditions
	 * @param rowNo
	 * @param columnNo
	 * @param paramArray
	 */
	Map<String, Object> getSqlDescription(Collection<SheetDesignDataSource> designDataSources,
                                          Collection<SheetDesignField> designFields, List<SheetDesignCondition> designConditions, Integer rowNo,
                                          Integer columnNo, JSONArray paramArray)throws BusinessException;
	/**
	 * 
	 * word报表查看sql描述
	 * @param designDataSources
	 * @param designFields
	 * @param designConditions
	 * @param sheetNo
	 * @param paramArray
	 */
	Map<String, Object> getSqlDescription(Collection<SheetDesignDataSource> designDataSources,
                                          List<SheetDesignCondition> designConditions, UUID designId, String sectionNo,
                                          JSONArray paramArray) throws BusinessException;


	Map<String, Object> getQueryDataSql(Collection<SheetDesignDataSource> designDataSources, Collection<SheetDesignField> designFields,
                                        List<SheetDesignCondition> designConditions, Integer rowNo, Integer columnNo, String[] fields, String[] fieldsDes,
                                        JSONArray paramArray)  throws BusinessException ;



	public Collection<SheetInfo> getSheetInfos(Set<String> sheetNos, String parameterId, String parameterValue);



	SheetEvalueateListener analyzeFormula(String formula, Map<String, Map<String, Object>> dataMap, Integer startRowNo,
										  Integer endRowNo, Integer startColumnNo, Integer endColumnNo);



	String getFormulaCaculation(Collection<SheetDesignExpression> expressions);



	public Collection<SheetInfo> getSheetInfos(Set<String> sheetNos, Collection<SheetParameter> parameters, UUID categoryId);



	public SheetEvalueateListener analyzeFormula(String formula, Map<String, Map<String, Object>> dataMap,
                                                 Integer startRowNo, Integer endRowNo, Integer startColumnNo, Integer endColumnNo, Byte type);

	/**
	 * 获取公式校核数据
	 * @return 
	 */
	//public List<Map<String, Object>> getFormulaCheckData(String formula, List<Map<String, Object>> result, Map<String,Map<String, Object>> dataMap);



	/**
	 * 
	 * 根据相对位置获取绝对位置
	 * @param designId
	 * @param rowNo
	 * @param columnNo
	 * @return
	 */
	Map<String, Integer> getCtrlPositionByRelativePosition(UUID designId, Integer rowNo, Integer columnNo);


	/**
	 * 
	 * 获取excel计算的表格数据
	 * @param designId
	 * @param paramArray
	 * @return
	 * @throws BusinessException
	 * @throws Exception
	 * @author sunch
	 * @param priority 
	 * @date:    2018年9月30日 上午11:06:35
	 * @since JDK 1.7
	 */
	List<SheetData> getComputeDatas4Excel(UUID userId, UUID sheetId, UUID designId, JSONArray paramArray, SheetComService sheetComService, Integer priority) throws BusinessException, Exception;


	/**
	 * 
	 * 批量直统报表
	 * @param designId
	 * @param jsonArray
	 * @param sheetComService
	 * @param userId
	 * @author sunch
	 * @param designId 
	 * @throws Exception 
	 * @throws BusinessException 
	 * @date:    2018年10月25日 上午9:28:24
	 * @since JDK 1.7
	 */
	public void batchDirectStatistic(UUID id, UUID designId, JSONArray jsonArray, SheetComService sheetComService, UUID userId) throws BusinessException, Exception;



	List<Map<String, Object>> formulaCheck(String sheetId, String designId, String formula) throws BusinessException;



	public void cancelStatistics(UUID userId, UUID key);



	SheetStatisticsStatus pollingGetStatus(UUID userId, UUID key);



	List<Map<String, Object>> fillUnit(UUID designId, UUID sheetId);


	/**
	 * 
	 * 公式运算（报表直统适用）
	 * @param sheetId
	 * @param designId
	 * @param sheetDatas
	 * @return
	 * @throws BusinessException
	 */
	List<Map<String, Object>> formulaCaculation(String sheetId, String designId, UUID userId, List<SheetData> sheetDatas)
			throws BusinessException;


	/**
	 * 添加汇总标记
	 * @param string
	 * @param id
	 * @author sunch
	 * @date:    2018年11月26日 下午11:57:01
	 * @since JDK 1.7
	 */
	public void addGatherSign(String string, String id);


	/**
	 * 移除汇总标记
	 * @param string
	 * @param id
	 * @author sunch
	 * @date:    2018年11月26日 下午11:57:22
	 * @since JDK 1.7
	 */
	public void removeGatherSign(String string, String id);



	void saveFormulaSheetData(UUID sheetId, UUID designId, UUID userId, SheetStatisticsStatus status) throws BusinessException, Exception;



	public void cancelStatisticsByUserId(UUID userId);



	public void removeGatherSignByUserId(UUID userId);



	SheetData queryDataBySql(String querySql, int row, int column, int betweenRow, int betweenColumn)
			throws BusinessException;



	Map<String, Object> getFormulaCaculationData(String sheetId, String designId, List<Map<String, Object>> sheetDatas,
                                                 Integer rowNo, Integer columnNo) throws BusinessException;



	public Map<String, Object> viewSheetSize();

}
