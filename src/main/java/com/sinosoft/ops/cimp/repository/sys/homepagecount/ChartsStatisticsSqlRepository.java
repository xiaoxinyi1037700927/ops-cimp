package com.sinosoft.ops.cimp.repository.sys.homepagecount;


import com.sinosoft.ops.cimp.entity.sys.homepagecount.ChartsStatisticsSql;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ChartsStatisticsSqlRepository extends JpaRepository<ChartsStatisticsSql, String>, QuerydslPredicateExecutor<ChartsStatisticsSql> {
}
