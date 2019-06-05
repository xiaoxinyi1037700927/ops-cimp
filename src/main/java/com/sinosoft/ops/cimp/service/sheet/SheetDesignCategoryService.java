/**
 * @Project:      IIMP
 * @Title:          SheetCategoryService.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet;

import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCategory;
import com.sinosoft.ops.cimp.exception.CloneException;
import com.sinosoft.ops.cimp.common.model.TreeNode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;



/**
 * @ClassName:  SheetCategoryService
 * @Description: 表格分类服务接口
 * @Author:        Nil
 * @Date:            2017年8月18日 下午1:26:36
 * @Version        1.0.0
 */
public interface SheetDesignCategoryService extends BaseEntityService<SheetDesignCategory> {
    /**
     * 获取根表格分类
     */
    public Collection<SheetDesignCategory> getRoot();
    
    /**
     * 获取根表格分类并附带上所有的子孙节点，即完整的树
     * @return 根表格分类列表
     */
    public Collection<SheetDesignCategory> getRootWithDescendants();
	public Collection<SheetDesignCategory> getAllCategory();
	/**
	 * 根据标识获取子表格分类
	 * 
	 * @param id    标识
	 */
	public Collection<SheetDesignCategory> getChildren(UUID id);

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
	public SheetDesignCategory importFromFile(String filename) throws FileNotFoundException, ClassNotFoundException, IOException;
	
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
    UUID create(SheetDesignCategory entity);
	
	/**
	 * 
	 * 修改表目录方法
	 * @param id
	 * @return
	 * @author kanglin
	 * @date:    2018年6月13日 上午10:26:21
	 * @since JDK 1.7
	 */
	boolean updateName(SheetDesignCategory entity);
	
	/**
	 * 
	 * 删除表目录方法
	 * @param id
	 * @return
	 * @author kanglin
	 * @date:    2018年6月13日 上午10:29:40
	 * @since JDK 1.7
	 */
	boolean deleteLeaf(SheetDesignCategory entity);
	
	/**
	 * 
	 * 上移表目录方法
	 * @return
	 * @author kanglin
	 * @date:    2018年6月13日 上午10:27:10
	 * @since JDK 1.7
	 */
	boolean moveUp(SheetDesignCategory entity);
	
	/**
	 * 
	 * 下移表目录方法
	 * @param id
	 * @return
	 * @author kanglin
	 * @date:    2018年6月13日 上午10:27:53
	 * @since JDK 1.7
	 */
	boolean moveDown(SheetDesignCategory entity);
}