package com.sinosoft.ops.cimp.repository.user;


import com.sinosoft.ops.cimp.entity.sys.user.HomePageCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface HomePageCountRepository extends JpaRepository<HomePageCount, String>, QuerydslPredicateExecutor<HomePageCount> {
}
