package com.sinosoft.ops.cimp.service.sys.syscode;


import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeItem;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeItemAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeItemModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeItemPageModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SysCodeItemService {

    boolean delSysCodeItemById(Integer id);

    boolean saveSysCodeItem(SysCodeItemAddModel sysCodeItemAddModel);

    boolean upSysCodeItem(SysCodeItemModifyModel sysCodeItemModifyModel);

    PaginationViewModel<SysCodeItemModifyModel> getSysCodeItemBySearchModel(SysCodeItemPageModel sysCodeItemPageModel);

    SysCodeItemModifyModel getSysCodeItemById(Integer codeSetId);

    List<SysCodeItem> findByCodeSetName(String name);

    /**
     * 根据代码集标识和代码获取代码项
     * @param setId 代码集标识
     * @param code 代码
     * @return 代码项
     */
    SysCodeItem getByCode(int setId,String code);
    /**
     * 根据代码集名称和代码获取代码项
     * @param setName 代码集名称
     * @param code 代码
     * @return 代码项
     */
    SysCodeItem getByCode(String setName,String code);

    /**
     * 根据代码集标识和代码获取代码描述
     * @param setId 代码集标识
     * @param code 代码
     * @return 代码描述
     */
    String getDescription(int setId,String code);
    /**
     * 根据代码集名称和代码获取代码描述
     * @param setName 代码集名称
     * @param code 代码
     * @return 代码描述
     */
    String getDescription(String setName,String code);

    /**
     * 根据代码集名称和代码获取扩展名称(代码英文名+中文名)
     * @param setId 代码集标识
     * @param code 代码
     * @return 扩展名称
     */
    String getNameEx(int setId, String code);
    /**
     * 根据代码集名称和代码获取扩展名称(代码英文名+中文名)
     * @param setName 代码集名称
     * @param code 代码
     * @return 扩展名称
     */
    String getNameEx(String setName, String code);

    /**
     * 获取代码项标识
     * @param setId 代码集标识
     * @return 代码项标识集
     */
    Collection<Integer> getIdsBySetId(int setId);
    /**
     * 获取代码项标识
     * @param setName 代码集名称
     * @return 代码项标识集
     */
    Collection<Integer> getIdsBySetName(String setName);

    /**
     * 获取代码和标识映射集
     * @param setId 代码集标识
     * @return 代码和标识映射集
     */
    Map<String,Integer> getCode2IdsBySetId(int setId);
    /**
     * 获取代码和标识映射集
     * @param setName 代码集名称
     * @return 代码和标识映射集
     */
    Map<String,Integer> getCode2IdsBySetName(String setName);

    /**
     * 根据标识获取
     * @param id 标识
     * @return 实体
     */
    SysCodeItem getById(Serializable id);

    /**
     * 根据标识获取
     * @param id 标识
     * @param lock 是否锁定
     * @return 实体
     */
    SysCodeItem getById( Serializable id, boolean lock );
}
