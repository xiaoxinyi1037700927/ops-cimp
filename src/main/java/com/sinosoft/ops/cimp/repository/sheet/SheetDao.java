package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.GatherInfo;
import com.sinosoft.ops.cimp.entity.sheet.Sheet;
import com.sinosoft.ops.cimp.entity.sheet.SheetGatherParameter;
import com.sinosoft.ops.cimp.exception.BusinessException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;



/**
 * 表格数据访问接口
 * @author wangft
 *
 */
public interface SheetDao extends BaseEntityDao<Sheet> {
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
	 * @param userId 用户标识
	 * @return 汇总生成的新表格
	 */
	public Sheet sum(String sheetNo, int version, Map<String, Object> params, UUID userId);
	
	/**
	 * 按表号和生成参数汇总表格（使用表格的初始版本）
	 * @param sheetNo 表格表号
	 * @param params 参数
	 * @param userId 用户标识
	 * @return 汇总生成的新表格
	 */
	public Sheet sum(String sheetNo, Map<String, Object> params, UUID userId);
	
	/**
	 * 按表号和生成参数校核表格
	 * @param sheetNo 表格表号
	 * @param version 版本号
	 * @param params 参数
	 * @param userId 用户标识
	 * @return 是否校核成功
	 * @throws BusinessException
	 */
	public boolean check(int reportNo, int version, Map<String, Object> params, UUID userId) throws BusinessException;
	
	/**
	 * 按表号和生成参数校核表格（使用表格的初始版本）
	 * @param sheetNo 表格表号
	 * @param params 参数
	 * @param userId 用户标识
	 * @return 是否校核成功
	 * @throws BusinessException
	 */
	public boolean check(int reportNo, Map<String, Object> params, UUID userId) throws BusinessException;
	
	/**
	 * 按表格标识校核表格（使用表格的初始版本）
	 * @param id 表格标识
	 * @param userId 用户标识
	 * @return 是否校核成功
	 * @throws BusinessException
	 */
	public boolean check(String id, UUID userId) throws BusinessException;
	
    /** 
     * 根据分类标识获取
     * @param categoryId 分类标识
     * @return 表格设计集
     * @author Ni
     * @since JDK 1.7
     */
	Collection<Sheet> getByCategoryId(UUID categoryId);
	Object getOnlyByCategoryId(UUID categoryId);
		
    /**
     * 批量增加序号
     * TODO请描述一下这个方法
     * @param upOrdinal
     * @param categorytId
     * @return
     * @author kanglin
     * @date:    2018年6月13日 上午10:58:07
     * @since JDK 1.7
     */
    boolean addOrdinals(int upOrdinal, UUID categoryId, UUID userName);
    
    /**
     * 
     * 得到目录下所有节点的最大ordinal
     * @param categoryId
     * @return
     * @author kanglin
     * @date:    2018年6月15日 上午9:42:23
     * @since JDK 1.7
     */
    int getMaxOrdinal(UUID categoryId);
	
    /**
     * 删除节点后，后面的节点ordinal减1
     * @param upOrdinal
     * @param categorytId
     * @return
     * @author kanglin
     * @date:    2018年6月13日 上午10:58:07
     * @since JDK 1.7
     */
    int minusOrdinals(int thisOrdinal, UUID categoryId, UUID userName);
    
    /**
     * 
     * 上一个节点
     * @param id
     * @return 上一个节点
     * @author kanglin
     * @date:    2018年6月13日 上午11:38:38
     * @since JDK 1.7
     */
    Sheet findPrevious(UUID id, UUID categoryId);
    
    /**
     * 
     * 节点换ordinal
     * @param id
     * @param previousOrdinal
     * @return 
     * @author kanglin
     * @date:    2018年6月13日 上午11:41:44
     * @since JDK 1.7
     */
    int updateOrdinal(UUID id, int newOrdinal, UUID userName);
    
    /**
     * 
     * 下一个节点
     * @param id
     * @return 上一个节点
     * @author kanglin
     * @date:    2018年6月13日 上午11:38:38
     * @since JDK 1.7
     */
    Sheet findNext(UUID id, UUID categoryId);

	List<Map> getSummaryData(Map queryParameter);

	public List<GatherInfo> getGatherInfos(UUID id, Integer rowNo, Integer columnNo);

	public Collection<Map<String, Object>> getParameterInfoByDesignId(UUID designId);

	public Collection<SheetGatherParameter> getGatherParameterValue(UUID sheetId);

	public Collection<Sheet> getByDesignId(UUID designId);

	//取得引用情况
	List<Map> getRefSituation(String id);
	int checkLevel(String loginName, String sheetId, String type);
	
	/** 
	 * 获取祖先机构名称
	 * @param myId 本机构标识
	 * @param ancestorId 祖先机构标识
	 * @return 祖先机构名称
	 * @author Ni
	 * @date:    2019年1月6日 下午4:31:50
	 * @since JDK 1.7
	 */
	String getAncestorOrganizationName(String myId, String ancestorId);
	
	/** 
	 * 归档报表
	 * @param categoryId 报表分类标识
	 * @param designId 报表设计标识
	 * @param userId 用户标识
	 * @param organizationId 机构标识
	 * @return 影响记录数
	 * @author Ni
	 * @date:    2019年1月6日 下午4:53:05
	 * @since JDK 1.7
	 */
	int archiveById(UUID categoryId, UUID designId, UUID userId, String organizationId);
	
    /** 
     * 取消归档报表
     * @param categoryId 报表分类标识
     * @param designId 报表设计标识
     * @param userId 用户标识
     * @return 影响记录数
     * @author Ni
     * @date:    2019年1月6日 下午4:53:05
     * @since JDK 1.7
     */
    int unarchiveById(UUID categoryId, UUID designId, UUID userId);	
}
