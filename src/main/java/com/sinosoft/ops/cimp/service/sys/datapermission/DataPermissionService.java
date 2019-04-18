package com.sinosoft.ops.cimp.service.sys.datapermission;


import com.sinosoft.ops.cimp.vo.to.sys.datapermission.PageInterfaceVO;

import java.util.List;

/**
 * Created by Jay on 2019/2/22.
 *
 * @author Jay
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinoSoft All Rights Received
 */
public interface DataPermissionService {

    /**
     * 保存
     * */
    Boolean savePageInterface(PageInterfaceVO vo);

    /**
     * 查询
     * */
    List<PageInterfaceVO> findPageInterfaceVOList(String permissionPageId, String roleId);

    /**
     *根据接口和角色查询
     * */
    String findDataByInterfaceAndRoleId(String pageInterfaceId, String roleId);

    /**
     *根据接口和角色查询
     * */
    String findSearchCondition();

    /**
     * 给角色下接口填入sql
     * */
    Boolean fillSqlToRole(String id, String roleId, String roleSql);
}
