package com.sinosoft.ops.cimp.repository.user;



import com.sinosoft.ops.cimp.entity.sys.user.RoleHomePageCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface RoleHomePageCountRepository extends JpaRepository<RoleHomePageCount, String>, QuerydslPredicateExecutor<RoleHomePageCount> {

    void deleteByIdIn(List<String> idList);
}
