package com.sinosoft.ops.cimp.service.sys.syscode;


import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeSet;
import com.sinosoft.ops.cimp.entity.system.CodeSetType;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeSetAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeSetModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeSetSearchListModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeSetSearchModel;

import java.util.Collection;
import java.util.List;

public interface SysCodeSetService {

    boolean delSysCodeSetById(Integer id);

    boolean saveSysCodeSet(SysCodeSetAddModel sysCodeSetAddModel);

    boolean upSysCodeSet(SysCodeSetModifyModel sysCodeSetModifyModel);

    List<SysCodeSetModifyModel> findAllSysCodeSets();

    PaginationViewModel<com.sinosoft.ops.cimp.vo.to.sys.syscode.SysCodeSetDisplayModel> getPageSysCodeSet(SysCodeSetSearchModel sysCodeSetSearchModel);

    SysCodeSetModifyModel getSysCodeById(Integer id);

    List<com.sinosoft.ops.cimp.vo.to.sys.syscode.SysCodeSetObtainModel> getSysCodeSetAndSysCodeItem(SysCodeSetSearchListModel sysCodeSetSearchListModel);

    List<com.sinosoft.ops.cimp.vo.to.sys.syscode.SysCodeSetModel> getSysCodeSet();

    /**
     * 根据名称获取系统代码集
     * @param name 名称
     * @return 系统代码集
     */
    SysCodeSet getByName(String name);

    /**
     * 根据代码类型获取代码集
     * @param type 代码类型
     * @return 代码集
     */
    public Collection<SysCodeSet> getByType(CodeSetType type);

    /**
     * 获取全部的信息集
     * @return 信息集集合
     */
    public Collection<SysCodeSet> getAll();

    /**
     * 根据标识获取版本号
     * @param id 标识
     * @return 版本号
     */
    int getVersion(int id);

    /**
     * 根据名称获取版本号
     * @param name 名称
     * @return 版本号
     */
    int getVersion(String name);

    /**
     * 根据名称获取系统代码集标识
     * @param name 名称
     * @return 系统代码集标识
     */
    Integer getIdByName(String name);

    /**
     * 对对象列表进行排序
     * @param list 对象列表
     */
    void sort(List<? extends SysCodeSet> list);

}
