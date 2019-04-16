package com.sinosoft.ops.cimp.repository.user;


import com.sinosoft.ops.cimp.entity.sys.user.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * Created by Jay on 2018/12/9.
 *
 * @author Jay
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinoSoft All Rights Received
 */
public interface MenuGroupRepository extends JpaRepository<MenuGroup,String>
        , QuerydslPredicateExecutor<MenuGroup> {
}
