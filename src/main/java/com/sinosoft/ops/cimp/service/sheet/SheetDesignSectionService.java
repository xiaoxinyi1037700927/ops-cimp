package com.sinosoft.ops.cimp.service.sheet;


import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignSection;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/**
 * 
 * @ClassName:  SheetDesignSectionService
 * @date:            2018年5月28日 下午2:28:22
 */
public interface SheetDesignSectionService extends BaseEntityService<SheetDesignSection> {
  
    
   public Collection<SheetDesignSection> getByDesignId(UUID designId);

   public Integer getMaxOrdinal();

   public void moveSection(String id, String moveType);

   public Boolean checkSectionSame(SheetDesignSection entity);
   
   public String getMaxSectionNoByDesignId(UUID designId);

   /**
    * 根据表格设计id获取数据块树
    */
   public Collection<SheetDesignSection> getSectionTreeByDesignId(UUID designId);

   /**
    * 
    * 生成数据块树形结构
    * @param collection
    * @param designId
    */
   List<SheetDesignSection> generateSectionTree(Collection<SheetDesignSection> collection, UUID designId);

   /**
    * 
    * 数据绑定
    */
   public void dataBinding(Collection<SheetDesignSection> collection);

   /**
    * 
    * 根据表格设计id获取数据块集合，（可排序）
    * @param designId	排序字段
    * @param orderField	是否降序
    * @param desc
    */
   Collection<SheetDesignSection> getByDesignId(UUID designId, String orderField, boolean desc);

   public void fillData2Templement(Collection<Map<String, Object>> collection, byte[] content, HttpServletResponse response) throws UnsupportedEncodingException, Exception;

   /**根据模板id删除数据块*/
   public void deleteByDesignId(UUID designId);

   public boolean moveUp(SheetDesignSection entity, UUID designId);

   public boolean moveDown(SheetDesignSection entity, UUID designId);

   public Map<String, Integer> getRangeByDesignId(UUID designId);
}