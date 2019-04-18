package com.sinosoft.ops.cimp.service.user.permissionPage;


import com.sinosoft.ops.cimp.vo.from.user.permissionPage.PermissionPageSearchVO;
import com.sinosoft.ops.cimp.vo.user.PermissionPageOperationVO;
import com.sinosoft.ops.cimp.vo.user.PermissionPageVO;

import java.util.List;

/**
 * Created by Jay on 2019/1/29.
 *
 * @author Jay
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinoSoft All Rights Received
 */
public interface PermissionPageService {

    /**
     * 查询页面所禁用的操作
     * */
    List<PermissionPageOperationVO> findProhibitOperation(PermissionPageSearchVO searchVO);

    /**
     * 新增页面
     * */
    Boolean addPermissionPage(PermissionPageVO permissionPageVO);

    /**
     * 查询页面
     * */
    List<PermissionPageVO> findPermissionPage(PermissionPageSearchVO searchVO);

    /**
     * 新增操作
     * */
    Boolean addPermissionPageOperation(PermissionPageOperationVO permissionPageOperationVO);

    /**
     * 查询页面操作
     * */
    List<PermissionPageOperationVO> findPermissionPageOperation(String permissionPageId, String roleId);

    /**
     * 启用/禁用操作
     * */
    Boolean switchPermissionPageOperation(String permissionPageOperationId, String roleId);

    /**
     * 启用/禁用操作
     * */
    Boolean switchPermissionPageOperation(List<String> permissionPageOperationIds, String roleId, String type);

    /**
     * 同步页面数量
     * */
    void pageCount(String permissionId);




}
