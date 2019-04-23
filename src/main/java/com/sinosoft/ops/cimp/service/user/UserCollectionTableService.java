package com.sinosoft.ops.cimp.service.user;

import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.user.userCollectionTable.UCTableAddModel;
import com.sinosoft.ops.cimp.vo.from.user.userCollectionTable.UCTableSearchModel;
import com.sinosoft.ops.cimp.vo.to.user.userCollectionTable.UCTableModifyModel;
import com.sinosoft.ops.cimp.vo.to.user.userCollectionTable.UCTableViewModel;

import java.util.List;

public interface UserCollectionTableService {

    /**
     * 查询用户收藏table分页列表
     * @param searchModel
     * @return
     */
    PaginationViewModel<UCTableViewModel> findUCTablePageList(UCTableSearchModel searchModel);

    /**
     * 根据用户id查询 用户收藏表
     * @param userId
     * @return
     */
    List<UCTableViewModel> findUCTableListByUserId(String userId);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    UCTableModifyModel findUCTableById(String id);

    /**
     * 新增
     * @param addModel
     * @return
     */
    Boolean saveUCTable(UCTableAddModel addModel);

    /**
     * 修改
     * @param modifyModel
     * @return
     */
    Boolean modifyUCTable(UCTableViewModel modifyModel);

    /**
     * 删除
     * @param ids
     * @return
     */
    Boolean deleteUCTable(List<String> ids);

    /**
     * 上移 下移
     * @param ids
     * @return
     */
    Boolean changeUCTableSort(List<String> ids);

}
