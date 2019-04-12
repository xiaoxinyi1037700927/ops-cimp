package com.sinosoft.ops.cimp.repository.code;


import com.sinosoft.ops.cimp.entity.sys.code.SysCodeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface SysCodeItemRepository extends JpaRepository<SysCodeItem, Integer> ,QuerydslPredicateExecutor<SysCodeItem> {

    List<SysCodeItem> findByCodeSetId(Integer codeSetId);

}
