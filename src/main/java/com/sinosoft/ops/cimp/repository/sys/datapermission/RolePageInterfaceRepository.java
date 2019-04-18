package com.sinosoft.ops.cimp.repository.sys.datapermission;


import com.sinosoft.ops.cimp.entity.sys.datapermission.RolePageInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * Created by Jay on 2019/2/22.
 *
 * @author Jay
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinoSoft All Rights Received
 */
public interface RolePageInterfaceRepository extends JpaRepository<RolePageInterface,String>,QuerydslPredicateExecutor<RolePageInterface> {
}
