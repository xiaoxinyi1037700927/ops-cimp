package com.sinosoft.ops.cimp.repository.user;

import com.sinosoft.ops.cimp.entity.sys.user.SystemCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Collection;
import java.util.List;

public interface SystemCodeRepository extends JpaRepository<SystemCode,String>, QuerydslPredicateExecutor<SystemCode> {

    List<SystemCode> findByTypeCodeAndParentCodeIsNullOrderByOrdinal(String code);


    List<SystemCode> findByParentCode(String parentCode);

    List<SystemCode> findByParentId(String parentId);

    List<SystemCode> findByTypeCodeInAndParentCodeIsNullOrderByOrdinal(Collection typeCodeList);
}
