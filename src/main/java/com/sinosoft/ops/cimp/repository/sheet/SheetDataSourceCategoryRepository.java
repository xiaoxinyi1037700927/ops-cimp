package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.entity.sheet.SheetDataSourceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SheetDataSourceCategoryRepository extends JpaRepository<SheetDataSourceCategory,String>, QuerydslPredicateExecutor<SheetDataSourceCategory> {

}
