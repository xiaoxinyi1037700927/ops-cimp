package com.sinosoft.ops.cimp.service.sheet;

import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.Sheet;
import com.sinosoft.ops.cimp.entity.sheet.SheetData;
import com.sinosoft.ops.cimp.entity.sheet.SheetGatherInfo;
import com.sinosoft.ops.cimp.entity.sheet.SheetTag;
import com.sinosoft.ops.cimp.exception.BusinessException;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;



/**
 * 表格服务
 * @author Nil
 * @version 1.0
 * @created 21-6月-2017 13:17:26
 */
public interface SheetService extends BaseEntityService<Sheet> {

    /**
     * 根据表格分类获取表格设计
     * @param categoryId 表格分类标识
     */
    public Collection<Sheet> getByCategoryId(UUID categoryId);
	Object getOnlyByCategoryId(UUID categoryId);

	/**
     * 
     * 得到主表目录以下所有的子目录，包含主目录
     * @param topCatigoryId
     * @return
     * @author kanglin
     * @date:    2018年6月14日 下午12:52:51
     * @since JDK 1.7
     */
    Collection<UUID> getDownCatigories(UUID topCatigoryId);
    
    /**
     * 
     * 增加新模板
     * @return
     * @author kanglin
     * @date:    2018年6月15日 上午9:09:19
     * @since JDK 1.7
     */
    UUID create(Sheet entity, UUID categoryId);
    
    /** 
     * 新建报表
     * @param sheet 报表
     * @param sheetDatas 报表数据集
     * @return
     * @author Ni
     * @date:    2018年10月18日 下午1:34:04
     * @since JDK 1.7
     */
    UUID create(Sheet sheet, Collection<SheetData> sheetDatas);
    
    /**
     * 
     * 增加套表新报表
     * @return
     * @author kanglin
     * @date:    2018年6月15日 上午9:09:19
     * @since JDK 1.7
     */
    boolean createBatch(Sheet entity, UUID categoryId, UUID designCategoryId, String[] designIdNos, String params, String type, String parameterId);
	boolean createBatchInCategory(Sheet entity, UUID categoryId, UUID designCategoryId, String[] designIdNos, String params, String type, String parameterId);

	/**
     * 
     * 修改模板
     * @return
     * @author kanglin
     * @date:    2018年6月15日 上午9:09:19
     * @since JDK 1.7
     */
    int updateOne(Sheet entity);
    
    /**
     * 
     * 删除模板
     * @return
     * @author kanglin
     * @date:    2018年6月15日 上午9:09:19
     * @since JDK 1.7
     */
    int delete(Sheet entity, UUID categoryId);
	
	/**
	 * 
	 * 上移表模板方法
	 * @return
	 * @author kanglin
	 * @date:    2018年6月13日 上午10:27:10
	 * @since JDK 1.7
	 */
	boolean up(Sheet entity, UUID categoryId);
	
	/**
	 * 
	 * 下移表模板方法
	 * @param id
	 * @return
	 * @author kanglin
	 * @date:    2018年6月13日 上午10:27:53
	 * @since JDK 1.7
	 */
	boolean down(Sheet entity, UUID categoryId);
	
	/**
	 * 按设计标识和参数获取表格
	 * @param designId    表格设计标识
	 * @param params    参数列表
	 */
	public Collection<Sheet> getByDesignId(UUID designId, Map<String, Object> params);
	
	/**
	 * 按设计标识和参数获取表格 
	 * @param sheetNo    表格表号
	 * @param version    版本号
	 * @param params    参数列表
	 */
	public Collection<Sheet> getByReportNo(String sheetNo, int version, Map<String, Object> params);
	/**
	 * 按表号和生成参数获取表格（使用表格的初始版本）
	 * @param sheetNo    表格表号
	 * @param params    参数列表
	 */
	public Collection<Sheet> getByReportNo(String sheetNo, Map<String, Object> params);
	
	   /**
     * 按表格设计标识和生成参数计算表格
     * @param designId 表格设计标识
     * @param params 参数列表
     * @param userId 用户标识
     * @return 计算所得的表格
     */
    public Sheet calculate(UUID designId, Map<String, Object> params, UUID userId);
	/**
	 * 按表号和生成参数计算表格
	 * @param sheetNo 表格表号
	 * @param version 版本号
	 * @param params 参数列表
	 * @param userId 用户标识
	 * @return 计算所得的表格
	 */
	public Sheet calculate(String sheetNo, int version, Map<String, Object> params, UUID userId);
	/**
	 * 按表号和生成参数计算表格
	 * @param sheetNo 表格表号
	 * @param params 参数列表
	 * @param userId 用户标识
	 * @return 计算所得的表格
	 */
	public Sheet calculate(String sheetNo, Map<String, Object> params, UUID userId);
	
	/**
	 * 按表号和生成参数汇总表格
	 * @param sheetNo 表格表号
	 * @param version 版本号
	 * @param params 参数
	 * @return 汇总生成的新表格
	 */
	public Sheet sum(String sheetNo, int version, Map<String, Object> params, UUID userId);
	/**
	 * 按表号和生成参数汇总表格（使用表格的初始版本）
	 * @param sheetNo 表格表号
	 * @param params 参数
	 * @return 汇总生成的新表格
	 */
	public Sheet sum(String sheetNo, Map<String, Object> params, UUID userId);
	
	/**
	 * 归档表格
	 * @param id 表格标识
	 * @return 是否归档成功
	 * @throws BusinessException
	 */
	boolean archive(UUID id, UUID userId) throws BusinessException;
	/** 
	 * 归档表格
	 * @param categoryId 报表分类标识
     * @param ids 报表标识
     * @param userId 用户标识
     * @param organizationId 机构标识
     * @return 影响的记录数
	 * @throws BusinessException
	 * @author Ni
	 * @date:    2019年1月6日 下午1:32:34
	 * @since JDK 1.7
	 */
	int archiveByIds(UUID categoryId, String[] ids, UUID userId, String organizationId) throws BusinessException;
	
    /**
       * 解档表格
       * @param id 表格标识
       * @param userId 用户标识
       * @return 是否成功
       * @throws BusinessException
       */
    boolean unarchive(UUID id, UUID userId) throws BusinessException;
    /** 
     * 取消归档表格
     * @param categoryId 报表分类标识
     * @param ids 报表标识
     * @param userId 用户标识
     * @param organizationId 机构标识
     * @return 影响的记录数
     * @throws BusinessException
     * @author Ni
     * @date:    2019年1月6日 下午1:27:44
     * @since JDK 1.7
     */
    int unarchiveByIds(UUID categoryId, String[] ids, UUID userId, String organizationId) throws BusinessException;
	
	/**
     * 将表格导出到文件
     * @param id 表格标识
     * @param filename 文件名
     * @return 是否成功
     * @throws FileNotFoundException
	 * @throws IOException
	 * @throws BusinessException
	 */
	public boolean exportToFile(UUID id, String filename) throws FileNotFoundException, IOException, BusinessException;
	/**
     * 从文件中导入表格
     * @param filename 文件名
     * @param userId 用户标识
     * @return 是否成功
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public Sheet importFromFile(String filename, UUID userId) throws ClassNotFoundException, IOException;
	
    /**
     * 按表格标识校核表格
     * @param id 表格标识
     * @param userId 用户标识
     * @return 是否校核成功
     */
    public boolean check(UUID id, UUID userId);
	/**
	 * 按表号和生成参数校核表格
	 * @param sheetNo 表格表号
	 * @param version 版本号
	 * @param params 参数
	 * @param userId 用户标识
	 * @return 是否校核成功
	 */
	public boolean check(String sheetNo, int version, Map<String, Object> params, UUID userId);
	/**
	 * 按表号和生成参数校核表格（使用表格的初始版本）
	 * @param sheetNo 表格表号
	 * @param params 参数
	 * @param userId 用户标识
	 * @return 是否校核成功
	 */
	public boolean check(String sheetNo, Map<String, Object> params, UUID userId);
	
    /** 
     * 获取表格类型
     * @return 表格类型映射表
     */
    public Map<Byte,String> getSheetTypeMap();
    
    /** 
     * 获取表格类型
     * @return 表格类型映射表列表
     */
    public Collection<Map<String,String>> getSheetType();

    /**
     * 
     * 汇总报表
     * @param dataMap	要汇总的报表集合
     * @param id 
     * @param sheetIds 	要汇总的报表id
     * @return	汇总数据
     */
	public Collection<SheetData> gatherSheet(Map<String, Map<String, SheetData>> dataMap, String id, String[] sheetIds);

	List<Map> getSummaryData(Map queryParameter);

	public List<Map<String, Object>> getGatherInfos(UUID id, Integer rowNo, Integer columnNo);

	public void exportGatherData(HttpServletResponse response, Collection<Map<String, Object>> parameterInfos, List<Map<String, Object>> gatherInfos) throws Exception;

	public void saveInfosAndDatas(UUID id, Collection<SheetData> sheetDatas, Collection<SheetGatherInfo> sheetGatherInfos, Collection<SheetTag> sheetTags, boolean bAuto);

	public Collection<Map<String, Object>> getParameterInfoByDesgignId(UUID designId);

	/** 
	 * 根据标识删除
	 * @param id 标识
	 * @throws BusinessException 如果已归档，抛出不允许操作的异常
	 * @author Ni
	 * @date:    2019年1月14日 下午6:02:41
	 * @since JDK 1.7
	 */
	void deleteById(UUID id) throws BusinessException;
	/** 
	 * 根据标识集删除
	 * @param ids 标识集
	 * @throws BusinessException 如果已归档，抛出不允许操作的异常
	 * @author Ni
	 * @date:    2019年1月14日 下午6:03:32
	 * @since JDK 1.7
	 */
	void deleteByIds(String[] ids) throws BusinessException;

	/** 
	 * 更新标志
	 * @param ids 报表标识
	 * @param newFlag 新标志
	 * @param notation 批注
	 * @param userId 用户标识
	 * @param organizationId 机构标识
	 */
	void updateFlagByIds(String[] ids, int newFlag, String notation, UUID userId, String organizationId) throws BusinessException;

	public Collection<Sheet> getByDesignId(UUID designId);

	//取得引用情况
	List<Map> getRefSituation(String id);
	int checkLevel(String loginName, String sheetId, String type);
}