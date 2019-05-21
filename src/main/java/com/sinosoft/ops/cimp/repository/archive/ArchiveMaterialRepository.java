package com.sinosoft.ops.cimp.repository.archive;

import com.sinosoft.ops.cimp.entity.archive.ArchiveMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveMaterialRepository extends JpaRepository<ArchiveMaterial,String>, QuerydslPredicateExecutor<ArchiveMaterial> {


        @Query("select a from ArchiveMaterial a where a.empId=?1 order by a.categoryId,a.seqNum")
        public List<ArchiveMaterial> findAllByEmpIdOrderByCategoryIdSeqNum(String empId);

        @Query("select id from ArchiveMaterial  where empId=?1 and categoryId=?2")
        public List<String> findByEmpIdAndCategoryId(String empId,String categoryId);

        @Query("select a from ArchiveMaterial a where a.empId=?1 and a.categoryId=?2 order by a.seqNum")
        public List<ArchiveMaterial> findAllByEmpIdAndCategoryIdOrderBySeqNum(String empId,String categoryId);

        @Query(value = "select t1.A36013 from EMP_A36 t1 where ( 1=1 ) and t1.STATUS=0 and upper(t1.EMP_ID)=?1",nativeQuery=true)
        public List<String> findByemp(String empId);

        @Query(value = "select  a.a001003 from  EMP_A001 a where upper(a.emp_id)=?1 and a.STATUS=0",nativeQuery=true)
        public String findByemp001(String empId);

        @Query(value = "SELECT  RESERVE_CADRE_ID FROM  USER_INFO  WHERE ID=?1 ",nativeQuery=true)
        public String findEmpidByUserID(String userid);
}
