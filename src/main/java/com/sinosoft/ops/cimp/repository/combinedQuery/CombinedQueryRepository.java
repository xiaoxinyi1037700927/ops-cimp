package com.sinosoft.ops.cimp.repository.combinedQuery;

import com.sinosoft.ops.cimp.entity.combinedQuery.CombinedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CombinedQueryRepository extends JpaRepository<CombinedQuery, String>, QuerydslPredicateExecutor<CombinedQuery> {

}
