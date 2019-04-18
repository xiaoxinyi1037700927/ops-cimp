package com.sinosoft.ops.cimp.repository.sys.homepagecount;


import com.sinosoft.ops.cimp.entity.sys.homepagecount.HomePageCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface HomePageCountRepository extends JpaRepository<HomePageCount, String>, QuerydslPredicateExecutor<HomePageCount> {
}
