package com.sinosoft.ops.cimp.repository.user;


import com.sinosoft.ops.cimp.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,String>, QuerydslPredicateExecutor<Role> {

    /**
     * 根据角色id查询角色列表
     * @param roleIdList
     * @return
     */
    List<Role> findByIdIn(List<String> roleIdList);

    /**
     * 根据code查询
     * @param code
     * @return
     */
    List<Role> findByCode(String code);

    List<Role> findByCodeIn(List<String> codeList);


}
