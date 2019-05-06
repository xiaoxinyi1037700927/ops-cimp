package com.sinosoft.ops.cimp.repository.emp;

import com.sinosoft.ops.cimp.entity.emp.EmpPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface EmpPhotoRepository extends JpaRepository<EmpPhoto, String>, QuerydslPredicateExecutor<EmpPhoto> {

    EmpPhoto getByEmpId(String empId);
}
