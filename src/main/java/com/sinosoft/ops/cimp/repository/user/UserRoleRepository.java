package com.sinosoft.ops.cimp.repository.user;


import com.sinosoft.ops.cimp.entity.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole,String>, QuerydslPredicateExecutor<UserRole> {

    /**
     * 根据用户ID查询拥有的角色
     * @param userId
     * @return
     */
    List<UserRole> findByUserId(String userId);

    void deleteByUserId(String userId);

    void deleteByUserIdIn(List<String> userIdList);
}
