package com.sinosoft.ops.cimp.repository.user;

import com.sinosoft.ops.cimp.entity.user.RoleMenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * Created by Jay on 2019/1/8.
 *
 * @author Jay
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinoSoft All Rights Received
 */
public interface RoleGroupRepository extends JpaRepository<RoleMenuGroup,String>
        , QuerydslPredicateExecutor<RoleMenuGroup> {
}
