package com.sinosoft.ops.cimp.repository.sys.departmentInfo;

import com.sinosoft.ops.cimp.vo.from.DepB001;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DepB001Repository extends JpaRepository<DepB001,String>,QuerydslPredicateExecutor<DepB001> {
    DepB001 findByTreeLevelCode(String code);
}
