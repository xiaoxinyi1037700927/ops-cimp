package com.sinosoft.ops.cimp.service.sheet;


import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesign;
import com.sinosoft.ops.cimp.exception.BusinessException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * 表格设计服务
 * 
 * @author Nil
 * @version 1.0
 * @created 21-6月-2017 13:17:19
 */
public interface SheetDesignService extends BaseEntityService<SheetDesign> {

    /**
     * 根据表格分类获取表格设计
     * @param categoryId 表格分类标识
     */
    public Collection<SheetDesign> getByCategoryId(UUID categoryId);

    /**
     * 锁定设计
     * @param id 设计标识
     * @param userId 用户标识
     * @return 是否成功
     * @throws BusinessException
     */
    public boolean lock(UUID id, UUID userId) throws BusinessException;

    /**
     * 解锁设计
     * 
     * @param id 设计标识
     * @param userId 用户标识
     * @return 是否成功
     * @throws BusinessException
     */
    public boolean unlock(UUID id, UUID userId) throws BusinessException ;

    /**
     * 归档表格设计
     * @param id 表格设计标识
     * @return 是否归档成功
     * @throws BusinessException
     */
    public boolean archive(UUID id, UUID userId) throws BusinessException;
    /**
     * 解档表格设计
     * @param id 表格设计标识
     * @param userId 用户标识
     * @return 是否成功
     * @throws BusinessException
     */
    public boolean unarchive(UUID id, UUID userId) throws BusinessException;

    /**
     * 将表格设计导出到文件
     * @param id 表格设计标识
     * @param filename 文件名
     * @return 是否成功
     * @throws FileNotFoundException
     * @throws IOException
     * @throws BusinessException
     */
    public boolean exportToFile(UUID id, String filename) throws FileNotFoundException, IOException, BusinessException;

    /**
     * 从文件中导入表格设计
     * 
     * @param filename 文件名
     * @param userId 用户标识
     * @return 导入的表格设计对象
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     */
    public SheetDesign importFromFile(String filename, UUID userId)
            throws FileNotFoundException, IOException, ClassNotFoundException;


    /**
     * 表格设计的版本号增加一
     * @param id 表格设计标识
     * @param userId 用户标识
     * @return 是否成功
     * @throws BusinessException
     */
    public boolean increaseVersion(UUID id, UUID userId) throws BusinessException;
    
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
    UUID create(SheetDesign entity, UUID categoryId);

    //判断模板号是否重复
    boolean checkSheetNo(String sheetNo);
    boolean checkSheetNo(String SheetNo, UUID id);

    //发布
    void output(SheetDesign entity);

    /**
     *
     * 另存新模板
     * @return
     * @author wangyg
     * @date:    2018年7月27日
     * @since JDK 1.7
     */
    UUID saveAs(SheetDesign entity, UUID categoryId)  throws Exception;

    //导出条件
    String ouputCondition(UUID id, String FilePath) throws Exception;

    //根据模板取得绑定的条件
    Collection<String> getSecCondition(String designId, String sectionNo);

    Collection<String> getSecField(String designId, String sectionNo);

    String write2File(UUID id, String FilePath);
    void readAndSave(LinkedHashMap serializableMap, String categoryId) throws  Exception;

    /**
     * 
     * 复制模板
     * @return
     * @author kanglin
     * @date:    2018年6月15日 上午9:09:19
     * @since JDK 1.7
     */
    UUID copy(SheetDesign entity, UUID categoryId);
    

    
    /**
     * 
     * 修改模板
     * @return
     * @author kanglin
     * @date:    2018年6月15日 上午9:09:19
     * @since JDK 1.7
     */
    int updateOne(SheetDesign entity);
    
    /**
     * 
     * 删除模板
     * @return
     * @author kanglin
     * @date:    2018年6月15日 上午9:09:19
     * @since JDK 1.7
     */
    boolean delete(SheetDesign entity, UUID categoryId);
	
	/**
	 * 
	 * 上移表模板方法
	 * @return
	 * @author kanglin
	 * @date:    2018年6月13日 上午10:27:10
	 * @since JDK 1.7
	 */
	boolean moveUp(SheetDesign entity, UUID categoryId);
	
	/**
	 * 
	 * 下移表模板方法
	 * @param
	 * @return
	 * @author kanglin
	 * @date:    2018年6月13日 上午10:27:53
	 * @since JDK 1.7
	 */
	boolean moveDown(SheetDesign entity, UUID categoryId);

    //取得引用情况
    List<Map> getRefSituation(String id);

    void setRefNum(Object o);

    /**
     * 
     * 根据父级单位id获取所有子级包含父级的 单位id集合
     * @param organizationIds
     * @return
     * @author sunch
     * @date:    2018年8月6日 下午6:12:47
     * @since JDK 1.7
     */
	public Collection<String> getAllOrganizationByParent(Collection<String> organizationIds);
}