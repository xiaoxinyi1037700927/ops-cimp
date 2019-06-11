package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.entity.sheet.SheetDataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SheetDataSourceRepository extends JpaRepository<SheetDataSource,String>, QuerydslPredicateExecutor<SheetDataSource> {

}
