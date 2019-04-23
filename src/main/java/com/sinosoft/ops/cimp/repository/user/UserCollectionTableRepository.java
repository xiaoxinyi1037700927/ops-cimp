package com.sinosoft.ops.cimp.repository.user;

import com.sinosoft.ops.cimp.entity.user.UserCollectionTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface UserCollectionTableRepository extends JpaRepository<UserCollectionTable,String>,
        QuerydslPredicateExecutor<UserCollectionTable> {

    /**
     * 查询排序字段
     * @param userId
     * @return
     */
    @Query("select max(sortNumber) from UserCollectionTable where userId = ?1 ")
    Integer getSortNumberByUserId(String userId);


    /**
     * 根据userId查询
     * @param userId
     * @return
     */
    List<UserCollectionTable> findAllByUserIdOrderBySortNumberAsc(String userId);


}
