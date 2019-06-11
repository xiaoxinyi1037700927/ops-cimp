/**
 * @Project:      IIMP
 * @Title:          SheetCategoryService.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet;

import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetCategory;
import com.sinosoft.ops.cimp.exception.CloneException;
import com.sinosoft.ops.cimp.common.model.TreeNode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;



/**
 * @ClassName:  SheetCategoryService
 * @Description: 表格分类服务接口
 * @Author:        kanglin
 * @Date:            2017年8月18日 下午1:26:36
 * @Version        1.0.0
 */
public interface SheetCategoryService extends BaseEntityService<SheetCategory> {
    /**
     * 获取根表格分类
     */
    public Collection<SheetCategory> getRoot();
    
    /**
     * 获取根表格分类并附带上所有的子孙节点，即完整的树
     * @return 根表格分类列表
     */
    public Collection<SheetCategory> getRootWithDescendants(String type, Integer param);
    
    /**
     * 根据用户拥有的单位或党组织 权限 获取分类树（包含本级和下级）
     * @param type
     * @param organizationIds	用户拥有的组织
     * @return
     */
    public Collection<SheetCategory> getRootWithDescendantsByOrg(String type, Collection<String> organizationIds, Byte param);
	/**
	 * 根据标识获取子表格分类
	 * 
	 * @param id    标识
	 */
	public Collection<SheetCategory> getChildren(UUID id);

	//获取下级分类信息
	List<Map> getCategoryMessage(UUID parentId);

	/**
	 * 将表格设计分类导出到文件
	 * @param id 表格设计分类标识
	 * @param filename 文件名
	 * @return 是否成功
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public boolean exportToFile(UUID id, String filename) throws FileNotFoundException, IOException;
	
	/**
	 * 从文件中导入表格设计分类
	 * @param filename 文件名
	 * @return 是否成功
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 */
	public SheetCategory importFromFile(String filename) throws FileNotFoundException, ClassNotFoundException, IOException;
	
	/***
	 * 根据表格类型获取全部的表格分类包括业务分类的树型数据
	 * @param type 表格类型
	 * @return 树型数据列表
	 */
	public Collection<TreeNode> getAllWithClassByType(Byte type) throws CloneException;
    
    /**
     * 
     * 新增表目录方法
     * @param parentId
     * @param name
     * @param upOrdinal
     * @return
     * @author kanglin
     * @date:    2018年6月13日 上午10:23:43
     * @since JDK 1.7
     */
    UUID create(SheetCategory entity);
	
	/**
	 * 
	 * 修改表目录方法
	 * @param id
	 * @return
	 * @author kanglin
	 * @date:    2018年6月13日 上午10:26:21
	 * @since JDK 1.7
	 */
	boolean updateName(SheetCategory entity);
	
	/**
	 * 
	 * 删除表目录方法
	 * @param id
	 * @return
	 * @author kanglin
	 * @date:    2018年6月13日 上午10:29:40
	 * @since JDK 1.7
	 */
	boolean deleteLeaf(SheetCategory entity);
	
	/**
	 * 
	 * 上移表目录方法
	 * @return
	 * @author kanglin
	 * @date:    2018年6月13日 上午10:27:10
	 * @since JDK 1.7
	 */
	boolean moveUp(SheetCategory entity);
	
	/**
	 * 
	 * 下移表目录方法
	 * @param id
	 * @return
	 * @author kanglin
	 * @date:    2018年6月13日 上午10:27:53
	 * @since JDK 1.7
	 */
	boolean moveDown(SheetCategory entity);

	/**
	 * 根据给定的组织id 获取最顶级 组织id
	 * @param organizationIds
	 * @param reportType 
	 * @return
	 */
	public String getTopOrgId(Collection<String> organizationIds, Integer reportType);

	Collection<SheetCategory> getWithDescendantsByOrg(String type, Collection<String> organizationIds, Byte param);

}