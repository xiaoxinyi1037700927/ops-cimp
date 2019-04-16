package com.sinosoft.ops.cimp.repository.user.permissionPage;


import com.sinosoft.ops.cimp.entity.sys.user.PermissionPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * Created by Jay on 2019/1/29.
 *
 * @author Jay
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinoSoft All Rights Received
 */
public interface PermissionPageRepository extends JpaRepository<PermissionPage,String>, QuerydslPredicateExecutor<PermissionPage> {
}
