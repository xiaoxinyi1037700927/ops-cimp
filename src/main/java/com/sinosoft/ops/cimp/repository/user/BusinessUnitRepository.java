package com.sinosoft.ops.cimp.repository.user;


import com.sinosoft.ops.cimp.entity.sys.user.BusinessUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BusinessUnitRepository extends JpaRepository<BusinessUnit,String>
        , QuerydslPredicateExecutor<BusinessUnit> {
}
